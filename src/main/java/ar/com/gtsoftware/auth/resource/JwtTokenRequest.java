package ar.com.gtsoftware.auth.resource;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -5616176897013108345L;

  @NotNull private String username;
  @NotNull private String password;
}
