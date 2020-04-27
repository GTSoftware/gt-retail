package ar.com.gtsoftware.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    private Long customerId;
    private String email;
    private String businessName;
    private String fantasyName;
    private String address;
    private String identification;
    private Long branchId;
    private String responsabilidadIVA;
    private List<Phone> phones;
}
