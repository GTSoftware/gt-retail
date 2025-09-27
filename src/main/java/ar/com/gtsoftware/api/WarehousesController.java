package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.warehouses.CreateOrUpdateWarehouseRequest;
import ar.com.gtsoftware.api.response.Warehouse;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface WarehousesController {

  @GetMapping(path = "/warehouses")
  List<Warehouse> getWarehouses();

  @GetMapping(path = "/warehouses/{warehouseId}")
  Warehouse getWarehouse(@PathVariable Long warehouseId);

  @PostMapping(path = "/warehouses")
  Warehouse createWarehouse(
      @Valid @RequestBody CreateOrUpdateWarehouseRequest createOrUpdateWarehouseRequest);

  @PutMapping(path = "/warehouses/{warehouseId}")
  Warehouse updateWarehouse(
      @PathVariable Long warehouseId,
      @Valid @RequestBody CreateOrUpdateWarehouseRequest createOrUpdateWarehouseRequest);

  @DeleteMapping(path = "/warehouses/{warehouseId}")
  void deleteWarehouse(@PathVariable Long warehouseId);
}
