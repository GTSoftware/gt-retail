package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Warehouse {

  private Long warehouseId;
  private String warehouseName;
  private Long branchId;
  private String branchName;
  private String address;
  private Boolean active;
  private Long countryId;
  private Long provinceId;
  private Long localityId;
  private String displayName;
}
