package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SalesController;
import ar.com.gtsoftware.api.exception.SaleNotFoundException;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SaleSearchResult;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.SaleResponse;
import ar.com.gtsoftware.api.response.SaleTotalsResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.SaleResponseTransformer;
import ar.com.gtsoftware.api.transformer.fromDomain.SaleSearchResultTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.utils.SecurityUtils;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SalesControllerImpl implements SalesController {

  private final ComprobantesService comprobantesService;
  private final SaleSearchResultTransformer saleSearchResultTransformer;
  private final SecurityUtils securityUtils;
  private final SaleResponseTransformer saleResponseTransformer;
  private final PaginatedResponseBuilder responseBuilder;

  @Override
  public PaginatedResponse<SaleSearchResult> findBySearchFilter(
      @Valid PaginatedSearchRequest<ComprobantesSearchFilter> searchRequest) {
    final ComprobantesSearchFilter searchFilter = searchRequest.getSearchFilter();
    searchFilter.addSortField("fechaComprobante", false);
    if (securityUtils.userHasRole(Roles.VENDEDORES)
        && !securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
      searchFilter.setIdUsuario(securityUtils.getUserDetails().getId());
    }

    return responseBuilder.build(comprobantesService, searchRequest, saleSearchResultTransformer);
  }

  @Override
  public SaleTotalsResponse getSalesTotals(ComprobantesSearchFilter searchFilter) {
    SaleTotalsResponse totalsResponse = new SaleTotalsResponse();

    searchFilter.setFacturada(true);
    totalsResponse.setInvoicedTotal(comprobantesService.calcularTotalVentas(searchFilter));

    searchFilter.setFacturada(false);
    totalsResponse.setNotInvoicedTotal(comprobantesService.calcularTotalVentas(searchFilter));

    return totalsResponse;
  }

  @Override
  public SaleResponse getSale(Long saleId) {
    final ComprobantesDto comprobante = comprobantesService.find(saleId);
    if (comprobante == null) {
      throw new SaleNotFoundException();
    }

    return saleResponseTransformer.transformSale(comprobante);
  }
}
