package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaleToPay {
  private Long saleId;
  private Long paymentId;
  private String paymentMethodDescription;
  private String paymentPlan;
  private Long paymentMethodId;
  private BigDecimal totalPayment;
  private BigDecimal minPayment;
  private BigDecimal maxPayment;
  private boolean editableAmount;
}
