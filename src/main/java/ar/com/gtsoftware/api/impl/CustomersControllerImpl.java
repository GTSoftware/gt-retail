package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CustomersController;
import ar.com.gtsoftware.api.exception.CustomerNotFoundException;
import ar.com.gtsoftware.api.request.CreateOrUpdateCustomerRequest;
import ar.com.gtsoftware.api.response.Customer;
import ar.com.gtsoftware.api.transformer.fromDomain.CustomerTransformer;
import ar.com.gtsoftware.api.transformer.toDomain.PersonaDtoTransformer;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.ClientesService;
import ar.com.gtsoftware.service.PersonasService;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomersControllerImpl implements CustomersController {

  private final ClientesService clientesService;
  private final PersonasService personasService;
  private final CustomerTransformer customerTransformer;
  private final PersonaDtoTransformer personaDtoTransformer;

  @Override
  public Customer addNewCustomer(@Valid CreateOrUpdateCustomerRequest newCustomer) {
    PersonasDto customer = personaDtoTransformer.transformNewCustomer(newCustomer);

    return customerTransformer.transformCustomer(clientesService.guardarCliente(customer));
  }

  @Override
  public Customer getCustomer(Long identificationTypeId, Long identificationNumber) {
    final PersonasSearchFilter psf =
        PersonasSearchFilter.builder()
            .idTipoDocumento(identificationTypeId)
            .documento(identificationNumber.toString())
            .activo(true)
            .build();
    final PersonasDto personaDto = personasService.findFirstBySearchFilter(psf);

    if (personaDto == null) {
      throw new CustomerNotFoundException();
    }

    return customerTransformer.transformCustomer(personaDto);
  }

  @Override
  public Customer getCustomerById(Long customerId) {
    final PersonasDto personaDto = personasService.find(customerId);

    if (personaDto == null) {
      throw new CustomerNotFoundException();
    }

    return customerTransformer.transformCustomer(personaDto);
  }

  @Override
  public void updateCustomer(
      Long customerId, @Valid CreateOrUpdateCustomerRequest updateCustomerRequest) {
    final PersonasDto personaDto = personasService.find(customerId);
    if (Objects.isNull(personaDto)) {
      throw new CustomerNotFoundException();
    }

    personasService.createOrEdit(
        personaDtoTransformer.transformFromExisting(updateCustomerRequest, personaDto));
  }
}
