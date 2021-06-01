package ar.com.gtsoftware.api.request.products;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PricePercent {
  private Long productPricePercentId;
  @NotNull private BigDecimal rate;
  @NotNull private Long percentTypeId;
  private Integer version;
}
