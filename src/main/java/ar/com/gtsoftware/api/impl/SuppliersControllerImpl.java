package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SuppliersController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.suppliers.SupplierInvoiceResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.SupplierInvoiceSearchResultTransformer;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import ar.com.gtsoftware.service.ComprobantesProveedorService;
import ar.com.gtsoftware.utils.SecurityUtils;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SuppliersControllerImpl implements SuppliersController {

  private final ComprobantesProveedorService proveedorService;
  private final SecurityUtils securityUtils;
  private final PaginatedResponseBuilder responseBuilder;
  private final SupplierInvoiceSearchResultTransformer saleSearchResultTransformer;

  @Override
  // TODO add roles check from Spring
  public PaginatedResponse<SupplierInvoiceResponse> findBySearchFilter(
      @Valid PaginatedSearchRequest<ComprobantesProveedorSearchFilter> searchRequest) {

    final ComprobantesProveedorSearchFilter searchFilter = searchRequest.getSearchFilter();
    searchFilter.addSortField("fechaComprobante", false);

    return responseBuilder.build(proveedorService, searchRequest, saleSearchResultTransformer);
  }
}
