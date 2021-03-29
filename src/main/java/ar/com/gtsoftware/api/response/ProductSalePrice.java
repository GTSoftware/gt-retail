package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductSalePrice {
  private Long salePriceId;
  private PriceList priceList;
  private BigDecimal utility;
  private BigDecimal finalPrice;
  private BigDecimal netPrice;
  private LocalDateTime lastUpdatedDate;
  private Integer version;
}
