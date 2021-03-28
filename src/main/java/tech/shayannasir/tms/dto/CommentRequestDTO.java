package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.enums.CommentSource;
import tech.shayannasir.tms.enums.CommentStatus;
import tech.shayannasir.tms.enums.CommentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDTO {

    @NotBlank
    String body;
    @NotNull
    CommentSource source;
    @NotNull
    CommentType type;
    @NotNull
    Long sourceID;

    CommentStatus status;
}
