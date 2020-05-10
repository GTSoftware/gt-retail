package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductoMovimiento {
    private ProductosDto producto;
    private RemitoDto remito;
    private BigDecimal cantidad;
    private BigDecimal stockParcial;
}
