package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleTotalsResponse {

  private BigDecimal invoicedTotal;
  private BigDecimal notInvoicedTotal;
}
