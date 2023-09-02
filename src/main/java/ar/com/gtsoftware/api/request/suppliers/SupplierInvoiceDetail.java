package ar.com.gtsoftware.api.request.suppliers;

import java.io.Serializable;
import java.math.BigDecimal;

public record SupplierInvoiceDetail(
    Long taxRateId, BigDecimal taxAmount, BigDecimal baseAmount, BigDecimal nonTaxableAmount)
    implements Serializable {}
