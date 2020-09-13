package ar.com.gtsoftware.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PaymentMethod {

    private Long paymentMethodId;
    private String paymentMethodName;
    private String paymentMethodShortName;
    private List<PaymentPlan> paymentPlans;
}
