package ar.com.gtsoftware.api.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Getter;

@Getter
public class ProductMovementRequest {

  @NotNull @Past private LocalDateTime fromDate;
  @NotNull private Long productId;
  @NotNull private Long warehouseId;
}
