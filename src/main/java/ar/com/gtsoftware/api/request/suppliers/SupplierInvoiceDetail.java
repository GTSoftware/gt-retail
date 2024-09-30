package ar.com.gtsoftware.api.request.suppliers;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public record SupplierInvoiceDetail(
    @NotNull Long taxRateId,
    @NotNull BigDecimal taxAmount,
    @NotNull BigDecimal baseAmount,
    @NotNull BigDecimal nonTaxableAmount)
    implements Serializable {}
