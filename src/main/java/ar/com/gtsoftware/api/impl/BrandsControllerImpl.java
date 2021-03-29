package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.BrandsController;
import ar.com.gtsoftware.api.response.ProductBrand;
import ar.com.gtsoftware.api.transformer.fromDomain.BrandsTransformer;
import ar.com.gtsoftware.search.MarcasSearchFilter;
import ar.com.gtsoftware.service.ProductosMarcasService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BrandsControllerImpl implements BrandsController {

  private final ProductosMarcasService productosMarcasService;
  private final BrandsTransformer brandsTransformer;

  @Override
  public List<ProductBrand> getProductBrands() {
    MarcasSearchFilter sf = new MarcasSearchFilter();
    sf.addSortField("nombreMarca", true);

    return brandsTransformer.transform(productosMarcasService.findAllBySearchFilter(sf));
  }
}
