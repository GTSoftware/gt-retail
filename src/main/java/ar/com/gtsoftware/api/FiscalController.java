package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.FiscalTaxRate;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FiscalController {

  @GetMapping(path = "/fiscal/responsabilidades-iva")
  List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva();

  @PostMapping(path = "/fiscal/fiscal-periods")
  PaginatedResponse<FiscalPeriodosFiscalesDto> findFiscalPeriods(
      @Valid @RequestBody PaginatedSearchRequest<FiscalPeriodosFiscalesSearchFilter> searchRequest);

  @GetMapping(path = "/fiscal/tax-rates")
  List<FiscalTaxRate> getTaxRates();
}
