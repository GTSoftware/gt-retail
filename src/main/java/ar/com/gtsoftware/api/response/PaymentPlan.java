package ar.com.gtsoftware.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PaymentPlan {

    private Long paymentPlanId;
    private String paymentPlanName;
    private List<PlanDetail> paymentPlanDetails;
}
