package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductMovementRequest {

  @NotNull @Past private LocalDateTime fromDate;
  @NotNull private Long productId;
  @NotNull private Long warehouseId;
}
