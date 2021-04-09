package tech.shayannasir.tms.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.CommentBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.CommentRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.entity.Article;
import tech.shayannasir.tms.entity.Comment;
import tech.shayannasir.tms.entity.Task;
import tech.shayannasir.tms.entity.Ticket;
import tech.shayannasir.tms.enums.CommentSource;
import tech.shayannasir.tms.enums.CommentType;
import tech.shayannasir.tms.repository.ArticleRepository;
import tech.shayannasir.tms.repository.CommentRepository;
import tech.shayannasir.tms.repository.TaskRepository;
import tech.shayannasir.tms.repository.TicketRepository;
import tech.shayannasir.tms.service.CommentService;
import tech.shayannasir.tms.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl extends MessageService implements CommentService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentBinder commentBinder;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseDTO addComment(CommentRequestDTO requestDTO) {
        CommentSource source = CommentSource.valueOf(requestDTO.getSource().name());
        ResponseDTO responseDTO = new ResponseDTO();
        Comment comment = commentBinder.bindToDocument(requestDTO);

        switch (source) {
            case ARTICLE:
                Optional<Article> existingArticle = articleRepository.findById(requestDTO.getSourceID());
                if (existingArticle.isPresent()) {
                    comment.setArticle(existingArticle.get());
                    commentRepository.save(comment);
                    responseDTO.setStatus(Boolean.TRUE);
                    responseDTO.setMessage("Comment Added Successfully");
                    return responseDTO;
                } else
                    return new ResponseDTO(Boolean.FALSE, "No Article Found with the ID");
            case TICKET:
                Optional<Ticket> existingTicket = ticketRepository.findById(requestDTO.getSourceID());
                if (existingTicket.isPresent()) {
                    comment.setTicket(existingTicket.get());
                    commentRepository.save(comment);
                    responseDTO.setStatus(Boolean.TRUE);
                    responseDTO.setMessage("Comment Added Successfully");
                    return responseDTO;
                } else
                    return new ResponseDTO(Boolean.FALSE, "No Ticket Found with the ID");
            case TASK:
                Optional<Task> existingTask = taskRepository.findById(requestDTO.getSourceID());
                if (existingTask.isPresent()) {
                    comment.setTask(existingTask.get());
                    commentRepository.save(comment);
                    responseDTO.setStatus(Boolean.TRUE);
                    responseDTO.setMessage("Comment Added Successfully");
                    return responseDTO;
                } else
                    return new ResponseDTO(Boolean.FALSE, "No Task Found with the ID");
        }
        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));
    }

}
