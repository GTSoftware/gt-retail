package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.NewCustomerRequest;
import ar.com.gtsoftware.api.response.Customer;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CustomersController {

  @PostMapping(path = "/customers/customer")
  Customer addNewCustomer(@Valid @RequestBody NewCustomerRequest newCustomerRequest);

  @GetMapping(path = "/customers/customer")
  Customer getCustomer(
      @RequestParam Long identificationTypeId, @RequestParam Long identificationNumber);
}
