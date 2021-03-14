package tech.shayannasir.tms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.shayannasir.tms.dto.*;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseDTO createUser(UserDTO userDTO);

    ResponseDTO logout(String token);

    ResponseDTO<LoginResponseDTO> login(Authentication authentication);

    ResponseDTO getUserDetails(Long id);

    ResponseDTO editUserDetails(UserDTO userDTO);

    DataTableResponseDTO<Object, List<UserDTO>> getListOfUsers(DataTableRequestDTO dataTableRequestDTO);
}
