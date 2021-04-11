package tech.shayannasir.tms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import tech.shayannasir.tms.enums.ArticleStatus;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    String description;

    ArticleStatus status;

    @ManyToMany
    List<Tag> tags;

    @OneToMany(mappedBy = "article")
    List<Comment> comments;

    @OneToOne
    Attachment coverPic;

    Long likes;
    Long dislikes;
    Long views;

}
