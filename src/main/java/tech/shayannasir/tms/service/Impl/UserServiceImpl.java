package tech.shayannasir.tms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.UserDataBinder;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.enums.Role;
import tech.shayannasir.tms.repository.UserRepository;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.UserService;
import tech.shayannasir.tms.util.JwtUtil;

import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl extends MessageService implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDataBinder dataBinder;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User 404!");
        return user;
    }

    @Override
    public ResponseDTO createUser(UserDTO userDTO) {

        ResponseDTO responseDTO = new ResponseDTO(true, MessageConstants.USER_CREATED);
        validateCreateUserRequest(userDTO, responseDTO);
        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(MessageConstants.INVALID_REQUEST);
            return responseDTO;
        }

        User userEmail = userRepository.findByEmail(userDTO.getEmailId());
        User username = userRepository.findByUsername(userDTO.getUsername());

        if (userEmail != null || username != null) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(MessageConstants.EMAIL_USERNAME_EXIST);
            return responseDTO;
        }

        User user = User.builder()
                .email(userDTO.getEmailId())
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .accountEnabled(true) //To be Removed
                .accountExpired(false) //To be Removed
                .accountLocked(false) //To be Removed
                .credentialsExpired(false) //To be Removed
                .build();

        Role role = null;

        if (userDTO.getRole() != null) {
            role = userDTO.getRole();
            user.setRole(role);
            userRepository.save(user);
            responseDTO.setAuditResponse(AuditResponseDTO.createWithTarget(user.getId()));
            responseDTO.setData(userDTO);
            return responseDTO;
        } else {
            responseDTO.setStatus(false);
            responseDTO.setMessage(MessageConstants.ROLE_NOT_EXIST);
            return responseDTO;
        }
    }

    @Override
    public String logout(String token) {
        return null;
    }

    @Override
    public ResponseDTO<LoginResponseDTO> login(Authentication authentication) {
        ResponseDTO<LoginResponseDTO> responseDTO = new ResponseDTO<>();
        User user = (User) loadUserByUsername(authentication.getName());
        UserDTO userDTO = dataBinder.bindToUserSummaryDTO(user);
        String token = jwtUtil.generateToken(authentication);
        responseDTO.setData(new LoginResponseDTO(token, userDTO));
        user.setLastLoginTime(new Date());
        userRepository.save(user);
        responseDTO.setStatus(true);
        responseDTO.setMessage(getMessage(MessageConstants.LOGIN_SUCCESS, user.getUsername()));
        return responseDTO;
    }

    private void validateCreateUserRequest(UserDTO userDTO, ResponseDTO responseDTO) {
        responseDTO.hasValue(userDTO.getUsername(), getMessage(MessageConstants.USERNAME_NOT_NULL))
                .hasValue(userDTO.getName(), getMessage(MessageConstants.NAME_NOT_NULL))
                .hasValue(userDTO.getPassword(), getMessage(MessageConstants.PASSWORD_NOT_NULL))
                .hasValue(userDTO.getPhoneNumber(), getMessage(MessageConstants.PHONE_NUMBER_NOT_NULL))
                .hasValue(userDTO.getEmailId(), getMessage("emailId.not.blank"));
        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            return;
        }

        if (!userDTO.getPassword().matches(Constants.PASSWORD_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.PASSWORD_PATTERN_NOT_MATCH)));

        if (!userDTO.getPhoneNumber().matches(Constants.MOBILE_NUMBER_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_MOBILE_NUMBER)));

        if (!userDTO.getUsername().matches(Constants.USERNAME_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_USERNAME)));

        if (!userDTO.getName().matches(Constants.NAME_AND_DESIGNATION_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_NAME)));

        if (!userDTO.getEmailId().matches(Constants.EMAIL_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage("invalid.user.email")));

    }
}
