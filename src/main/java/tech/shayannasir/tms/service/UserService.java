package tech.shayannasir.tms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.shayannasir.tms.dto.LoginResponseDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.UserDTO;

public interface UserService extends UserDetailsService {

    ResponseDTO createUser(UserDTO userDTO);
    String logout (String token);

    ResponseDTO<LoginResponseDTO> login(Authentication authentication);

    ResponseDTO getUserDetails(Long id);

    ResponseDTO editUserDetails(UserDTO userDTO);
}
