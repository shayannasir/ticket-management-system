package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordDTO {

    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.PASSWORD_INVALID_MESSAGE)
    String previousPassword;
    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.PASSWORD_INVALID_MESSAGE)
    String newPassword;
    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.PASSWORD_INVALID_MESSAGE)
    String confirmPassword;
    @NotBlank
    String oldJwtToken;
}
