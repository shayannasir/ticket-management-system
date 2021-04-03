package tech.shayannasir.tms.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.EndUser;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.UserService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    public ResponseDTO createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return userService.createUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO authenticationRequest) {

        if (StringUtils.isEmpty(authenticationRequest.getUsername()) || StringUtils.isEmpty(authenticationRequest.getPassword())) {
            return new ResponseDTO<>(false, messageService.getMessage(MessageConstants.USERNAME_PASSWORD_NOT_NULL));
        }

        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userService.login(authentication);
        } catch (BadCredentialsException ex) {
            return new ResponseDTO<>(false, messageService.getMessage(MessageConstants.USERNAME_PASSWORD_NOT_MATCH));
        } catch (DisabledException ex) {
            return new ResponseDTO<>(false, messageService.getMessage(MessageConstants.USER_DISABLED));
        } catch (AuthenticationException ex) {
            return new ResponseDTO<>(false, ex.getMessage());
        }
    }

    @GetMapping("/details/{id}")
    public ResponseDTO getUserDetails(@PathVariable String id) {
        try {
            Long ID = Long.parseLong(id.trim());
            return userService.getUserDetails(ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_USER_ID));
        }
    }

    @PostMapping("/update")
    public ResponseDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.editUserDetails(userDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseDTO forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return userService.forgotUserPassword(forgotPasswordDTO);
    }

    @PostMapping("/reset-password")
    public ResponseDTO forgotPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return userService.resetUserPassword(resetPasswordDTO);
    }

    @PostMapping("/logout")
    public ResponseDTO logout(@RequestHeader(Constants.TOKEN_HEADER) String authHeader) {
        if (StringUtils.isNotEmpty(authHeader)) {
            return userService.logout(authHeader.substring(Constants.ESCAPE_BEARER));
        }
        return new ResponseDTO(false, messageService.getMessage(MessageConstants.INVALID_REQUEST));
    }

    @PostMapping("/list")
    public DataTableResponseDTO<Object, List<UserDTO>> getUserList(@RequestBody DataTableRequestDTO dataTableRequestDTO) {

        return userService.getListOfUsers(dataTableRequestDTO);

    }

    /*
    * End User APIs
    * */
    @PostMapping("/end-user/update")
    public ResponseDTO editEndUser(EndUserDTO endUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return userService.editEndUserDetails(endUserDTO);
    }

    @GetMapping("/end-user/fetch")
    public ResponseDTO getEndUser(@RequestParam String email) {
        if (StringUtils.isBlank(email))
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_EMAIL));
        return userService.fetchEndUserByEmail(email);
    }

    @PostMapping("/end-user/list")
    public DataTableResponseDTO<Object, List<EndUserDTO>> getEndUserList(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
        return userService.getListOfEndUsers(dataTableRequestDTO);
    }


}
