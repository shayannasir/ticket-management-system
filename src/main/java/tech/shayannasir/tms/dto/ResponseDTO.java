package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.enums.ErrorCode;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDTO<T> {

    private static String defaultErrorMessage = "Unable to process request";
    private static String defaultValidationErrorMessage = "Invalid request.Validation failed";

    private Boolean status = true;

    private String message;

    private T data;

    @JsonIgnore
    private List<AuditResponseDTO> auditResponses;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ErrorDTO> errors = new ArrayList<>();

    public ResponseDTO(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDTO<T> hasValue(Object mandatoryDataForValidation, String defaultErrorMessage) {
        if (Objects.isNull(mandatoryDataForValidation)) {
            this.setStatus(Boolean.FALSE);
            this.setMessage(defaultErrorMessage);
            this.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, defaultErrorMessage));
        }
        return this;
    }

    public ResponseDTO(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(Boolean status, String message, List<ErrorDTO> errorDTOS) {
        this.status = status;
        this.message = message;
        this.errors = errorDTOS;
    }

    public void addToErrors(List<ErrorDTO> errors) {
        this.getErrors().addAll(errors);
    }

    public void addToErrors(ErrorDTO errorDTO) {
        this.getErrors().add(errorDTO);
    }

    public void setAuditResponse(AuditResponseDTO auditResponse) {

        if (CollectionUtils.isEmpty(this.auditResponses)) {
            this.auditResponses = new ArrayList<>();
        }

        this.auditResponses.add(auditResponse);
    }
}
