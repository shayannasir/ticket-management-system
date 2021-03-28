package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.CommentRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;

public interface CommentService {

    ResponseDTO addComment(CommentRequestDTO commentRequestDTO);
}
