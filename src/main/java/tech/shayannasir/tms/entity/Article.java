package tech.shayannasir.tms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import tech.shayannasir.tms.enums.ArticleStatus;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends AuditEntity{

    String title;
    @Lob
    String description;

    ArticleStatus status;

    @ManyToMany
    List<Tag> tags;

    @OneToMany(mappedBy = "article")
    List<Comment> comments;

    @OneToOne
    Attachment coverPic;

    @OneToMany(mappedBy = "article")
    List<Attachment> attachments;

    Long views;

}
