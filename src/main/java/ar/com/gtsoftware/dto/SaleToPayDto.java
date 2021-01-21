package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.ComprobantesPagosDto;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaleToPayDto {
  private ComprobantesDto sale;
  private ComprobantesPagosDto payment;
  private BigDecimal totalPayment;
  private BigDecimal minAllowedPayment;
  private BigDecimal maxAllowedPayment;
  private boolean editableAmount;
}
