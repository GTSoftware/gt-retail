package ar.com.gtsoftware.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentMethod {

  private Long paymentMethodId;
  private String paymentMethodName;
  private String paymentMethodShortName;
  private List<PaymentPlan> paymentPlans;
}
