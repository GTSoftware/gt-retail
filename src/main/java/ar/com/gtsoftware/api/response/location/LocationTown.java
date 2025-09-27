package ar.com.gtsoftware.api.response.location;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LocationTown {

  private Long townId;
  private String displayName;
  private Integer version;
}
