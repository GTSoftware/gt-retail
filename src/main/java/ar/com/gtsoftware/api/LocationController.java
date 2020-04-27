package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.UbicacionLocalidadesDto;
import ar.com.gtsoftware.dto.domain.UbicacionPaisesDto;
import ar.com.gtsoftware.dto.domain.UbicacionProvinciasDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LocationController {

    @GetMapping(path = "/locations/countries")
    List<UbicacionPaisesDto> getCountries();

    @GetMapping(path = "/locations/provinces")
    List<UbicacionProvinciasDto> getProvinces(@RequestParam Long countryId);

    @GetMapping(path = "/locations/towns")
    List<UbicacionLocalidadesDto> getTowns(@RequestParam Long provinceId, @RequestParam(required = false) String query);
}
