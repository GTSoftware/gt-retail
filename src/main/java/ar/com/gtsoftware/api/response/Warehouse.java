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
  private String displayName;
}
