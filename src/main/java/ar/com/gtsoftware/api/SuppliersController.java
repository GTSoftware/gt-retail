package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.CreateOrUpdateCustomerRequest;
import ar.com.gtsoftware.api.response.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface SuppliersController {

  @PostMapping(path = "/suppliers")
  CustomerResponse addNewSupplier(
      @RequestBody CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest);

  @GetMapping(path = "/suppliers/supplier")
  CustomerResponse getSupplier(
      @RequestParam Long identificationTypeId, @RequestParam Long identificationNumber);

  @GetMapping(path = "/suppliers/{supplierId}")
  CustomerResponse getSupplierById(@PathVariable Long supplierId);

  @PatchMapping(path = "/suppliers/{supplierId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void updateSupplier(
      @PathVariable Long supplierId,
      @RequestBody CreateOrUpdateCustomerRequest updateCustomerRequest);
}
