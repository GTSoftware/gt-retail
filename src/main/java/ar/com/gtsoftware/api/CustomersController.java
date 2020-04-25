package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.NewCustomerRequest;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

public interface CustomersController {

    @PostMapping(path = "/customers/customer")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponse(code = 201, message = "Created")
    void addNewCustomer(@Valid @RequestBody NewCustomerRequest newCustomerRequest);
}
