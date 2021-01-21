package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryNoteItem {
  @NotNull private final Long productId;

  @NotNull
  @DecimalMin("0.01")
  private final BigDecimal quantity;

  private final int itemNumber;
}
