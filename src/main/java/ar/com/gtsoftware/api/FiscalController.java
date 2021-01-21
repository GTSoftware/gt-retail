package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface FiscalController {

  @GetMapping(path = "/fiscal/responsabilidades-iva")
  List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva();
}
