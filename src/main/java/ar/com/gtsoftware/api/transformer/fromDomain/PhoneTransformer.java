package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.Phone;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.PersonasTelefonosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PhoneTransformer implements Transformer<PersonasTelefonosDto, Phone> {

  @Override
  public Phone transform(PersonasTelefonosDto from) {
    Objects.requireNonNull(from);

    return Phone.builder()
        .phoneId(from.getId())
        .version(from.getVersion())
        .phoneNumber(from.getNumero())
        .reference(from.getReferencia())
        .build();
  }

  @Override
  public List<Phone> transform(List<PersonasTelefonosDto> telefonosList) {
    List<Phone> phones = null;
    if (telefonosList != null) {
      phones = new LinkedList<>();
      for (PersonasTelefonosDto tel : telefonosList) {
        phones.add(transform(tel));
      }
    }
    return phones;
  }
}
