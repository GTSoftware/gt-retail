package ar.com.gtsoftware.api.request.products;

import java.math.BigDecimal;
import javax.validation.constraints.Digits;
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

  @Digits(integer = 4, fraction = 4)
  @NotNull
  private BigDecimal utility;

  @Digits(integer = 15, fraction = 4)
  @NotNull
  private BigDecimal netPrice;

  @Digits(integer = 15, fraction = 4)
  @NotNull
  private BigDecimal finalPrice;

  private Integer version;
}
