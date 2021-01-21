package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductSupplyType {
  private Long supplyTypeId;
  private String supplyTypeName;
  private String displayName;
}
