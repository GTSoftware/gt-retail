package ar.com.gtsoftware.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtUnAuthorizedResponseAuthenticationEntryPoint
    implements AuthenticationEntryPoint, Serializable {

  @Serial private static final long serialVersionUID = -8970718410437077606L;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException)
      throws IOException, ServletException {
    response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED,
        "You need to provide the Jwt Token to Access this resource");
  }
}
