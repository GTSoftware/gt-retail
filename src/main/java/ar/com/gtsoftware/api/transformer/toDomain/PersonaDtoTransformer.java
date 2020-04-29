package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.NewCustomerRequest;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonaDtoTransformer {

    private final SecurityUtils securityUtils;

    public PersonasDto transformNewCustomer(NewCustomerRequest newCustomerRequest) {
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
