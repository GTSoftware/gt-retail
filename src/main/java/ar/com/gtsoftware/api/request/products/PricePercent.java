package ar.com.gtsoftware.api.request.products;

import java.math.BigDecimal;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PricePercent {
  private Long productPricePercentId;
  @NotNull private Long percentTypeId;

  @Digits(integer = 15, fraction = 4)
  @NotNull
  private BigDecimal rate;

  private Integer version;
}
