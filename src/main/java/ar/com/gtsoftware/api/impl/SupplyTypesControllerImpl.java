package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SupplyTypesController;
import ar.com.gtsoftware.api.response.ProductSupplyType;
import ar.com.gtsoftware.api.transformer.fromDomain.SupplyTypesTransformer;
import ar.com.gtsoftware.service.ProductosTiposProveeduriaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupplyTypesControllerImpl implements SupplyTypesController {

  private final ProductosTiposProveeduriaService tiposProveeduriaService;
  private final SupplyTypesTransformer supplyTypesTransformer;

  @Override
  public List<ProductSupplyType> getProductSupplyTypes() {
    return supplyTypesTransformer.transformSupplyTypes(tiposProveeduriaService.findAll());
  }
}
