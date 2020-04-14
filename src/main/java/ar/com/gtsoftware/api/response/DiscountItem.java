package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DiscountItem {
    private Long id;
    private String codigoPropio;
    private String descripcion;
    private String unidadVenta;
    private BigDecimal precioVentaUnitario;
    private BigDecimal cantidad;
    private BigDecimal subTotal;
}
