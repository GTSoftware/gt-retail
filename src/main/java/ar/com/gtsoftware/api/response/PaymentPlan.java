package ar.com.gtsoftware.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentPlan {

  private Long paymentPlanId;
  private String paymentPlanName;
  private List<PlanDetail> paymentPlanDetails;
}
