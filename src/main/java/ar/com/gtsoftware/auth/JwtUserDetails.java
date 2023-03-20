package ar.com.gtsoftware.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JwtUserDetails implements UserDetails {

  private static final long serialVersionUID = 5116803780192438148L;

  private Long id;
  private String loginName;
  private String password;
  private String completeUserName;
  private Long sucursalId;
  private String sucursalName;
  private List<String> userRoles;

  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return loginName;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (isEnabled()) {
      return userRoles.stream().map(SimpleGrantedAuthority::new).toList();
    }
    return List.of();
  }

  @Override
  public boolean isEnabled() {
    return CollectionUtils.isNotEmpty(userRoles);
  }
}
