package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SaleSearchResult;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.SaleResponse;
import ar.com.gtsoftware.api.response.SaleTotalsResponse;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SalesController {

  @PostMapping(path = "/sales/search")
  PaginatedResponse<SaleSearchResult> findBySearchFilter(
      @Valid @RequestBody PaginatedSearchRequest<ComprobantesSearchFilter> searchRequest);

  @PostMapping(path = "/sales/totals")
  SaleTotalsResponse getSalesTotals(@Valid @RequestBody ComprobantesSearchFilter searchFilter);

  @GetMapping(path = "/sale/{saleId}")
  SaleResponse getSale(@NotNull @PathVariable Long saleId);
}
