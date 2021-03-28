package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;

import java.util.List;

public interface ArticleService {
    ResponseDTO createNewArticle(ArticleRequestDTO articleRequestDTO);
    ResponseDTO fetchArticleDetails(Long id);
    ResponseDTO performAction(ArticleInsightRequestDTO requestDTO);
    DataTableResponseDTO<Object, List<ArticleResponseDTO>> fetchListOfArticles(DataTableRequestDTO dataTableRequestDTO);
}
