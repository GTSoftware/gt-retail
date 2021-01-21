package ar.com.gtsoftware.api.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
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
  @NotNull private BigDecimal precioVentaUnitario;

  @NotNull
  @DecimalMin("0.01")
  private BigDecimal cantidad;
}
