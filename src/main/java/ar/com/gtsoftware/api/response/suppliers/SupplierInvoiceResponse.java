package ar.com.gtsoftware.api.response.suppliers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SupplierInvoiceResponse(
    Long invoiceId,
    String supplier,
    String invoiceType,
    LocalDateTime invoiceDate,
    String number,
    BigDecimal total) {}
