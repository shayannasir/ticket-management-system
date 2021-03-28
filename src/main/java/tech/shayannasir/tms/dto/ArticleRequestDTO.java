package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.enums.ArticleStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRequestDTO {

    Long id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotNull
    ArticleStatus status;

    List<String> tags;

    List<Long> comments;

}
