package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.FiscalController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.FiscalTaxRate;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.transformer.fromDomain.FiscalTaxRateTransformer;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import ar.com.gtsoftware.service.FiscalAlicuotasIvaService;
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
  private final FiscalAlicuotasIvaService alicuotasIvaService;
  private final FiscalTaxRateTransformer taxRateTransformer;

  private final PaginatedResponseBuilder responseBuilder;

  @Override
  public List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva() {
    return responsabilidadesIvaService.findAll();
  }

  @Override
  public PaginatedResponse<FiscalPeriodosFiscalesDto> findFiscalPeriods(
      @Valid PaginatedSearchRequest<FiscalPeriodosFiscalesSearchFilter> searchRequest) {
    final FiscalPeriodosFiscalesSearchFilter searchFilter = searchRequest.getSearchFilter();
    if (!searchFilter.hasOrderFields()) {
      searchFilter.addSortField("id", false);
    }

    return responseBuilder.build(periodosFiscalesService, searchRequest);
  }

  @Override
  public List<FiscalTaxRate> getTaxRates() {
    final List<FiscalAlicuotasIvaDto> alicuotas = alicuotasIvaService.findAll();

    return taxRateTransformer.transform(alicuotas);
  }
}
