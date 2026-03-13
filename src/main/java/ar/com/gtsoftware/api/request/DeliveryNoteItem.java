package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryNoteItem {
  @NotNull private final Long productId;

  @NotNull
  @DecimalMin("0.01")
  private final BigDecimal quantity;
}
