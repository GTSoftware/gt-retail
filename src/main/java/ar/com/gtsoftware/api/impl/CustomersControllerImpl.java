package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CustomersController;
import ar.com.gtsoftware.api.exception.CustomerNotFoundException;
import ar.com.gtsoftware.api.request.NewCustomerRequest;
import ar.com.gtsoftware.api.response.Customer;
import ar.com.gtsoftware.api.transformer.CustomerTransformer;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.ClientesService;
import ar.com.gtsoftware.service.PersonasService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CustomersControllerImpl implements CustomersController {

    private final ClientesService clientesService;
    private final PersonasService personasService;
    private final SecurityUtils securityUtils;
    private final CustomerTransformer customerTransformer;

    @Override
    public Customer addNewCustomer(@Valid NewCustomerRequest newCustomerRequest) {
        PersonasDto customer = transformNewCustomer(newCustomerRequest);

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

    private PersonasDto transformNewCustomer(NewCustomerRequest newCustomerRequest) {
        return PersonasDto.builder()
                .idSucursal(SucursalesDto.builder().id(securityUtils.getUserDetails().getSucursalId()).build())
                .razonSocial(newCustomerRequest.getRazonSocial())
                .apellidos(newCustomerRequest.getApellidos())
                .nombreFantasia(newCustomerRequest.getNombreFantasia())
                .nombres(newCustomerRequest.getNombres())
                .calle(newCustomerRequest.getCalle())
                .altura(newCustomerRequest.getAltura())
                .piso(newCustomerRequest.getPiso())
                .depto(newCustomerRequest.getDepto())
                .documento(newCustomerRequest.getDocumento())
                .email(newCustomerRequest.getEmail())
                .idGenero(LegalGenerosDto.builder().id(newCustomerRequest.getGeneroId()).build())
                .idLocalidad(UbicacionLocalidadesDto.builder().id(newCustomerRequest.getLocalidadId()).build())
                .idPais(UbicacionPaisesDto.builder().id(newCustomerRequest.getPaisId()).build())
                .idProvincia(UbicacionProvinciasDto.builder().id(newCustomerRequest.getProvinciaId()).build())
                .idResponsabilidadIva(FiscalResponsabilidadesIvaDto.builder().id(newCustomerRequest.getResponsabilidadIvaId()).build())
                .idTipoDocumento(LegalTiposDocumentoDto.builder().id(newCustomerRequest.getTipoDocumentoId()).build())
                .idTipoPersoneria(LegalTiposPersoneriaDto.builder().id(newCustomerRequest.getTipoPersoneriaId()).build())
                .personasTelefonosList(newCustomerRequest.getTelefonos())
                .build();
    }
}
