package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.SucursalesDto;
import ar.com.gtsoftware.search.SucursalesSearchFilter;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@ApiModel(description = "Gestión de sucursales")
public interface SucursalesController {
    @GetMapping(path = "/sucursales")
    List<SucursalesDto> retrieveAllSucursales();

    @GetMapping(path = "/sucursales/{sucursalId}")
    SucursalesDto retrieveSucursal(@PathVariable Long sucursalId);

    @PostMapping(path = "/sucursales/search-all")
    List<SucursalesDto> findBySearchFilter(@Valid @RequestBody SucursalesSearchFilter sf);
}
