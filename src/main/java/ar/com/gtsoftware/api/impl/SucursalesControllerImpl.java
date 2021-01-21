package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SucursalesController;
import ar.com.gtsoftware.dto.domain.SucursalesDto;
import ar.com.gtsoftware.search.SucursalesSearchFilter;
import ar.com.gtsoftware.service.SucursalesService;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class SucursalesControllerImpl implements SucursalesController {

  private final SucursalesService sucursalesService;
  private final SecurityUtils securityUtils;

  @Override
  public List<SucursalesDto> retrieveAllSucursales() {
    return sucursalesService.findAll();
  }

  @Override
  public SucursalesDto retrieveSucursal(@PathVariable Long sucursalId) {
    return sucursalesService.find(sucursalId);
  }

  @Override
  public List<SucursalesDto> findBySearchFilter(@Valid @RequestBody SucursalesSearchFilter sf) {
    if (!sf.hasOrderFields()) {
      sf.addSortField("nombreSucursal", true);
    }

    return sucursalesService.findAllBySearchFilter(sf);
  }
}
