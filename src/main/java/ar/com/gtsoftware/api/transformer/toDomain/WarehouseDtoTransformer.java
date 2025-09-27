package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.warehouses.CreateOrUpdateWarehouseRequest;
import ar.com.gtsoftware.dto.domain.DepositosDto;
import ar.com.gtsoftware.dto.domain.SucursalesDto;
import ar.com.gtsoftware.dto.domain.UbicacionLocalidadesDto;
import ar.com.gtsoftware.dto.domain.UbicacionPaisesDto;
import ar.com.gtsoftware.dto.domain.UbicacionProvinciasDto;
import org.springframework.stereotype.Component;

@Component
public class WarehouseDtoTransformer {

  public DepositosDto transformNew(CreateOrUpdateWarehouseRequest request) {
    DepositosDto dto = new DepositosDto();
    dto.setNombreDeposito(request.getWarehouseName());
    dto.setDireccion(request.getAddress());
    dto.setActivo(Boolean.TRUE.equals(request.getActive()));
    dto.setIdSucursal(SucursalesDto.builder().id(request.getBranchId()).build());
    dto.setIdPais(UbicacionPaisesDto.builder().id(request.getCountryId()).build());
    dto.setIdProvincia(UbicacionProvinciasDto.builder().id(request.getProvinceId()).build());
    dto.setIdLocalidad(UbicacionLocalidadesDto.builder().id(request.getLocalityId()).build());
    return dto;
  }

  public DepositosDto transformFromExisting(
      CreateOrUpdateWarehouseRequest request, DepositosDto existing) {
    DepositosDto dto = transformNew(request);
    dto.setId(existing.getId());
    dto.setFechaAlta(existing.getFechaAlta());
    dto.setVersion(existing.getVersion());
    return dto;
  }
}
