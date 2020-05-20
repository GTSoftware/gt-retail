package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SaleSearchResult;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.SaleTotalsResponse;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface SalesController {

    @PostMapping(path = "/sales/search")
    PaginatedResponse<SaleSearchResult> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<ComprobantesSearchFilter> searchRequest);

    @PostMapping(path = "/sales/totals")
    SaleTotalsResponse getSalesTotals(@Valid @RequestBody ComprobantesSearchFilter searchFilter);
}
