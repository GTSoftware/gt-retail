package ar.com.gtsoftware.api.request.suppliers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record NewSupplierInvoiceRequest(
    @NotNull LocalDateTime invoiceDate,
    String notes,
    @Size(max = 1) @Pattern(regexp = "^[ABCMabcm]+$") String letter,
    Integer pointOfSale,
    Integer invoiceNumber,
    Long invoiceTypeId,
    Long supplierId,
    Long fiscalPeriodId,
    @NotEmpty List<SupplierInvoiceDetail> invoiceDetails,
    BigDecimal grossIncomePerceptionAmount,
    BigDecimal taxPerceptionAmount)
    implements Serializable {}
