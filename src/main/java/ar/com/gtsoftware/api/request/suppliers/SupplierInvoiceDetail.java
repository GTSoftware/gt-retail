package ar.com.gtsoftware.api.request.suppliers;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public record SupplierInvoiceDetail(
    @NotNull Long taxRateId,
    @NotNull BigDecimal taxAmount,
    @NotNull BigDecimal baseAmount,
    @NotNull BigDecimal nonTaxableAmount)
    implements Serializable {}
