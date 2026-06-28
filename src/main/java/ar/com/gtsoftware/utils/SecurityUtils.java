package ar.com.gtsoftware.utils;

import ar.com.gtsoftware.auth.JwtUserDetails;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.service.UsuariosService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityUtils {

  private final UsuariosService usuariosService;

  public boolean userHasRole(Roles role) {
    Collection<SimpleGrantedAuthority> authorities =
        (Collection<SimpleGrantedAuthority>)
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

  public void checkUserRole(Roles... roleList) {
    for (Roles role : roleList) {
      if (!userHasRole(role)) {
        log.error("Unauthorized access to a resource. Required role: {} is required", role);

        throw new AccessDeniedException("User does not have access to this resource");
      }
    }
  }

  public void checkUserHasAnyRole(Roles... roleList) {
    boolean hasAnyRole = false;
    for (Roles role : roleList) {
      if (userHasRole(role)) {
        hasAnyRole = true;
        break;
      }
    }
    if (!hasAnyRole) {
      log.error("Unauthorized access to a resource. User does not have any of the given roles");

      throw new AccessDeniedException("User does not have access to this resource");
    }
  }

  public JwtUserDetails getUserDetails() {
    return (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public UsuariosDto getCurrentUser() {
    return usuariosService.find(getUserDetails().getId());
  }
}
