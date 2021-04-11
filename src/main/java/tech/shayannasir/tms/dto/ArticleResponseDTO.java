package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import tech.shayannasir.tms.entity.Attachment;
import tech.shayannasir.tms.entity.Comment;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.enums.ArticleStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {

    Long id;

    String title;
    String description;
    ArticleStatus status;
    List<Tag> tags;
    List<Comment> comments;
    Long likes;
    Long dislikes;
    Long views;

    Date createdDate;
    Date lastModifiedDate;
    UserDetailDTO  createdBy;
    UserDetailDTO lastModifiedBy;
    Attachment coverPic;

}
