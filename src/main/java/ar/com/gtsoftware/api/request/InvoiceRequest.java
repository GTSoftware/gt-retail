package ar.com.gtsoftware.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class InvoiceRequest {

    @NotNull
    private Long saleId;
    @NotNull
    private Integer pointOfSale;
    @Past
    private LocalDateTime invoiceDate;

    private Long invoiceNumber;
}
