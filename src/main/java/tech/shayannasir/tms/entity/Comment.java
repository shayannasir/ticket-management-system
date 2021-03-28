package tech.shayannasir.tms.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.enums.CommentSource;
import tech.shayannasir.tms.enums.CommentStatus;
import tech.shayannasir.tms.enums.CommentType;

import javax.persistence.Entity;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment extends AuditEntity {

    String body;
    CommentStatus status;
    CommentSource source;
    CommentType type;
    Long sourceID;

}
