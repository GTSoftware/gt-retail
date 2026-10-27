package ar.com.gtsoftware.api.response.cashbox;

import java.math.BigDecimal;

public record CashBoxArqueoDetail(
    Long id,
    Long paymentFormId,
    String paymentFormName,
    BigDecimal systemAmount,
    BigDecimal declaredAmount,
    BigDecimal difference,
    String notes) {}
