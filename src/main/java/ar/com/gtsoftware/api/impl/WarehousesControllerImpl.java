package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.WarehousesController;
import ar.com.gtsoftware.api.exception.WarehouseNotFoundException;
import ar.com.gtsoftware.api.request.warehouses.CreateOrUpdateWarehouseRequest;
import ar.com.gtsoftware.api.response.Warehouse;
import ar.com.gtsoftware.api.transformer.fromDomain.WarehousesTransformer;
import ar.com.gtsoftware.api.transformer.toDomain.WarehouseDtoTransformer;
import ar.com.gtsoftware.dto.domain.DepositosDto;
import ar.com.gtsoftware.search.DepositosSearchFilter;
import ar.com.gtsoftware.service.DepositosService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WarehousesControllerImpl implements WarehousesController {

  private final DepositosService depositosService;
  private final WarehousesTransformer warehousesTransformer;
  private final WarehouseDtoTransformer warehouseDtoTransformer;

  @Override
  @Transactional(readOnly = true)
  public List<Warehouse> getWarehouses() {
    final DepositosSearchFilter sf = new DepositosSearchFilter();
    sf.addSortField("idSucursal.id", true);
    sf.addSortField("nombreDeposito", true);

    return warehousesTransformer.transform(depositosService.findAllBySearchFilter(sf));
  }

  @Override
  public Warehouse getWarehouse(Long warehouseId) {
    final DepositosDto dto = depositosService.find(warehouseId);
    if (dto == null) {
      throw new WarehouseNotFoundException();
    }
    return warehousesTransformer.transform(dto);
  }

  @Override
  @Transactional
  public Warehouse createWarehouse(CreateOrUpdateWarehouseRequest createOrUpdateWarehouseRequest) {
    DepositosDto dto = warehouseDtoTransformer.transformNew(createOrUpdateWarehouseRequest);
    dto = depositosService.createOrEdit(dto);
    return warehousesTransformer.transform(dto);
  }

  @Override
  @Transactional
  public Warehouse updateWarehouse(
      Long warehouseId, CreateOrUpdateWarehouseRequest createOrUpdateWarehouseRequest) {
    DepositosDto existing = depositosService.find(warehouseId);
    if (existing == null) {
      throw new WarehouseNotFoundException();
    }
    DepositosDto dto =
        warehouseDtoTransformer.transformFromExisting(createOrUpdateWarehouseRequest, existing);
    dto = depositosService.createOrEdit(dto);
    return warehousesTransformer.transform(dto);
  }

  @Override
  @Transactional
  public void deleteWarehouse(Long warehouseId) {
    DepositosDto existing = depositosService.find(warehouseId);
    if (existing == null) {
      throw new WarehouseNotFoundException();
    }
    depositosService.remove(existing);
  }
}
