package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PrepareToPayResponse {

    private List<SaleToPay> salesToPay;
    private String customer;
    private List<Bank> banks;
    private List<PaymentMethod> noExtraCostPaymentMethods;
}
