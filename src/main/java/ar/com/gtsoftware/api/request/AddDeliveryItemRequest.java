package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddDeliveryItemRequest {
  private final Long productId;
  private final String productCode;
  private final String supplierCode;

  @NotNull
  @DecimalMin("0.01")
  private final BigDecimal quantity;

  private final Long originWarehouseId;
  private final Long destinationWarehouseId;
  private final Long supplierId;
  private final boolean usePurchaseUnits;
}
