package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tech.shayannasir.tms.entity.Attachment;
import tech.shayannasir.tms.entity.Task;
import tech.shayannasir.tms.enums.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseEntityDTO<Long> {

    Long  id;

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
    @NotBlank
    private String name;
    @NotBlank
    private String designation;
    @NotBlank
    private String emailId;
    @NotBlank
    private String empID;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String department;

    private String fileName;

    private Attachment coverPic;

    private Long totalTickets;
    private Long dueTickets;
    private Long totalTasks;
    private Long dueTasks;


    private Date createdDate;
    private Boolean accountExpired;
    private Boolean accountLocked;
    private Boolean credentialsExpired;
    private Boolean accountEnabled;

    private Long lastLoginTimeStamp;


}
