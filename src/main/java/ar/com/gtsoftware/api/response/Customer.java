package ar.com.gtsoftware.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
