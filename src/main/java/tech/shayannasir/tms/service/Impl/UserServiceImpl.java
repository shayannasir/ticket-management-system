package tech.shayannasir.tms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

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

        ResponseDTO responseDTO = new ResponseDTO(true, getMessage(MessageConstants.USER_CREATED));
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

    @Override
    public ResponseDTO getUserDetails(Long id) {
        User tokenUser = getCurrentLoggedInUser();

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            if (tokenUser.getRole().toString().equals(Role.SUPER_ADMIN.name()) || tokenUser.getUsername().equals(optionalUser.get().getUsername())) {
                return new ResponseDTO<UserDTO>(true, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY,
                        null, Locale.getDefault()), dataBinder.bindToUserSummaryDTO(optionalUser.get()));
            } else {
                return new ResponseDTO(false, getMessage(MessageConstants.OPERATION_NOT_PERMITTED));
            }
        }
        return new ResponseDTO(false, getMessage(MessageConstants.USER_NOT_FOUND, id));
    }

    @Override
    public ResponseDTO editUserDetails(UserDTO userDTO) {

        ResponseDTO responseDTO = new ResponseDTO(false, getMessage(MessageConstants.USER_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User tokenUser = (User) authentication.getPrincipal();

        if (tokenUser.getRole().name().equals((Role.SUPER_ADMIN).name()) || tokenUser.getUsername().equals(userDTO.getUsername())) {
            Optional<User> optionalUser = userRepository.findById(userDTO.getId());
            User usernameUser = userRepository.findByUsername(userDTO.getUsername());
            User emailUser = userRepository.findByEmail(userDTO.getEmailId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (emailUser != null && !emailUser.getId().equals(user.getId())
                    || usernameUser != null && !usernameUser.getId().equals(user.getId())) {
                    responseDTO.setStatus(false);
                    responseDTO.setMessage(getMessage(MessageConstants.EMAIL_USERNAME_EXIST));
                    return responseDTO;
                }
                user.setUsername(userDTO.getUsername());
                user.setEmail(userDTO.getEmailId());
                user.setName(userDTO.getName());
                user.setPhoneNumber(userDTO.getPhoneNumber());

                if (Objects.nonNull(userDTO.getAccountEnabled())) {
                    if (tokenUser.getRole().name().equals((Role.SUPER_ADMIN).name()))
                        user.setAccountEnabled(userDTO.getAccountEnabled());
                    else
                        userDTO.setAccountEnabled(user.getAccountEnabled());
                }

                userRepository.save(user);

                responseDTO.setStatus(true);
                responseDTO.setMessage(getMessage(MessageConstants.USER_UPDATED));
                responseDTO.setAuditResponse(AuditResponseDTO.createWithTarget(user.getId()));
                responseDTO.setData(userDTO);
                return responseDTO;
            }
        }
        return new ResponseDTO(false, getMessage(MessageConstants.OPERATION_NOT_PERMITTED));
    }

    @Override
    public ResponseDTO logout(String token) {
        ResponseDTO responseDTO = new ResponseDTO(true, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
        jwtUtil.inValidateToken(token);
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

    public User getCurrentLoggedInUser() {
        User user = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
                user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user != null) {
                    user = userRepository.findById(user.getId()).orElse(null);
                }
            }
        }
        return user;
    }
}
