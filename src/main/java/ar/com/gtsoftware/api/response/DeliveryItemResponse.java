package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DeliveryItemResponse {
  private Long productId;
  private String productCode;
  private String supplierCode;
  private String description;
  private BigDecimal totalStock;
  private BigDecimal originWarehouseStock;
  private BigDecimal originWarehouseNewStock;
  private BigDecimal destinationWarehouseStock;
  private BigDecimal destinationWarehouseNewStock;
  private BigDecimal quantity;
  private String purchaseUnits;
  private String saleUnits;
}
