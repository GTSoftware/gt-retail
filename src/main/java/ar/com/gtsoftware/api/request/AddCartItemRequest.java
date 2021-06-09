package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartItemRequest {
  private Long productId;
  private String productCode;

  @NotNull
  @DecimalMin("0.01")
  private BigDecimal cantidad;

  private BigDecimal precioVentaUnitario;
}
