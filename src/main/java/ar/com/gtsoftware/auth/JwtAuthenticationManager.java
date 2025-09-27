package ar.com.gtsoftware.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // The JWT filter (JwtTokenAuthorizationOncePerRequestFilter) performs the actual
    // authentication and sets the SecurityContext accordingly. This manager acts as a
    // pass-through to satisfy the AuthenticationManager requirement without altering
    // the Authentication built by the filter.
    return authentication;
  }
}
