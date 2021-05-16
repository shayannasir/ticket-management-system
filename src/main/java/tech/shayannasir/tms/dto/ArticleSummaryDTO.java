package tech.shayannasir.tms.dto;

import lombok.*;
import tech.shayannasir.tms.entity.Attachment;
import tech.shayannasir.tms.enums.ArticleStatus;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSummaryDTO {

    Long id;
    String title;
    int comments;
    Long likes;
    Long dislikes;
    Long views;
    Date createdDate;
    String  createdBy;
    Attachment coverPic;


}
