package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.Article;

import java.util.List;

public interface ArticleService {
    ResponseDTO createNewArticle(ArticleRequestDTO articleRequestDTO);
    ResponseDTO fetchArticleDetails(Long id);
    ResponseDTO performAction(ArticleInsightRequestDTO requestDTO);
    DataTableResponseDTO<Object, List<ArticleSummaryDTO>> fetchListOfArticles(DataTableRequestDTO dataTableRequestDTO);
    ResponseDTO editArticle(ArticleRequestDTO articleRequestDTO);
    Article validateArticle(Long articleID, ResponseDTO responseDTO);
    ResponseDTO fetchActions(Long articleID);
}
