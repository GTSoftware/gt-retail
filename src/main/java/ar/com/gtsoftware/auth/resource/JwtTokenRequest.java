package ar.com.gtsoftware.auth.resource;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenRequest implements Serializable {

  private static final long serialVersionUID = -5616176897013108345L;

  @NotNull private String username;
  @NotNull private String password;
}
