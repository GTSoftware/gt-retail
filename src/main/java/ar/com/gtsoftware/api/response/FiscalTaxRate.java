package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FiscalTaxRate {
  private Long taxRateId;
  private BigDecimal rate;
  private String displayName;
  private String taxRateName;
}
