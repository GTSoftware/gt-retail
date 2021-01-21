package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductMovement {
  private String productCode;
  private String supplierCode;
  private String productDescription;
  private BigDecimal partialStock;
  private BigDecimal movementQuantity;
  private String saleUnit;
  private String movementType;
  private String deliveryNoteObservation;
  private String deliveryNoteOrigin;
  private String deliveryNoteDestination;
  private LocalDateTime movementDate;
  private Long deliveryNoteId;
}
