package tech.shayannasir.tms.binder;

import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.CommentRequestDTO;
import tech.shayannasir.tms.entity.Comment;
import tech.shayannasir.tms.enums.CommentStatus;

import java.util.Objects;

@Component
public class CommentBinder {

    public Comment bindToDocument(CommentRequestDTO source) {
        Comment target = new Comment();

        target.setBody(source.getBody());
        target.setSource(source.getSource());
        target.setType(source.getType());
        target.setSourceID(source.getSourceID());

        if (Objects.nonNull(source.getStatus()))
            target.setStatus(source.getStatus());
        else
            target.setStatus(CommentStatus.PUBLISHED);

        return target;
    }

}
