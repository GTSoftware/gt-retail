package ar.com.gtsoftware.api.response;

import ar.com.gtsoftware.dto.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
public class ProductSearchResult {
    private Long id;
    private String codigoPropio;
    private String descripcion;
    private String observaciones;
    private Date fechaUltimaModificacion;
    private ProductosTiposUnidadesDto tipoUnidadVenta;
    private ProductosTiposUnidadesDto tipoUnidadCompra;
    private ProductosTiposProveeduriaDto tipoProveeduria;
    private ProductosSubRubrosDto subRubro;
    private ProductosRubrosDto rubro;
    private ProductosMarcasDto marca;
    private BigDecimal precioVenta;
    private String codigoFabricante;
    private BigDecimal stockActual;
    private BigDecimal stockActualEnSucursal;
}
