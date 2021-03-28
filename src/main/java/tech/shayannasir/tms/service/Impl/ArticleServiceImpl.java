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
import tech.shayannasir.tms.enums.ArticleInsightAction;
import tech.shayannasir.tms.enums.ArticleStatus;
import tech.shayannasir.tms.enums.CommentSource;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.repository.ArticleRepository;
import tech.shayannasir.tms.repository.CommentRepository;
import tech.shayannasir.tms.repository.TagRepository;
import tech.shayannasir.tms.repository.UserRepository;
import tech.shayannasir.tms.service.ArticleService;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl extends MessageService implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ArticleBinder dataBinder;
    @Autowired
    private CommentBinder commentBinder;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserDataBinder userBinder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseDTO createNewArticle(ArticleRequestDTO articleRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        validateCreateArticleRequest(articleRequestDTO, responseDTO);
        List<Tag> tags = resourceService.mapTagValueToObjects(articleRequestDTO.getTags(), responseDTO);

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
            ArticleResponseDTO articleResponseDTO = dataBinder.bindDocumentToDTO(existingArticle.get());
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
    public DataTableResponseDTO<Object, List<ArticleResponseDTO>> fetchListOfArticles(DataTableRequestDTO dataTableRequestDTO) {
        List<ArticleResponseDTO> articleDTOs = new ArrayList<>();
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
        articleResults.stream().forEach(article -> {

            CreatedModifiedUserDTO createdModifiedUserDTO = userBinder.fetchCreatedAndModifiedUsersFor(article.getCreatedBy(), article.getLastModifiedBy());

            ArticleResponseDTO articleResponseDTO = ArticleResponseDTO.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .status(article.getStatus())
                    .tags(article.getTags())
                    .comments(article.getComments())
                    .likes(article.getLikes())
                    .dislikes(article.getDislikes())
                    .views(article.getViews())
                    .createdDate(article.getCreatedDate())
                    .lastModifiedDate(article.getLastModifiedDate())
                    .createdBy(createdModifiedUserDTO.getCreatedBy())
                    .lastModifiedBy(createdModifiedUserDTO.getModifiedBy())
                    .build();
            articleDTOs.add(articleResponseDTO);
        });

        DataTableResponseDTO<Object, List<ArticleResponseDTO>> responseDTO = DataTableResponseDTO.getInstance(articleDTOs, count);
        responseDTO.setRecordsTotal(articleRepository.count());
        return responseDTO;
    }

    private void validateCreateArticleRequest(ArticleRequestDTO articleRequestDTO, ResponseDTO responseDTO) {
        responseDTO.hasValue(articleRequestDTO.getTitle(), "Article Title cannot be Empty")
                .hasValue(articleRequestDTO.getDescription(), "Article Body cannot be Empty")
                .hasValue(articleRequestDTO.getStatus(), "Article Status cannot be Empty");

        if (!EnumUtils.isValidEnum(ArticleStatus.class, articleRequestDTO.getStatus().name()))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Article Status"));
    }
}
