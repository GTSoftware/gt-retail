package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductPercent {

  @NotNull private final BigDecimal percentValue;
  @NotNull private final Long percentTypeId;
}
