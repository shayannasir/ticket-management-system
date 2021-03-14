package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tech.shayannasir.tms.enums.Role;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseEntityDTO<Long> {

    Long  id;

    private String emailId;

    private String username;

    private String password;

    private Date createdDate;

    private String name;

    private String phoneNumber;

    private Boolean accountExpired;

    private Boolean accountLocked;

    private Boolean credentialsExpired;

    private Boolean accountEnabled;

    private Role role;

    private Long lastLoginTimeStamp;

}
