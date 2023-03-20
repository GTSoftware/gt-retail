package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.suppliers.SupplierInvoiceResponse;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SuppliersController {

  @PostMapping(path = "/supplier-invoices/search")
  PaginatedResponse<SupplierInvoiceResponse> findBySearchFilter(
      @Valid @RequestBody PaginatedSearchRequest<ComprobantesProveedorSearchFilter> searchRequest);
}
