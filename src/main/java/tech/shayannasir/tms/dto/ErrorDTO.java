package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import springfox.documentation.spring.web.json.Json;
import tech.shayannasir.tms.enums.ErrorCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private ErrorCode errorCode;

    private String errorMessage;

    private String propertyName;

    public ErrorDTO(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
