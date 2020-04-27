package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface FiscalController {

    @GetMapping(path = "/fiscal/responsabilidades-iva")
    List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva();
}
