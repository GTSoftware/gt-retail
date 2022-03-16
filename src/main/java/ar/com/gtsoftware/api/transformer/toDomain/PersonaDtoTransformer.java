package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.CreateOrUpdateCustomerRequest;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonaDtoTransformer {

  private final SecurityUtils securityUtils;

  public PersonasDto transformNewCustomer(
      CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest) {
    return PersonasDto.builder()
        .idSucursal(
            SucursalesDto.builder().id(securityUtils.getUserDetails().getSucursalId()).build())
        .razonSocial(createOrUpdateCustomerRequest.getRazonSocial())
        .apellidos(createOrUpdateCustomerRequest.getApellidos())
        .nombreFantasia(createOrUpdateCustomerRequest.getNombreFantasia())
        .nombres(createOrUpdateCustomerRequest.getNombres())
        .calle(createOrUpdateCustomerRequest.getCalle())
        .altura(createOrUpdateCustomerRequest.getAltura())
        .piso(createOrUpdateCustomerRequest.getPiso())
        .depto(createOrUpdateCustomerRequest.getDepto())
        .documento(createOrUpdateCustomerRequest.getDocumento())
        .email(createOrUpdateCustomerRequest.getEmail())
        .idGenero(LegalGenerosDto.builder().id(createOrUpdateCustomerRequest.getGeneroId()).build())
        .idLocalidad(
            UbicacionLocalidadesDto.builder()
                .id(createOrUpdateCustomerRequest.getLocalidadId())
                .build())
        .idPais(UbicacionPaisesDto.builder().id(createOrUpdateCustomerRequest.getPaisId()).build())
        .idProvincia(
            UbicacionProvinciasDto.builder()
                .id(createOrUpdateCustomerRequest.getProvinciaId())
                .build())
        .idResponsabilidadIva(
            FiscalResponsabilidadesIvaDto.builder()
                .id(createOrUpdateCustomerRequest.getResponsabilidadIvaId())
                .build())
        .idTipoDocumento(
            LegalTiposDocumentoDto.builder()
                .id(createOrUpdateCustomerRequest.getTipoDocumentoId())
                .build())
        .idTipoPersoneria(
            LegalTiposPersoneriaDto.builder()
                .id(createOrUpdateCustomerRequest.getTipoPersoneriaId())
                .build())
        .personasTelefonosList(createOrUpdateCustomerRequest.getTelefonos())
        .build();
  }

  public PersonasDto transformFromExisting(
      CreateOrUpdateCustomerRequest updateCustomerRequest, PersonasDto existingCustomer) {
    final PersonasDto customer = transformNewCustomer(updateCustomerRequest);
    customer.setFechaAlta(existingCustomer.getFechaAlta());
    customer.setIdSucursal(existingCustomer.getIdSucursal());

    return customer;
  }
}
