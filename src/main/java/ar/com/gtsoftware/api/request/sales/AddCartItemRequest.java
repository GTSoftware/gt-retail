package ar.com.gtsoftware.api.request.sales;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
