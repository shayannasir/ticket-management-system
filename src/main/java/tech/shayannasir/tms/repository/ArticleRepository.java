package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
