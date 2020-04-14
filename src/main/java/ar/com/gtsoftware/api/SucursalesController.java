package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.SucursalesDto;
import ar.com.gtsoftware.search.SucursalesSearchFilter;
import ar.com.gtsoftware.service.SucursalesService;
import ar.com.gtsoftware.utils.SecurityUtils;
import io.swagger.annotations.ApiModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@ApiModel(description = "Gestión de sucursales")
public class SucursalesController {

    private final SucursalesService sucursalesService;
    private final SecurityUtils securityUtils;
    private final Logger logger = LoggerFactory.getLogger(SucursalesController.class);


    @GetMapping(path = "/sucursales")
    public List<SucursalesDto> retrieveAllSucursales() {
        return sucursalesService.findAll();
    }

    @GetMapping(path = "/sucursales/{sucursalId}")
    public SucursalesDto retrieveSucursal(@PathVariable Long sucursalId) {
        return sucursalesService.find(sucursalId);
    }

    @PostMapping(path = "/sucursales/search-all")
    public List<SucursalesDto> findBySearchFilter(@Valid @RequestBody SucursalesSearchFilter sf) {
        if (!sf.hasOrderFields()) {
            sf.addSortField("nombreSucursal", true);
        }
        return sucursalesService.findAllBySearchFilter(sf);
    }

}
