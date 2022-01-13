package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.UnitTypeController;
import ar.com.gtsoftware.api.response.ProductUnitType;
import ar.com.gtsoftware.api.transformer.fromDomain.ProductUnitTypeTransformer;
import ar.com.gtsoftware.search.UnidadesSearchFilter;
import ar.com.gtsoftware.service.ProductosTiposUnidadesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UnitTypeControllerImpl implements UnitTypeController {

  private final ProductosTiposUnidadesService tiposUnidadesService;
  private final ProductUnitTypeTransformer unitTypeTransformer;

  @Override
  public List<ProductUnitType> getProductUnitTypes() {
    UnidadesSearchFilter sf = new UnidadesSearchFilter();
    sf.addSortField("nombreUnidad", true);

    return unitTypeTransformer.transform(tiposUnidadesService.findAllBySearchFilter(sf));
  }
}
