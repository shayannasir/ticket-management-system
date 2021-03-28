package tech.shayannasir.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.shayannasir.tms.dto.CommentRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.CommentService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseDTO addCommentToArticle(@Valid @RequestBody CommentRequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        else
            return commentService.addComment(requestDTO);
    }
}
