package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PointOfSale {
  private int posNumber;
  private String posType;
  private String displayName;
  private boolean byDefault;
}
