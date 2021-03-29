package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CategoriesController;
import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.api.transformer.fromDomain.CategoriesTransformer;
import ar.com.gtsoftware.search.RubrosSearchFilter;
import ar.com.gtsoftware.service.ProductosRubrosService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoriesControllerImpl implements CategoriesController {

  private final ProductosRubrosService rubrosService;
  private final CategoriesTransformer transformer;

  @Override
  public List<ProductCategory> getProductCategories() {
    final RubrosSearchFilter sf = new RubrosSearchFilter();
    sf.addSortField("nombreRubro", true);

    return transformer.transform(rubrosService.findAllBySearchFilter(sf));
  }
}
