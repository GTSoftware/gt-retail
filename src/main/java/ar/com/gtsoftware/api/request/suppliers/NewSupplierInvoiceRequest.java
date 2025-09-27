package ar.com.gtsoftware.api.request.suppliers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewSupplierInvoiceRequest(
    @NotNull LocalDateTime invoiceDate,
    @Size(max = 1024) String notes,
    @NotNull @NotEmpty @Size(max = 1) @Pattern(regexp = "^[ABCMabcm]+$") String letter,
    @NotNull Integer pointOfSale,
    @NotNull Integer invoiceNumber,
    @NotNull Long invoiceTypeId,
    @NotNull Long supplierId,
    @NotNull Long fiscalPeriodId,
    @NotEmpty List<SupplierInvoiceDetail> invoiceDetails,
    @NotNull BigDecimal grossIncomePerceptionAmount,
    @NotNull BigDecimal taxPerceptionAmount)
    implements Serializable {}
