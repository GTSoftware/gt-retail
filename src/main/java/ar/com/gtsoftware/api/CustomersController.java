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

public interface CustomersController {

  @PostMapping(path = "/customers")
  CustomerResponse addNewCustomer(
      @RequestBody CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest);

  @GetMapping(path = "/customers/customer")
  CustomerResponse getCustomer(
      @RequestParam Long identificationTypeId, @RequestParam Long identificationNumber);

  @GetMapping(path = "/customers/{customerId}")
  CustomerResponse getCustomerById(@PathVariable Long customerId);

  @PatchMapping(path = "/customers/{customerId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void updateCustomer(
      @PathVariable Long customerId,
      @RequestBody CreateOrUpdateCustomerRequest updateCustomerRequest);
}
