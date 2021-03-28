package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.enums.ArticleInsightAction;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleInsightRequestDTO {

    @NotNull
    Long articleID;
    @NotNull
    ArticleInsightAction actionType;
}
