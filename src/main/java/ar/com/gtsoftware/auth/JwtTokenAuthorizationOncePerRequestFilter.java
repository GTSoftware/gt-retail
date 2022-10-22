package ar.com.gtsoftware.auth;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

  private final UserDetailsService databaseUserDetailsService;

  private final JwtTokenUtil jwtTokenUtil;

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  private final CacheManager cacheManager;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    log.debug("Authentication Request For '{}'", request.getRequestURL());

    final String requestTokenHeader = request.getHeader(tokenHeader);

    if (isBearerTokenPresent(requestTokenHeader)) {
      String jwtToken = requestTokenHeader.substring(7);
      if (SecurityContextHolder.getContext().getAuthentication() == null) {

        final UserDetails cacheSession = getCacheSession(jwtToken);

        if (Objects.nonNull(cacheSession)) {
          setUserSessionInContext(request, cacheSession);
        } else {
          loadSessionFromDatabase(jwtToken, request);
        }
      }

    } else {
      log.debug("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
    }

    chain.doFilter(request, response);
  }

  private UserDetails getCacheSession(String jwtToken) {
    return getCache().get(jwtToken, UserDetails.class);
  }

  private void invalidateToken(String jwtToken) {
    getCache().evict(jwtToken);
  }

  private Cache getCache() {
    return Objects.requireNonNull(
        cacheManager.getCache("sessions"), "Sessions cache should not be null");
  }

  private void loadSessionFromDatabase(String jwtToken, HttpServletRequest request) {
    log.debug("Loading session from Database");

    if (jwtTokenUtil.isValidToken(jwtToken)) {
      String username = getUsernameFromJWT(jwtToken);
      log.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);

      UserDetails userDetails = databaseUserDetailsService.loadUserByUsername(username);
      setUserSessionInContext(request, userDetails);

      getCache().put(jwtToken, userDetails);
    } else {
      log.debug("JWT_TOKEN_NOT_VALID '{}'", jwtToken);
      invalidateToken(jwtToken);
    }
  }

  private void setUserSessionInContext(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    usernamePasswordAuthenticationToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }

  private String getUsernameFromJWT(String jwtToken) {
    try {
      return jwtTokenUtil.getUsernameFromToken(jwtToken);
    } catch (IllegalArgumentException e) {
      log.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
      invalidateToken(jwtToken);
    } catch (ExpiredJwtException e) {
      log.warn("JWT_TOKEN_EXPIRED", e);
      invalidateToken(jwtToken);
    } catch (Exception ex) {
      log.error("Illegal JWT reason: {} token: {}", ex.getMessage(), jwtToken);
      invalidateToken(jwtToken);
    }

    return null;
  }

  private boolean isBearerTokenPresent(String requestTokenHeader) {
    return StringUtils.startsWith(requestTokenHeader, "Bearer ");
  }
}
