package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonSearchResult {
  private Long personId;
  private String email;
  private String businessName;
  private String fantasyName;
  private String address;
  private String identification;
  private Long branchId;
  private boolean customer;
  private boolean supplier;
  private String gender;
  private String displayName;
}
