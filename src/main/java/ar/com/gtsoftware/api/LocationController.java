package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.location.CreateOrUpdateTownRequest;
import ar.com.gtsoftware.api.response.location.LocationTown;
import ar.com.gtsoftware.dto.domain.UbicacionPaisesDto;
import ar.com.gtsoftware.dto.domain.UbicacionProvinciasDto;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface LocationController {

  @GetMapping(path = "/locations/countries")
  List<UbicacionPaisesDto> getCountries();

  @GetMapping(path = "/locations/provinces")
  List<UbicacionProvinciasDto> getProvinces(@RequestParam Long countryId);

  @GetMapping(path = "/locations/towns")
  List<LocationTown> getTowns(
      @RequestParam Long provinceId, @RequestParam(required = false) String query);

  @PostMapping(path = "/locations/towns")
  LocationTown createTown(@Valid @RequestBody CreateOrUpdateTownRequest request);

  @PatchMapping(path = "/locations/towns/{townId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void updateTown(@PathVariable Long townId, @RequestBody CreateOrUpdateTownRequest request);
}
