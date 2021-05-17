package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ArticleActionResponseDTO {
    Long userId;
    Boolean isLiked;
    Long articleID;
}
