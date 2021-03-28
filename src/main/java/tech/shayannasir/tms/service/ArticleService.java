package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ArticleInsightRequestDTO;
import tech.shayannasir.tms.dto.ArticleRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;

public interface ArticleService {
    ResponseDTO createNewArticle(ArticleRequestDTO articleRequestDTO);
    ResponseDTO fetchArticleDetails(Long id);
    ResponseDTO performAction(ArticleInsightRequestDTO requestDTO);
}
