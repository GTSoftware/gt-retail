package ar.com.gtsoftware.api.request.products;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SalePrice {
  private Long salePriceId;
  @NotNull private Long priceListId;
  @NotNull private BigDecimal utility;
  @NotNull private BigDecimal netPrice;
  @NotNull private BigDecimal finalPrice;
  private Integer version;
}
