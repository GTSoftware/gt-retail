package ar.com.gtsoftware.api.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesToPayRequest {

  @NotNull @NotEmpty private List<Long> salesIds;
}
