package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.ArticleAction;

import java.util.List;

public interface ArticleActionRepository extends CrudRepository<ArticleAction, Long> {
    List<ArticleAction> findAllByArticleIDAndIsLiked(Long articleID, Boolean isLiked);
    List<ArticleAction> findAllByArticleID(Long articleID);
    ArticleAction findByArticleIDAndUserID(Long articleID, Long userID);

}
