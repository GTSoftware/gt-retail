package ar.com.gtsoftware.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SaleToPay {
    private Long saleId;
    private Long paymentId;
    private String paymentMethodDescription;
    private Long paymentMethodId;
    private BigDecimal totalPayment;
    private BigDecimal minPayment;
    private BigDecimal maxPayment;
    private boolean editableAmount;
}
