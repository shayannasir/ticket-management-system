package tech.shayannasir.tms.service.Impl;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.ArticleBinder;
import tech.shayannasir.tms.binder.CommentBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.Article;
import tech.shayannasir.tms.entity.Comment;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.entity.Ticket;
import tech.shayannasir.tms.enums.ArticleInsightAction;
import tech.shayannasir.tms.enums.ArticleStatus;
import tech.shayannasir.tms.enums.CommentSource;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.repository.ArticleRepository;
import tech.shayannasir.tms.repository.CommentRepository;
import tech.shayannasir.tms.repository.TagRepository;
import tech.shayannasir.tms.service.ArticleService;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

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

    private void validateCreateArticleRequest(ArticleRequestDTO articleRequestDTO, ResponseDTO responseDTO) {
        responseDTO.hasValue(articleRequestDTO.getTitle(), "Article Title cannot be Empty")
                .hasValue(articleRequestDTO.getDescription(), "Article Body cannot be Empty")
                .hasValue(articleRequestDTO.getStatus(), "Article Status cannot be Empty");

        if (!EnumUtils.isValidEnum(ArticleStatus.class, articleRequestDTO.getStatus().name()))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Article Status"));
    }
}
