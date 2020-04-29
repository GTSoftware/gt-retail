package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.Phone;
import ar.com.gtsoftware.dto.domain.PersonasTelefonosDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PhoneTransformer {

    public List<Phone> transformPhones(List<PersonasTelefonosDto> telefonosList) {
        List<Phone> phones = null;
        if (telefonosList != null) {
            phones = new ArrayList<>(telefonosList.size());
            for (PersonasTelefonosDto tel : telefonosList) {
                phones.add(Phone.builder()
                        .phoneNumber(tel.getNumero())
                        .reference(tel.getReferencia())
                        .build());
            }
        }
        return phones;
    }
}
