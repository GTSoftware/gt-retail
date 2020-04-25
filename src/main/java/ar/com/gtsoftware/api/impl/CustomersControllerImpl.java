package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CustomersController;
import ar.com.gtsoftware.api.request.NewCustomerRequest;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.service.ClientesService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CustomersControllerImpl implements CustomersController {

    private final ClientesService clientesService;
    private final SecurityUtils securityUtils;

    @Override
    public void addNewCustomer(@Valid NewCustomerRequest newCustomerRequest) {
        PersonasDto customer = transformCustomer(newCustomerRequest);

        clientesService.guardarCliente(customer);
    }

    private PersonasDto transformCustomer(NewCustomerRequest newCustomerRequest) {
        return PersonasDto.builder()
                .idSucursal(SucursalesDto.builder().id(securityUtils.getUserDetails().getSucursalId()).build())
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
