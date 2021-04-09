package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailDTO {

    Long id;
    String username;
    String name;
    String email;
    String phoneNumber;
    String role;
    String department;

}
