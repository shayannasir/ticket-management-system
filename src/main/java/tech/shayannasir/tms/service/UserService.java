package tech.shayannasir.tms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseDTO createUser(UserDTO userDTO);

    ResponseDTO logout(String token);

    ResponseDTO<LoginResponseDTO> login(Authentication authentication);

    ResponseDTO getUserDetails(Long id);

    ResponseDTO editUserDetails(UserDTO userDTO);

    DataTableResponseDTO<Object, List<UserDTO>> getListOfUsers(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO editEndUserDetails(EndUserDTO endUserDTO);

    ResponseDTO fetchEndUserByEmail(String email);

    DataTableResponseDTO<Object, List<EndUserDTO>> getListOfEndUsers(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO forgotUserPassword(ForgotPasswordDTO forgotPasswordDTO);

    ResponseDTO resetUserPassword(ResetPasswordDTO resetPasswordDTO);

    User validateUser(Long userID, ResponseDTO responseDTO);

    User getCurrentLoggedInUser();
}
