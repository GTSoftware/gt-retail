package ar.com.gtsoftware.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SalePayment {
    @NotNull
    private Long paymentMethodId;
    @NotNull
    private BigDecimal paymentAmount;
    private Long paymentPlanId;
    private Long paymentPlanDetailId;

}
