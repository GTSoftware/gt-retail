package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.DeliveryNotesController;
import ar.com.gtsoftware.api.exception.DeliveryNoteValidationException;
import ar.com.gtsoftware.api.exception.ProductNotFoundException;
import ar.com.gtsoftware.api.request.AddDeliveryItemRequest;
import ar.com.gtsoftware.api.request.AddDeliveryNoteRequest;
import ar.com.gtsoftware.api.response.DeliveryItemResponse;
import ar.com.gtsoftware.api.response.Warehouse;
import ar.com.gtsoftware.api.transformer.toDomain.RemitoDtoTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.DepositosDto;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.dto.domain.RemitoTipoMovimientoDto;
import ar.com.gtsoftware.search.DepositosSearchFilter;
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class DeliveryNotesControllerImpl implements DeliveryNotesController {

    private final RemitoTipoMovimientoService tipoMovimientoService;
    private final DepositosService depositosService;
    private final ProductosService productosService;
    private final ProductoXDepositoService productoXDepositoService;
    private final RemitoService remitoService;
    private final RemitoDtoTransformer remitoDtoTransformer;
    private final SecurityUtils securityUtils;

    @Override
    public List<Warehouse> getWarehouses() {
        Long branchId = null;
        if (!securityUtils.userHasRole(Roles.ADMINISTRADORES)
                && securityUtils.userHasRole(Roles.STOCK_MEN)) {
            branchId = securityUtils.getUserDetails().getSucursalId();
        }
        final DepositosSearchFilter sf = DepositosSearchFilter.builder()
                .activo(true)
                .idSucursal(branchId)
                .build();
        sf.addSortField("idSucursal.id", true);
        sf.addSortField("nombreDeposito", true);
        final List<DepositosDto> depositosDto = depositosService.findAllBySearchFilter(sf);

        return transformWarehouses(depositosDto);
    }

    //TODO move to proper transformer
    private List<Warehouse> transformWarehouses(List<DepositosDto> depositosDto) {
        List<Warehouse> warehouses = new ArrayList<>(depositosDto.size());
        for (DepositosDto deposito : depositosDto) {
            warehouses.add(
                    Warehouse.builder()
                            .branchId(deposito.getIdSucursal().getId())
                            .branchName(deposito.getIdSucursal().getNombreSucursal())
                            .warehouseId(deposito.getId())
                            .warehouseName(deposito.getNombreDeposito())
                            .displayName(String.format("%s - %s (%d)", deposito.getNombreDeposito(),
                                    deposito.getIdSucursal().getNombreSucursal(),
                                    deposito.getIdSucursal().getId()))
                            .build()
            );
        }

        return warehouses;
    }

    @Override
    public List<RemitoTipoMovimientoDto> getDeliveryTypes() {
        return tipoMovimientoService.findAll();
    }

    @Override
    public DeliveryItemResponse addProduct(@Valid AddDeliveryItemRequest addDeliveryItemRequest) {

        ProductosSearchFilter psf = ProductosSearchFilter.builder()
                .activo(true)
                .llevaControlStock(true)
                .codigoPropio(addDeliveryItemRequest.getProductCode())
                .idProducto(addDeliveryItemRequest.getProductId())
                .codigoFabrica(addDeliveryItemRequest.getSupplierCode())
                .build();

        ProductosDto productosDto = productosService.findFirstBySearchFilter(psf);

        if (productosDto != null) {
            DeliveryItemResponse response = DeliveryItemResponse.builder()
                    .description(productosDto.getDescripcion())
                    .productCode(productosDto.getCodigoPropio())
                    .productId(productosDto.getId())
                    .supplierCode(productosDto.getCodigoFabricante())
                    .purchaseUnits(productosDto.getIdTipoUnidadCompra().getNombreUnidad())
                    .saleUnits(productosDto.getIdTipoUnidadVenta().getNombreUnidad())
                    .quantity(fixQuantity(productosDto, addDeliveryItemRequest))
                    .build();

            addStock(response, addDeliveryItemRequest);

            return response;
        }

        throw new ProductNotFoundException();
    }

    @Override
    public Long saveDeliveryNote(@Valid AddDeliveryNoteRequest addDeliveryNoteRequest) {
        RemitoDto remitoDto = remitoDtoTransformer.transformAddDeliveryNote(addDeliveryNoteRequest);

        if (!remitoDto.getIsDestinoInterno() && !remitoDto.getIsOrigenInterno()) {
            throw new DeliveryNoteValidationException("No se puede realizar un remito externo-externo");
        }

        if (Objects.equals(remitoDto.getIdDestinoPrevistoInterno(), remitoDto.getIdOrigenInterno())) {
            throw new DeliveryNoteValidationException("No se puede realizar entre el mismo depósito");
        }

        return remitoService.guardarRemito(remitoDto);
    }

    private void addStock(DeliveryItemResponse response, AddDeliveryItemRequest addDeliveryItemRequest) {
        ProductoXDepositoSearchFilter totalStockSF = ProductoXDepositoSearchFilter.builder()
                .idProducto(response.getProductId())
                .build();
        response.setTotalStock(productoXDepositoService.getStockBySearchFilter(totalStockSF));

        if (addDeliveryItemRequest.getOriginWarehouseId() != null) {
            ProductoXDepositoSearchFilter sf = ProductoXDepositoSearchFilter.builder()
                    .idDeposito(addDeliveryItemRequest.getOriginWarehouseId())
                    .idProducto(response.getProductId())
                    .build();
            response.setOriginWarehouseStock(productoXDepositoService.getStockBySearchFilter(sf));
            response.setOriginWarehouseNewStock(response.getOriginWarehouseStock().subtract(response.getQuantity()));
        }

        if (addDeliveryItemRequest.getDestinationWarehouseId() != null) {
            ProductoXDepositoSearchFilter sf = ProductoXDepositoSearchFilter.builder()
                    .idDeposito(addDeliveryItemRequest.getDestinationWarehouseId())
                    .idProducto(response.getProductId())
                    .build();
            response.setDestinationWarehouseStock(productoXDepositoService.getStockBySearchFilter(sf));
            response.setDestinationWarehouseNewStock(response.getDestinationWarehouseStock().add(response.getQuantity()));
        }

    }

    private BigDecimal fixQuantity(ProductosDto productosDto, AddDeliveryItemRequest addDeliveryItemRequest) {
        BigDecimal resultQuantity = BigDecimal.ONE;
        BigDecimal requestQuantity = addDeliveryItemRequest.getQuantity().setScale(2, RoundingMode.HALF_UP);

        if (addDeliveryItemRequest.isUsePurchaseUnits()) {
            requestQuantity = requestQuantity.multiply(productosDto.getUnidadesCompraUnidadesVenta());
        }

        if (productosDto.getIdTipoUnidadVenta().isCantidadEntera()) {
            BigDecimal integerPart = requestQuantity.setScale(0, RoundingMode.DOWN);
            if (integerPart.signum() != 0) {
                resultQuantity = integerPart;
            }
        } else {
            resultQuantity = requestQuantity;
        }

        return resultQuantity;
    }
}
