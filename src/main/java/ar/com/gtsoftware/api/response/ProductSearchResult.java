package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductSearchResult {
    private Long productId;
    private String codigoPropio;
    private String descripcion;
    private String observaciones;
    private LocalDateTime fechaUltimaModificacion;
    private String saleUnit;
    private String purchaseUnit;
    private ProductSupplyType supplyType;
    private ProductSubCategory subCategory;
    private ProductCategory category;
    private ProductBrand brand;
    private BigDecimal precioVenta;
    private String codigoFabricante;
    private BigDecimal stockActual;
    private BigDecimal stockActualEnSucursal;
}
