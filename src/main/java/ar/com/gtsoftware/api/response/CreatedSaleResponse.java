package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatedSaleResponse {
  private Long saleId;
  private Long deliveryNoteId;
}
