package tech.shayannasir.tms.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleAction extends AuditEntity {

    Long userID;
    Boolean isLiked;
    Long articleID;

}
