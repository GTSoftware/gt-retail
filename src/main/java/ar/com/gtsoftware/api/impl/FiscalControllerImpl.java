package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.FiscalController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import ar.com.gtsoftware.service.FiscalPeriodosFiscalesService;
import ar.com.gtsoftware.service.FiscalResponsabilidadesIvaService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FiscalControllerImpl implements FiscalController {

  private final FiscalResponsabilidadesIvaService responsabilidadesIvaService;
  private final FiscalPeriodosFiscalesService periodosFiscalesService;

  @Override
  public List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva() {
    return responsabilidadesIvaService.findAll();
  }

  @Override
  public List<FiscalPeriodosFiscalesDto> findFiscalPeriods(
      @Valid PaginatedSearchRequest<FiscalPeriodosFiscalesSearchFilter> searchRequest) {
    return periodosFiscalesService.findBySearchFilter(
        searchRequest.getSearchFilter(),
        searchRequest.getFirstResult(),
        searchRequest.getMaxResults());
  }
}
