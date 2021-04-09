package tech.shayannasir.tms.binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.CreatedModifiedUserDTO;
import tech.shayannasir.tms.dto.UserDTO;
import tech.shayannasir.tms.dto.UserDetailDTO;
import tech.shayannasir.tms.entity.Department;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.repository.DepartmentRepository;
import tech.shayannasir.tms.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserDataBinder {

    @Autowired
    private UserRepository userRepository;

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
        userDTO.setDepartment(user.getDepartment().getName());
        userDTO.setTotalTasks(user.getTotalTasks());
        userDTO.setTotalTickets(user.getTotalTickets());
        userDTO.setDueTasks(user.getDueTasks());
        userDTO.setDueTickets(user.getDueTickets());
        return userDTO;
    }

    public UserDetailDTO bindDocumentToDetailDTO(User source) {
        UserDetailDTO target = new UserDetailDTO();

        if (Objects.nonNull(source)) {
            target.setId(source.getId());
            target.setUsername(source.getUsername());
            target.setName(source.getName());
            target.setEmail(source.getEmail());
            target.setPhoneNumber(source.getPhoneNumber());
            target.setRole(source.getRole().name());
            target.setDepartment(source.getDepartment().getName());
        }
        return target;
    }

    public CreatedModifiedUserDTO fetchCreatedAndModifiedUsersFor(Long createdID, Long modifiedID) {
        CreatedModifiedUserDTO createdModifiedUserDTO = new CreatedModifiedUserDTO();
        Optional<User> createdBy = userRepository.findById(createdID);
        createdBy.ifPresent(user -> createdModifiedUserDTO.setCreatedBy(bindDocumentToDetailDTO(Optional.of(user).orElse(null))));
        Optional<User> modifiedBy = userRepository.findById(modifiedID);
        modifiedBy.ifPresent(user -> createdModifiedUserDTO.setModifiedBy(bindDocumentToDetailDTO(Optional.of(user).orElse(null))));
        return createdModifiedUserDTO;
    }

}
