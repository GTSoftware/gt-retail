package ar.com.gtsoftware.utils;

import ar.com.gtsoftware.auth.JwtUserDetails;
import ar.com.gtsoftware.auth.Roles;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtils {

    public boolean userHasRole(Roles role) {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (SimpleGrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role.name());
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }

    public JwtUserDetails getUserDetails() {
        return (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
