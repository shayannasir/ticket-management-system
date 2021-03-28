package tech.shayannasir.tms.util;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ErrorDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;

import java.util.List;

public class ErrorUtil {

    public static ResponseDTO bindErrorResponse(ErrorCode errorCode, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(Boolean.FALSE);

        String errorMessage;

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMessage = error.getField() + ": " + error.getDefaultMessage();
            responseDTO.addToErrors(new ErrorDTO(errorCode, errorMessage));
        }
        return responseDTO;
    }

}
