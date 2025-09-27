package ar.com.gtsoftware.api.request.warehouses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
