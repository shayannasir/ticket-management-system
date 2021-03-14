package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.shayannasir.tms.enums.Role;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;

    String username;

    String password;

    String name;

    String email;

    String phoneNumber;

    Role role;

    Boolean accountEnabled;

    Boolean accountExpired = false;

    Boolean accountLocked = false;

    Boolean credentialsExpired = false;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastLoginTime;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role != null)
            return Arrays.asList(this.role);
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return !credentialsExpired;
    }
}
