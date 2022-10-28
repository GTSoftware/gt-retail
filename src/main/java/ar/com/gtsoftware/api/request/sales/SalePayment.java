package ar.com.gtsoftware.api.request.sales;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SalePayment {
  @NotNull private Long paymentMethodId;
  @NotNull private BigDecimal paymentAmount;
  private Long paymentPlanId;
  private Long paymentPlanDetailId;
}
