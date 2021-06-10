package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaleItem {
  @NotNull private Long id;
  @NotNull private String codigoPropio;
  @NotNull private String descripcion;

  @Digits(integer = 15, fraction = 4)
  @NotNull
  private BigDecimal precioVentaUnitario;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer = 17, fraction = 2)
  private BigDecimal cantidad;
}
