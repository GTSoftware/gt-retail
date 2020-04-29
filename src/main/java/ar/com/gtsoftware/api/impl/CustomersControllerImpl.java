package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CustomersController;
import ar.com.gtsoftware.api.exception.CustomerNotFoundException;
import ar.com.gtsoftware.api.request.NewCustomerRequest;
import ar.com.gtsoftware.api.response.Customer;
import ar.com.gtsoftware.api.transformer.fromDomain.CustomerTransformer;
import ar.com.gtsoftware.api.transformer.toDomain.PersonaDtoTransformer;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.ClientesService;
import ar.com.gtsoftware.service.PersonasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CustomersControllerImpl implements CustomersController {

    private final ClientesService clientesService;
    private final PersonasService personasService;
    private final CustomerTransformer customerTransformer;
    private final PersonaDtoTransformer personaDtoTransformer;

    @Override
    public Customer addNewCustomer(@Valid NewCustomerRequest newCustomerRequest) {
        PersonasDto customer = personaDtoTransformer.transformNewCustomer(newCustomerRequest);

        return customerTransformer.transformCustomer(clientesService.guardarCliente(customer));
    }

    @Override
    public Customer getCustomer(Long identificationTypeId, Long identificationNumber) {
        final PersonasSearchFilter psf = PersonasSearchFilter.builder()
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

}
