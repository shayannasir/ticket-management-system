package tech.shayannasir.tms.enums;

import tech.shayannasir.tms.dto.ErrorDTO;

public enum ErrorCode {

    HTTP_MESSAGE_NOT_READABLE("HTTP Message not readable"),
    VALIDATION_ERROR("Validation error"),
    INVALID_REQUEST("Invalid request");

    private String errorDisplayValue;

    ErrorCode(String errorDisplayValue) {
        this.errorDisplayValue = errorDisplayValue;
    }

    public ErrorDTO getErrorDTOInstance() {
        return new ErrorDTO(this, this.errorDisplayValue);
    }

    public ErrorDTO getErrorDTOInstance(String errorMessage) {
        return new ErrorDTO(this, errorMessage);
    }


}
