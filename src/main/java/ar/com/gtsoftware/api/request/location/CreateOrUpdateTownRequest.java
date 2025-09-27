package ar.com.gtsoftware.api.request.location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateOrUpdateTownRequest {
  @NotEmpty
  @Size(min = 1, max = 100)
  private String name;

  @NotEmpty
  @Size(min = 1, max = 20)
  private String postalCode;

  @NotNull private Long provinceId;

  private Integer version;
}
