package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.StockController;
import ar.com.gtsoftware.api.request.ProductMovementRequest;
import ar.com.gtsoftware.api.response.ProductMovement;
import ar.com.gtsoftware.dto.ProductoMovimiento;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.search.RemitoDetalleSearchFilter;
import ar.com.gtsoftware.service.RemitoService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockControllerImpl implements StockController {

    private final RemitoService remitoService;
    private final BusinessDateUtils dateUtils;

    @Override
    public List<ProductMovement> getProductMovements(@Valid ProductMovementRequest request) {
        RemitoDetalleSearchFilter detalleSearchFilter = RemitoDetalleSearchFilter.builder()
                .idDepositoMovimiento(request.getWarehouseId())
                .idProducto(request.getProductId())
                .fechaRemitoDesde(request.getFromDate())
                .fechaRemitoHasta(dateUtils.getCurrentDateTime())
                .build();
        final List<ProductoMovimiento> movimientosProducto = remitoService.getMovimientosProducto(detalleSearchFilter);

        return transformMovements(movimientosProducto);
    }

    private List<ProductMovement> transformMovements(List<ProductoMovimiento> movimientosProducto) {
        List<ProductMovement> productMovements = new ArrayList<>(movimientosProducto.size());
        for (ProductoMovimiento pMov : movimientosProducto) {
            productMovements.add(
                    ProductMovement.builder()
                            .deliveryNoteObservation(pMov.getRemito().getObservaciones())
                            .movementDate(pMov.getRemito().getFechaAlta())
                            .movementQuantity(pMov.getCantidad())
                            .partialStock(pMov.getStockParcial())
                            .productCode(pMov.getProducto().getCodigoPropio())
                            .productDescription(pMov.getProducto().getDescripcion())
                            .saleUnit(pMov.getProducto().getIdTipoUnidadVenta().getNombreUnidad())
                            .supplierCode(pMov.getProducto().getCodigoFabricante())
                            .movementType(pMov.getRemito().getRemitoTipoMovimiento().getNombreTipo())
                            .deliveryNoteOrigin(transformOrigin(pMov.getRemito()))
                            .deliveryNoteDestination(transformDestination(pMov.getRemito()))
                            .deliveryNoteId(pMov.getRemito().getId())
                            .build()
            );
        }

        return productMovements;
    }

    private String transformDestination(RemitoDto remito) {
        if (remito.getIsDestinoInterno()) {
            return remito.getIdDestinoPrevistoInterno().getNombreDeposito();
        } else {
            return remito.getIdDestinoPrevistoExterno().getRazonSocial();
        }
    }

    private String transformOrigin(RemitoDto remito) {
        if (remito.getIsOrigenInterno()) {
            return remito.getIdOrigenInterno().getNombreDeposito();
        } else {
            return remito.getIdOrigenExterno().getRazonSocial();
        }
    }
}
