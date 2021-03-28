package tech.shayannasir.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Article;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAll();
    List<Article> findAll(Sort sort);
    Page<Article> findAll(Pageable pageable);
}
