package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaleDetail {

  private String productCode;
  private String description;
  private String saleUnit;
  private BigDecimal quantity;
  private BigDecimal subTotal;
}
