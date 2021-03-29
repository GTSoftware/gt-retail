package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.PersonSearchResult;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonSearchResultTransformer implements Transformer<PersonasDto, PersonSearchResult> {

  private static final String ADDRESS_FORMAT = "%s %s Piso: %s Dpto: %s (%s) %s - %s - %s";
  private static final String IDENTIFICATION_FORMAT = "%s %s";
  private static final String DISPLAY_NAME_FORMAT = "[%s] %s";

  @Override
  public List<PersonSearchResult> transform(List<PersonasDto> personasDtoList) {
    List<PersonSearchResult> persons = new ArrayList<>(personasDtoList.size());
    for (PersonasDto persona : personasDtoList) {
      persons.add(transform(persona));
    }

    return persons;
  }

  @Override
  public PersonSearchResult transform(PersonasDto personaDto) {
    return PersonSearchResult.builder()
        .branchId(personaDto.getIdSucursal().getId())
        .personId(personaDto.getId())
        .businessName(personaDto.getRazonSocial())
        .email(personaDto.getEmail())
        .fantasyName(personaDto.getNombreFantasia())
        .identification(
            String.format(
                IDENTIFICATION_FORMAT,
                personaDto.getIdTipoDocumento().getNombreTipoDocumento(),
                personaDto.getDocumento()))
        .address(
            String.format(
                ADDRESS_FORMAT,
                personaDto.getCalle(),
                personaDto.getAltura(),
                personaDto.getPiso(),
                personaDto.getDepto(),
                personaDto.getIdLocalidad().getCodigoPostal(),
                personaDto.getIdLocalidad().getNombreLocalidad(),
                personaDto.getIdProvincia().getNombreProvincia(),
                personaDto.getIdPais().getNombrePais()))
        .customer(personaDto.isCliente())
        .supplier(personaDto.isProveedor())
        .gender(personaDto.getIdGenero().getNombreGenero())
        .displayName(
            String.format(
                DISPLAY_NAME_FORMAT, personaDto.getDocumento(), personaDto.getRazonSocial()))
        .build();
  }
}
