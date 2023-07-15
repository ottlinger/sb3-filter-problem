package de.aikiit.prototype.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ApplicationUser extends User {
    private static final long serialVersionUID = 2203934453215335777L;
    private final UserTenant tenant;

    public ApplicationUser(UserTenant tenant, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.tenant = tenant;
    }

    public UserTenant getTenant() {
        return tenant;
    }
}
