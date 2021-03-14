package tech.shayannasir.tms.binder;

import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.UserDTO;
import tech.shayannasir.tms.entity.User;

@Component
public class UserDataBinder {

    public UserDTO bindToUserSummaryDTO(User user) {

        UserDTO userDTO = UserDTO.builder().build();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setEmailId(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setAccountEnabled(user.getAccountEnabled());
        if (user.getLastLoginTime() != null) {
            userDTO.setLastLoginTimeStamp(user.getLastLoginTime().getTime());
        }
        return userDTO;
    }

}
