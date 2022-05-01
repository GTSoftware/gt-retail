package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.CustomerResponse;
import ar.com.gtsoftware.api.response.IdentificationType;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerTransformer {

  private static final String ADDRESS_FORMAT = "%s %s Piso: %s Dpto: %s (%s) %s - %s - %s";
  private static final String IDENTIFICATION_FORMAT = "%s %s";
  private final PhoneTransformer phoneTransformer;

  public List<CustomerResponse> transformCustomers(List<PersonasDto> personasDtoList) {
    List<CustomerResponse> customerResponses = new ArrayList<>(personasDtoList.size());
    for (PersonasDto persona : personasDtoList) {
      customerResponses.add(transformCustomer(persona));
    }

    return customerResponses;
  }

  public CustomerResponse transformCustomer(PersonasDto personasDto) {

    return CustomerResponse.builder()
        .branchId(personasDto.getIdSucursal().getId())
        .customerId(personasDto.getId())
        .businessName(personasDto.getRazonSocial())
        .email(personasDto.getEmail())
        .fantasyName(personasDto.getNombreFantasia())
        .identification(
            String.format(
                IDENTIFICATION_FORMAT,
                personasDto.getIdTipoDocumento().getNombreTipoDocumento(),
                personasDto.getDocumento()))
        .address(
            String.format(
                ADDRESS_FORMAT,
                personasDto.getCalle(),
                personasDto.getAltura(),
                personasDto.getPiso(),
                personasDto.getDepto(),
                personasDto.getIdLocalidad().getCodigoPostal(),
                personasDto.getIdLocalidad().getNombreLocalidad(),
                personasDto.getIdProvincia().getNombreProvincia(),
                personasDto.getIdPais().getNombrePais()))
        .phones(phoneTransformer.transform(personasDto.getPersonasTelefonosList()))
        .responsabilidadIVA(personasDto.getIdResponsabilidadIva().getNombreResponsabildiad())
        .version(personasDto.getVersion())
        .pais(personasDto.getIdPais())
        .provincia(personasDto.getIdProvincia())
        .localidad(personasDto.getIdLocalidad())
        .genero(personasDto.getIdGenero())
        .tipoDocumento(
            IdentificationType.builder()
                .id(personasDto.getIdTipoDocumento().getId())
                .identificationTypeName(personasDto.getIdTipoDocumento().getNombreTipoDocumento())
                .build())
        .documento(personasDto.getDocumento())
        .tipoPersoneria(personasDto.getIdTipoPersoneria())
        .calle(personasDto.getCalle())
        .altura(personasDto.getAltura())
        .piso(personasDto.getPiso())
        .depto(personasDto.getDepto())
        .nombres(personasDto.getNombres())
        .apellidos(personasDto.getApellidos())
        .tipoResponsableIva(personasDto.getIdResponsabilidadIva())
        .razonSocial(personasDto.getRazonSocial())
        .activo(personasDto.isActivo())
        .build();
  }
}
