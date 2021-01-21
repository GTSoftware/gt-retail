package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductBrand {
  private Long brandId;
  private String brandName;
  private String displayName;
}
