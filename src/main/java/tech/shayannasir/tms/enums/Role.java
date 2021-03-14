package tech.shayannasir.tms.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    SUPER_ADMIN;

    @Override
    public String getAuthority() {
        return null;
    }
}
