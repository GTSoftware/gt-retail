package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ProductPercentTypeController;
import ar.com.gtsoftware.api.response.ProductPercentType;
import ar.com.gtsoftware.api.transformer.fromDomain.ProductPercentTypeTransformer;
import ar.com.gtsoftware.dto.domain.ProductosTiposPorcentajesDto;
import ar.com.gtsoftware.service.ProductosTiposPorcentajesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductPercentTypeControllerImpl implements ProductPercentTypeController {
  private final ProductosTiposPorcentajesService tiposPorcentajesService;
  private final ProductPercentTypeTransformer transformer;

  @Override
  public List<ProductPercentType> getPercentTypes() {
    final List<ProductosTiposPorcentajesDto> tiposPorcentaje = tiposPorcentajesService.findAll();

    return transformer.transform(tiposPorcentaje);
  }
}
