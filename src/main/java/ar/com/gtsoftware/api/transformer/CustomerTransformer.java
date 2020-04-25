package ar.com.gtsoftware.api.transformer;

import ar.com.gtsoftware.api.response.Customer;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerTransformer {

    private static final String ADDRESS_FORMAT = "%s %s Piso: %s Dpto: %s (%s) %s - %s - %s";
    private static final String IDENTIFICATION_FORMAT = "%s %s";
    private final PhoneTransformer phoneTransformer;

    public List<Customer> transformCustomers(List<PersonasDto> personasDtoList) {
        List<Customer> customers = new ArrayList<>(personasDtoList.size());
        for (PersonasDto persona : personasDtoList) {
            customers.add(transformCustomer(persona));
        }

        return customers;
    }

    public Customer transformCustomer(PersonasDto personasDto) {

        return Customer.builder().branchId(personasDto.getIdSucursal().getId())
                .customerId(personasDto.getId())
                .businessName(personasDto.getRazonSocial())
                .email(personasDto.getEmail())
                .fantasyName(personasDto.getNombreFantasia())
                .identification(String.format(IDENTIFICATION_FORMAT,
                        personasDto.getIdTipoDocumento().getNombreTipoDocumento(),
                        personasDto.getDocumento()))
                .address(String.format(ADDRESS_FORMAT,
                        personasDto.getCalle(),
                        personasDto.getAltura(),
                        personasDto.getPiso(),
                        personasDto.getDepto(),
                        personasDto.getIdLocalidad().getCodigoPostal(),
                        personasDto.getIdLocalidad().getNombreLocalidad(),
                        personasDto.getIdProvincia().getNombreProvincia(),
                        personasDto.getIdPais().getNombrePais()))
                .phones(phoneTransformer.transformPhones(personasDto.getPersonasTelefonosList()))
                .responsabilidadIVA(personasDto.getIdResponsabilidadIva().getNombreResponsabildiad())
                .build();
    }

}
