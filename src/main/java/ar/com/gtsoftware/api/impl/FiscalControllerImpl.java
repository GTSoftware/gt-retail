package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.FiscalController;
import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.service.FiscalResponsabilidadesIvaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FiscalControllerImpl implements FiscalController {

  private final FiscalResponsabilidadesIvaService responsabilidadesIvaService;

  @Override
  public List<FiscalResponsabilidadesIvaDto> getResponsabilidadesIva() {
    return responsabilidadesIvaService.findAll();
  }
}
