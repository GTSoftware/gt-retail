package ar.com.gtsoftware.auth.resource;

import ar.com.gtsoftware.auth.JwtTokenUtil;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationRestController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService userDetailsService;

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  private final CacheManager cacheManager;

  @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
  public ResponseEntity<JwtTokenResponse> createAuthenticationToken(
      @RequestBody @Valid JwtTokenRequest authenticationRequest) {

    final String username = authenticationRequest.getUsername();
    log.info("Trying to authenticate username: {}", username);

    authenticate(username, authenticationRequest.getPassword());

    log.info("User {} authentication successful", username);

    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    final String token = jwtTokenUtil.generateToken(userDetails);

    getCache().put(token, userDetails);

    return ResponseEntity.ok(new JwtTokenResponse(token));
  }

  @RequestMapping(value = "/logoff", method = RequestMethod.POST)
  public void logoff(HttpServletRequest request) {
    String authToken = request.getHeader(tokenHeader);
    final String token = authToken.substring(7);

    getCache().evict(token);
  }

  private void authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new AuthenticationException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("INVALID_CREDENTIALS", e);
    }
  }

  private Cache getCache() {
    return Objects.requireNonNull(
        cacheManager.getCache("sessions"), "Sessions cache should not be null");
  }
}
