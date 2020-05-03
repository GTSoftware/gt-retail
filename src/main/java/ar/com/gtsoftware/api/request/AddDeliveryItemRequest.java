package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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
    private final boolean usePurchaseUnits;
}
