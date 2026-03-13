package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesToPayRequest {

  @NotNull @NotEmpty private List<Long> salesIds;
}
