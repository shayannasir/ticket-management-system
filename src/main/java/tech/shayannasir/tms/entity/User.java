package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.shayannasir.tms.enums.Role;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AuditEntity implements UserDetails {

    String username;
    String password;
    Role role;

    String name;
    String designation;
    String email;
    String empID;
    String phoneNumber;

    @OneToOne
    Department department;

    Long totalTickets;
    Long dueTickets;

    Long totalTasks;
    Long dueTasks;

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
        return accountEnabled;
    }
}
