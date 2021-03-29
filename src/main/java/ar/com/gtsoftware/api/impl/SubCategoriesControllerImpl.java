package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SubCategoriesController;
import ar.com.gtsoftware.api.response.ProductSubCategory;
import ar.com.gtsoftware.api.transformer.fromDomain.SubCategoriesTransformer;
import ar.com.gtsoftware.search.SubRubroSearchFilter;
import ar.com.gtsoftware.service.ProductosSubRubrosService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubCategoriesControllerImpl implements SubCategoriesController {

  private final ProductosSubRubrosService subRubrosService;
  private final SubCategoriesTransformer subCategoriesTransformer;

  @Override
  public List<ProductSubCategory> getProductSubCategories(Long categoryId) {
    final SubRubroSearchFilter sf =
        SubRubroSearchFilter.builder().idProductosRubros(categoryId).build();
    sf.addSortField("nombreSubRubro", true);

    return subCategoriesTransformer.transform(subRubrosService.findAllBySearchFilter(sf));
  }
}
