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
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.LoginRequestDTO;
import tech.shayannasir.tms.dto.LoginResponseDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.UserDTO;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.UserService;

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
        return userService.createUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO authtenticationRequest) {

        if (StringUtils.isEmpty(authtenticationRequest.getUsername()) || StringUtils.isEmpty(authtenticationRequest.getPassword())) {
            return new ResponseDTO<>(false, messageService.getMessage(MessageConstants.USERNAME_PASSWORD_NOT_NULL));
        }

        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authtenticationRequest.getUsername(),
                            authtenticationRequest.getPassword())
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
}
