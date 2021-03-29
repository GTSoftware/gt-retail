package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductUnitType {
  private Long unitTypeId;
  private String unitName;
}
