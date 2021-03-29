package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PriceList {
  private Long priceListId;
  private String priceListName;
  private String displayName;
}
