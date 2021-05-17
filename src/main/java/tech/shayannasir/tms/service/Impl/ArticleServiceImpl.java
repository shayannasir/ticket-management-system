package tech.shayannasir.tms.service.Impl;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.ArticleBinder;
import tech.shayannasir.tms.binder.CommentBinder;
import tech.shayannasir.tms.binder.UserDataBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.*;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.ArticleService;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;
import tech.shayannasir.tms.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleServiceImpl extends MessageService implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ArticleBinder dataBinder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private UserService userService;

    @Override
    public ResponseDTO createNewArticle(ArticleRequestDTO articleRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        validateCreateArticleRequest(articleRequestDTO, responseDTO);
        List<Tag> tags = resourceService.mapTagValueToObjects(articleRequestDTO.getTags(), responseDTO);
        Attachment attachment = attachmentRepository.findByName(articleRequestDTO.getFileName());
        if (Objects.isNull(attachment))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid File Name"));

        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }

        Article article = Article.builder()
                .title(articleRequestDTO.getTitle())
                .description(articleRequestDTO.getDescription())
                .status(articleRequestDTO.getStatus())
                .tags(tags)
                .coverPic(attachment)
                .likes(0L)
                .dislikes(0L)
                .views(0L)
                .build();

        articleRepository.save(article);
        responseDTO.setStatus(Boolean.TRUE);
        responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));

        return responseDTO;
    }

    @Override
    public ResponseDTO fetchArticleDetails(Long id) {

        Optional<Article> existingArticle = articleRepository.findById(id);
        if (existingArticle.isPresent()) {
            Article article = existingArticle.get();
            article.setViews(article.getViews() + 1);
            articleRepository.save(article);
            ArticleResponseDTO articleResponseDTO = dataBinder.bindDocumentToDTO(article);
            return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY), articleResponseDTO);
        }
        return new ResponseDTO(Boolean.FALSE, "No Article found by the provided Article ID");
    }

    @Override
    public ResponseDTO performAction(ArticleInsightRequestDTO requestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Optional<Article> existingArticle = articleRepository.findById(requestDTO.getArticleID());
        if (existingArticle.isPresent()) {
            Article article = existingArticle.get();
            try {
                ArticleInsightAction action = ArticleInsightAction.valueOf(requestDTO.getActionType().name());
                switch (action) {
                    case LIKE:
                        article.setLikes(article.getLikes() + 1);
                        break;
                    case DISLIKE:
                        article.setDislikes(article.getDislikes() + 1);
                        break;
                    case VIEW:
                        article.setViews(article.getViews() + 1);
                        break;
                }
                articleRepository.save(article);
                responseDTO.setStatus(Boolean.TRUE);
                responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
            } catch (Exception e) {
                responseDTO.setStatus(Boolean.FALSE);
                responseDTO.setMessage("Invalid Action Type");
            }
        }
        return responseDTO;
    }

    @Override
    public DataTableResponseDTO<Object, List<ArticleSummaryDTO>> fetchListOfArticles(DataTableRequestDTO dataTableRequestDTO) {
        List<ArticleSummaryDTO> articleDTOs = new ArrayList<>();
        List<Article> articleResults;
        long count;
        Sort sort = null;
        if (StringUtils.isNotBlank(dataTableRequestDTO.getSortColumn())) {
            sort = Sort.by(dataTableRequestDTO.getSortDirection(), dataTableRequestDTO.getSortColumn());
        }
        if (BooleanUtils.isTrue(dataTableRequestDTO.getFetchAllRecords())) {
            if (sort != null)
                articleResults = articleRepository.findAll(sort);
            else
                articleResults = articleRepository.findAll();

            count = articleResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<Article> articlePage = articleRepository.findAll(pageable);
            articleResults = articlePage.getContent();
            count = articlePage.getTotalPages();
        }
        articleResults.parallelStream().forEach(article -> {

            ArticleSummaryDTO articleResponseDTO = ArticleSummaryDTO.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .comments(article.getComments().size())
                    .likes(article.getLikes())
                    .dislikes(article.getDislikes())
                    .views(article.getViews())
                    .createdDate(article.getCreatedDate())
                    .coverPic(article.getCoverPic())
                    .build();
            Optional<User> createdBy = userRepository.findById(article.getCreatedBy());
            createdBy.ifPresent(user -> articleResponseDTO.setCreatedBy(user.getName()));
            articleDTOs.add(articleResponseDTO);
        });

        DataTableResponseDTO<Object, List<ArticleSummaryDTO>> responseDTO = DataTableResponseDTO.getInstance(articleDTOs, count);
        responseDTO.setRecordsTotal(articleRepository.count());
        return responseDTO;
    }

    @Override
    public ResponseDTO editArticle(ArticleRequestDTO articleRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, "Article updated successfully");
        Optional<Article> existing = articleRepository.findById(articleRequestDTO.getId());

        if (existing.isPresent()) {
            Article article = existing.get();
            User loggedInUser = userService.getCurrentLoggedInUser();

            if (article.getCreatedBy().equals(loggedInUser.getId()) || loggedInUser.getRole().name().equals(Role.SUPER_ADMIN.name())) {
                validateCreateArticleRequest(articleRequestDTO, responseDTO);
                List<Tag> tags = resourceService.mapTagValueToObjects(articleRequestDTO.getTags(), responseDTO);
                Attachment attachment = attachmentRepository.findByName(articleRequestDTO.getFileName());
                if (Objects.isNull(attachment))
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid File Name"));

                if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
                    responseDTO.setStatus(Boolean.FALSE);
                    responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
                    return responseDTO;
                }

                article.setTitle(articleRequestDTO.getTitle());
                article.setDescription(articleRequestDTO.getDescription());
                article.setStatus(articleRequestDTO.getStatus());
                article.setTags(tags);
                article.setCoverPic(attachment);

                articleRepository.save(article);
                responseDTO.setData(article);
                return responseDTO;
            }
            return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.OPERATION_NOT_PERMITTED));
        }

        return new ResponseDTO(Boolean.FALSE, "Article with the given ID not found");
    }

    private void validateCreateArticleRequest(ArticleRequestDTO articleRequestDTO, ResponseDTO responseDTO) {
        responseDTO.hasValue(articleRequestDTO.getTitle(), "Article Title cannot be Empty")
                .hasValue(articleRequestDTO.getDescription(), "Article Body cannot be Empty")
                .hasValue(articleRequestDTO.getStatus(), "Article Status cannot be Empty")
                .hasValue(articleRequestDTO.getFileName(), "Article Cover cannot be Empty");

        if (!EnumUtils.isValidEnum(ArticleStatus.class, articleRequestDTO.getStatus().name()))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Article Status"));
    }

    @Override
    public Article validateArticle(Long articleID, ResponseDTO responseDTO) {
        Optional<Article> optionalArticle = articleRepository.findById(articleID);
        if (optionalArticle.isPresent())
            return optionalArticle.get();
        responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Article ID"));
        return null;
    }
}
