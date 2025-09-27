package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.Warehouse;
import ar.com.gtsoftware.dto.domain.DepositosDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WarehousesTransformer {

  public Warehouse transform(DepositosDto deposito) {
    return Warehouse.builder()
        .warehouseId(deposito.getId())
        .warehouseName(deposito.getNombreDeposito())
        .branchId(deposito.getIdSucursal() != null ? deposito.getIdSucursal().getId() : null)
        .branchName(
            deposito.getIdSucursal() != null ? deposito.getIdSucursal().getNombreSucursal() : null)
        .address(deposito.getDireccion())
        .active(deposito.isActivo())
        .countryId(deposito.getIdPais() != null ? deposito.getIdPais().getId() : null)
        .provinceId(deposito.getIdProvincia() != null ? deposito.getIdProvincia().getId() : null)
        .localityId(deposito.getIdLocalidad() != null ? deposito.getIdLocalidad().getId() : null)
        .displayName(
            deposito.getIdSucursal() != null
                ? "%s - %s (%d)"
                    .formatted(
                        deposito.getNombreDeposito(),
                        deposito.getIdSucursal().getNombreSucursal(),
                        deposito.getIdSucursal().getId())
                : deposito.getNombreDeposito())
        .build();
  }

  public List<Warehouse> transform(List<DepositosDto> depositos) {
    List<Warehouse> result = new ArrayList<>(depositos.size());
    for (DepositosDto d : depositos) {
      result.add(transform(d));
    }
    return result;
  }
}
