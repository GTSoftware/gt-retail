package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductPricePercent {

  private Long productPricePercentId;
  private LocalDateTime lastUpdatedDate;
  private BigDecimal rate;
  private ProductPercentType percentType;
  private Integer version;
}
