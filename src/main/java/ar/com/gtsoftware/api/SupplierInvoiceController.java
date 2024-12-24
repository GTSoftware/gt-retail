package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.suppliers.NewSupplierInvoiceRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.suppliers.SupplierInvoiceResponse;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SupplierInvoiceController {

  @PostMapping(path = "/supplier-invoices/search")
  PaginatedResponse<SupplierInvoiceResponse> findInvoicesBySearchFilter(
      @Valid @RequestBody PaginatedSearchRequest<ComprobantesProveedorSearchFilter> searchRequest);

  @DeleteMapping(path = "/supplier-invoices/{invoiceId}")
  void deleteInvoiceById(@PathParam("invoiceId") Long invoiceId);

  @PostMapping(path = "/supplier-invoice/")
  SupplierInvoiceResponse createInvoice(
      @Valid @RequestBody NewSupplierInvoiceRequest invoiceRequest);
}
