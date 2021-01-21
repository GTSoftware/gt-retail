package ar.com.gtsoftware.api.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
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
}
