package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.LocationController;
import ar.com.gtsoftware.dto.domain.UbicacionLocalidadesDto;
import ar.com.gtsoftware.dto.domain.UbicacionPaisesDto;
import ar.com.gtsoftware.dto.domain.UbicacionProvinciasDto;
import ar.com.gtsoftware.search.LocalidadesSearchFilter;
import ar.com.gtsoftware.search.ProvinciasSearchFilter;
import ar.com.gtsoftware.service.UbicacionLocalidadesService;
import ar.com.gtsoftware.service.UbicacionPaisesService;
import ar.com.gtsoftware.service.UbicacionProvinciasService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationControllerImpl implements LocationController {

  private final UbicacionPaisesService paisesService;
  private final UbicacionProvinciasService provinciasService;
  private final UbicacionLocalidadesService localidadesService;

  @Override
  public List<UbicacionPaisesDto> getCountries() {
    return paisesService.findAll();
  }

  @Override
  public List<UbicacionProvinciasDto> getProvinces(Long countryId) {
    final ProvinciasSearchFilter sf = ProvinciasSearchFilter.builder().idPais(countryId).build();
    sf.addSortField("nombreProvincia", true);
    return provinciasService.findAllBySearchFilter(sf);
  }

  @Override
  public List<UbicacionLocalidadesDto> getTowns(Long provinceId, String query) {
    final LocalidadesSearchFilter sf =
        LocalidadesSearchFilter.builder().idProvincia(provinceId).nombreLocalidad(query).build();
    sf.addSortField("nombreLocalidad", true);

    final List<UbicacionLocalidadesDto> localidades = localidadesService.findAllBySearchFilter(sf);
    for (UbicacionLocalidadesDto localidad : localidades) {
      localidad.setDisplayName(
          String.format("(%s) %s", localidad.getCodigoPostal(), localidad.getNombreLocalidad()));
    }

    return localidades;
  }
}
