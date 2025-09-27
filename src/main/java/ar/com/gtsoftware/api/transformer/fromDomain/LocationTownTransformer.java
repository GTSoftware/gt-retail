package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.location.LocationTown;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.UbicacionLocalidadesDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class LocationTownTransformer implements Transformer<UbicacionLocalidadesDto, LocationTown> {

  @Override
  public List<LocationTown> transform(List<UbicacionLocalidadesDto> from) {
    Objects.requireNonNull(from);

    List<LocationTown> towns = new ArrayList<>(from.size());

    for (var item : from) {
      towns.add(transform(item));
    }

    return towns;
  }

  @Override
  public LocationTown transform(UbicacionLocalidadesDto from) {
    Objects.requireNonNull(from);

    return LocationTown.builder()
        .townId(from.getId())
        .displayName("%s (%s)".formatted(from.getNombreLocalidad(), from.getCodigoPostal()))
        .version(from.getVersion())
        .build();
  }
}
