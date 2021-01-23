package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
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
  List<FiscalPeriodosFiscalesDto> findFiscalPeriods(
      @Valid @RequestBody PaginatedSearchRequest<FiscalPeriodosFiscalesSearchFilter> searchRequest);
}
