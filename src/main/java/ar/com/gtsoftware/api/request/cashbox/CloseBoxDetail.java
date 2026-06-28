package ar.com.gtsoftware.api.request.cashbox;

import java.math.BigDecimal;

public record CloseBoxDetail(
    Long paymentFormId, BigDecimal currentAmount, BigDecimal declaredAmount, String notes) {}
