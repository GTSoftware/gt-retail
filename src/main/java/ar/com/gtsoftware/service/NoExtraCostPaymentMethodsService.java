package ar.com.gtsoftware.service;

import ar.com.gtsoftware.api.response.PaymentMethod;
import java.util.List;

public interface NoExtraCostPaymentMethodsService {

  List<PaymentMethod> getNoExtraCostPaymentMethods();
}
