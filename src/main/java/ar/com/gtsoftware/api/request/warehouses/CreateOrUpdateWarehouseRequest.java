package ar.com.gtsoftware.api.request.warehouses;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateWarehouseRequest {

  @NotBlank private String warehouseName;

  private String address;

  @NotNull private Boolean active;

  @NotNull private Long branchId;

  @NotNull private Long countryId;

  @NotNull private Long provinceId;

  @NotNull private Long localityId;
}
