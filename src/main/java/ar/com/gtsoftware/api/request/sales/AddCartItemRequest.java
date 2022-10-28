package ar.com.gtsoftware.api.request.sales;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartItemRequest {
  private Long productId;
  private String productCode;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer = 17, fraction = 2)
  private BigDecimal cantidad;

  @Digits(integer = 15, fraction = 4)
  private BigDecimal precioVentaUnitario;
}
