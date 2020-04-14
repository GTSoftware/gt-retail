package ar.com.gtsoftware.api.response;

import ar.com.gtsoftware.api.PromotionCartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class CartItemResponse {
    private Long id;
    private String codigoPropio;
    private String descripcion;
    private BigDecimal precioVentaUnitario;
    private BigDecimal stockActual;
    private BigDecimal stockActualEnSucursal;
    private BigDecimal cantidad;
    private BigDecimal subTotal;
    private boolean cantidadEntera;
    private String unidadVenta;
    private DiscountItem discountItem;
}
