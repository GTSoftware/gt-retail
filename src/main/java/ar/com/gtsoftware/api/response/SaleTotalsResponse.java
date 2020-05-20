package ar.com.gtsoftware.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SaleTotalsResponse {

    private BigDecimal invoicedTotal;
    private BigDecimal notInvoicedTotal;

}
