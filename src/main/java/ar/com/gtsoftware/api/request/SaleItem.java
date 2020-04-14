package ar.com.gtsoftware.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SaleItem {
    @NotNull
    private Long id;
    @NotNull
    private String codigoPropio;
    @NotNull
    private String descripcion;
    @NotNull
    private BigDecimal precioVentaUnitario;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal cantidad;
}
