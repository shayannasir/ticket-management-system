package tech.shayannasir.tms.controller;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.ArticleService;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    public ResponseDTO createArticle(@Valid @RequestBody ArticleRequestDTO articleRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return articleService.createNewArticle(articleRequestDTO);
    }

    @GetMapping("/details/{id}")
    public ResponseDTO fetchArticle(@PathVariable String id) {
        try {
            Long ID = Long.parseLong(id.trim());
            return articleService.fetchArticleDetails(ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, "Invalid Article ID");
        }
    }

    @PostMapping("/action")
    public ResponseDTO performAction(@Valid @RequestBody ArticleInsightRequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        }
        return articleService.performAction(requestDTO);
    }

    @PostMapping("/list")
    public DataTableResponseDTO<Object, List<ArticleResponseDTO>> getArticleList(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
        return articleService.fetchListOfArticles(dataTableRequestDTO);
    }

}
