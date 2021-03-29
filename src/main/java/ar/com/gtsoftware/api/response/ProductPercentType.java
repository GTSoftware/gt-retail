package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductPercentType {

  private Long percentTypeId;
  private String percentName;
  private String displayName;
  private boolean percent;
}
