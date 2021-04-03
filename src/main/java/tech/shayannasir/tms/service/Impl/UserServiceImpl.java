package tech.shayannasir.tms.service.Impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import tech.shayannasir.tms.entity.Department;
import tech.shayannasir.tms.entity.EndUser;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.enums.Role;
import tech.shayannasir.tms.repository.DepartmentRepository;
import tech.shayannasir.tms.repository.EndUserRepository;
import tech.shayannasir.tms.repository.UserRepository;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.UserService;
import tech.shayannasir.tms.util.JwtUtil;

import java.util.*;
import java.util.function.BooleanSupplier;

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
    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

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
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }

        User userEmail = userRepository.findByEmail(userDTO.getEmailId());

        if (Objects.nonNull(userEmail)) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(getMessage(MessageConstants.EMAIL_USERNAME_EXIST));
            return responseDTO;
        }

        Department department = departmentRepository.findByValue(userDTO.getDepartment());

        if (Objects.isNull(department)) {
            responseDTO.setStatus(false);
            responseDTO.setMessage("Invalid Department");
            return responseDTO;
        }

        User user = User.builder()
                .username(userDTO.getEmailId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .designation(userDTO.getDesignation())
                .email(userDTO.getEmailId())
                .empID(userDTO.getEmpID())
                .phoneNumber(userDTO.getPhoneNumber())
                .department(department)
                .totalTasks(0L)
                .dueTasks(0L)
                .totalTickets(0L)
                .dueTickets(0L)
                .accountEnabled(true)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
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

        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.USER_NOT_FOUND));

        Department department = departmentRepository.findByValue(userDTO.getDepartment());
        if (Objects.isNull(department)) {
            responseDTO.setMessage("Invalid Department");
            return responseDTO;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User tokenUser = (User) authentication.getPrincipal();

        if (tokenUser.getRole().name().equals((Role.SUPER_ADMIN).name()) || tokenUser.getId().equals(userDTO.getId())) {
            Optional<User> optionalUser = userRepository.findById(userDTO.getId());
            User emailUser = userRepository.findByEmail(userDTO.getEmailId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (emailUser != null && !emailUser.getId().equals(user.getId())) {
                    responseDTO.setStatus(false);
                    responseDTO.setMessage(getMessage(MessageConstants.EMAIL_USERNAME_EXIST));
                    return responseDTO;
                }

                if (tokenUser.getRole().name().equals((Role.SUPER_ADMIN).name())) {
                    user.setEmail(userDTO.getEmailId());
                    user.setUsername(userDTO.getEmailId());
                    user.setName(userDTO.getName());
                    user.setDesignation(userDTO.getDesignation());
                    user.setDepartment(department);
                    user.setEmpID(userDTO.getEmpID());
                    user.setRole(userDTO.getRole());
                    if (Objects.nonNull(userDTO.getAccountEnabled()))
                        user.setAccountEnabled(userDTO.getAccountEnabled());
                }
                user.setPhoneNumber(userDTO.getPhoneNumber());

                userRepository.save(user);

                responseDTO.setStatus(true);
                responseDTO.setMessage(getMessage(MessageConstants.USER_UPDATED));
                return responseDTO;
            }
        }
        return new ResponseDTO(false, getMessage(MessageConstants.OPERATION_NOT_PERMITTED));
    }

    @Override
    public ResponseDTO forgotUserPassword(ForgotPasswordDTO forgotPasswordDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.INVALID_REQUEST));

        User user = getCurrentLoggedInUser();
        if (Objects.nonNull(user)) {
            validateChangePasswordRequest(user, forgotPasswordDTO, responseDTO);

            if (responseDTO.getStatus()) {
                user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
                userRepository.save(user);
                responseDTO.setMessage("Password Changed Successfully");
                jwtUtil.inValidateToken(forgotPasswordDTO.getOldJwtToken());
            }
        } else {
            responseDTO.setMessage("User Not Found");
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO resetUserPassword(ResetPasswordDTO resetPasswordDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.INVALID_REQUEST));
        User user = getCurrentLoggedInUser();
        if (Objects.nonNull(user) && user.getRole().name().equals((Role.SUPER_ADMIN).name())) {
            Optional<User> optionalUser = userRepository.findById(resetPasswordDTO.getUserId());
            if (optionalUser.isPresent()) {
                User currentUser = optionalUser.get();
                validateResetPasswordRequest(currentUser, resetPasswordDTO, responseDTO);

                if (responseDTO.getStatus()) {
                    currentUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
                    userRepository.save(currentUser);
                    responseDTO.setMessage("Password Reset Successfully");
                }
            } else {
                responseDTO.setStatus(Boolean.FALSE);
                responseDTO.setMessage("Invalid UserID");
            }
        } else {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.OPERATION_NOT_PERMITTED));
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO logout(String token) {
        ResponseDTO responseDTO = new ResponseDTO(true, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
        jwtUtil.inValidateToken(token);
        return responseDTO;
    }

    @Override
    public DataTableResponseDTO<Object, List<UserDTO>> getListOfUsers(DataTableRequestDTO dataTableRequestDTO) {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> userResults;
        long resultCount;
        Sort sort = null;
        if (StringUtils.isNotBlank(dataTableRequestDTO.getSortColumn())) {
            sort = Sort.by(dataTableRequestDTO.getSortDirection(), dataTableRequestDTO.getSortColumn());
        }
        if (BooleanUtils.isTrue(dataTableRequestDTO.getFetchAllRecords())) {
            if (sort != null)
                userResults = userRepository.findAll(sort);
            else
                userResults = userRepository.findAll();

            resultCount = userResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<User> userPage = userRepository.findAll(pageable);
            userResults = userPage.getContent();
            resultCount = userPage.getTotalElements();
        }
        userResults.stream().forEach(user -> {
            UserDTO userDTO = UserDTO.builder()
                    .name(user.getName())
                    .id(user.getId())
                    .phoneNumber(user.getPhoneNumber())
                    .emailId(user.getEmail())
                    .username(user.getUsername())
                    .accountEnabled(user.getAccountEnabled())
                    .department(user.getDepartment().getName())
                    .totalTickets(user.getTotalTickets())
                    .dueTickets(user.getDueTickets())
                    .totalTasks(user.getTotalTasks())
                    .dueTasks(user.getDueTasks())
                    .designation(user.getDesignation())
                    .role(user.getRole())
                    .empID(user.getEmpID())
                    .createdDate(user.getCreatedDate())
                    .build();
            userDTOS.add(userDTO);
        });

        DataTableResponseDTO<Object, List<UserDTO>> responseDTO = DataTableResponseDTO.getInstance(userDTOS, resultCount);
        responseDTO.setRecordsTotal(userRepository.count());
        return responseDTO;
    }

    private void validateCreateUserRequest(UserDTO userDTO, ResponseDTO responseDTO) {
        responseDTO.hasValue(userDTO.getUsername(), getMessage(MessageConstants.USERNAME_NOT_NULL))
                .hasValue(userDTO.getName(), getMessage(MessageConstants.NAME_NOT_NULL))
                .hasValue(userDTO.getPassword(), getMessage(MessageConstants.PASSWORD_NOT_NULL))
                .hasValue(userDTO.getPhoneNumber(), getMessage(MessageConstants.PHONE_NUMBER_NOT_NULL))
                .hasValue(userDTO.getEmailId(), getMessage(MessageConstants.EMAIL_NOT_NULL));
        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            return;
        }

        if (!userDTO.getPassword().matches(Constants.PASSWORD_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.PASSWORD_PATTERN_NOT_MATCH)));

        if (!userDTO.getPhoneNumber().matches(Constants.MOBILE_NUMBER_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_MOBILE_NUMBER)));

        if (!userDTO.getUsername().equals(userDTO.getEmailId()))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_USERNAME)));

        if (!userDTO.getName().matches(Constants.NAME_AND_DESIGNATION_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_NAME)));

        if (!userDTO.getEmailId().matches(Constants.EMAIL_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_EMAIL)));

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

    private void validateChangePasswordRequest(User user, ForgotPasswordDTO passwordDTO, ResponseDTO responseDTO) {

        if (!passwordEncoder.matches(passwordDTO.getPreviousPassword(), user.getPassword())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Incorrect Old Password"));
        }
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "New and Confirm Password do not match"));
        }
        if (passwordDTO.getNewPassword().equals(passwordDTO.getPreviousPassword())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "New Password cannot be same as old password"));
        }

    }

    private void validateResetPasswordRequest(User user, ResetPasswordDTO passwordDTO, ResponseDTO responseDTO) {

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "New and Confirm Password do not match"));
        }
        if (passwordEncoder.matches(passwordDTO.getNewPassword(), user.getPassword())) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "New Password cannot be same as old Password"));
        }
    }

    /*
    * End User Services
    * */

    @Override
    public ResponseDTO editEndUserDetails(EndUserDTO endUserDTO) {
        ResponseDTO responseDTO = new ResponseDTO(false, getMessage(MessageConstants.USER_NOT_FOUND));
        Optional<EndUser> existingUser = endUserRepository.findById(endUserDTO.getId());
        if (existingUser.isPresent()) {
            EndUser endUser = existingUser.get();
            endUser.setName(endUserDTO.getName());
            endUser.setEmail(endUserDTO.getEmail());
            endUser.setNumber(endUserDTO.getNumber());
            endUser.setWorkID(endUserDTO.getWorkID());

            endUserRepository.save(endUser);

            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO fetchEndUserByEmail(String email) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.USER_NOT_FOUND));

        EndUser endUser = endUserRepository.findByEmail(email);
        if (Objects.nonNull(endUser)) {
            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
            responseDTO.setData(endUser);
        }
        return responseDTO;
    }

    @Override
    public DataTableResponseDTO<Object, List<EndUserDTO>> getListOfEndUsers(DataTableRequestDTO dataTableRequestDTO) {
        List<EndUserDTO> userDTOS = new ArrayList<>();
        List<EndUser> userResults;
        long resultCount;
        Sort sort = null;
        if (StringUtils.isNotBlank(dataTableRequestDTO.getSortColumn())) {
            sort = Sort.by(dataTableRequestDTO.getSortDirection(), dataTableRequestDTO.getSortColumn());
        }
        if (BooleanUtils.isTrue(dataTableRequestDTO.getFetchAllRecords())) {
            if (sort != null)
                userResults = endUserRepository.findAll(sort);
            else
                userResults = endUserRepository.findAll();

            resultCount = userResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<EndUser> userPage = endUserRepository.findAll(pageable);
            userResults = userPage.getContent();
            resultCount = userPage.getTotalElements();
        }
        userResults.stream().forEach(user -> {
            EndUserDTO userDTO = EndUserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .number(user.getNumber())
                    .workID(user.getWorkID())
                    .totalTickets(user.getTotalTickets())
                    .dueTickets(user.getDueTickets())
                    .build();
            userDTOS.add(userDTO);
        });

        DataTableResponseDTO<Object, List<EndUserDTO>> responseDTO = DataTableResponseDTO.getInstance(userDTOS, resultCount);
        responseDTO.setRecordsTotal(endUserRepository.count());
        return responseDTO;
    }

}
