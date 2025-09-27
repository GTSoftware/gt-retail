package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InvoiceRequest {

  @NotNull private Long saleId;
  @NotNull private Integer pointOfSale;
  @Past private LocalDateTime invoiceDate;

  private Long invoiceNumber;

  @NotNull private String invoiceRequestId;
}
