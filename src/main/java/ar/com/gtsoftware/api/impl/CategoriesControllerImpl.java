package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CategoriesController;
import ar.com.gtsoftware.api.exception.CategoryNotFoundException;
import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.api.transformer.fromDomain.CategoriesTransformer;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.search.RubrosSearchFilter;
import ar.com.gtsoftware.service.ProductosRubrosService;

import java.util.List;

import ar.com.gtsoftware.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

  @Override
  @Transactional
  public ProductCategory updateCategory(Long categoryId, @Valid ProductCategory productCategory) {
    ProductosRubrosDto rubroDto = rubrosService.find(categoryId);
    if (rubroDto == null) {
      throw new CategoryNotFoundException();
    }

    rubroDto.setNombreRubro(TextUtils.upperCaseTrim(productCategory.getCategoryName()));
    rubroDto.setVersion(productCategory.getVersion());

    ProductosRubrosDto updatedRubroDto = rubrosService.createOrEdit(rubroDto);

    return transformer.transform(updatedRubroDto);
  }


  @Override
  @Transactional
  public ProductCategory createCategory(@Valid ProductCategory productCategory) {
    ProductosRubrosDto rubroDto = new ProductosRubrosDto();
    rubroDto.setNombreRubro(TextUtils.upperCaseTrim(productCategory.getCategoryName()));

    return transformer.transform(rubrosService.createOrEdit(rubroDto));
  }

  @Override
  @Transactional
  public void deleteCategory(Long categoryId) {
    ProductosRubrosDto rubroDto = rubrosService.find(categoryId);
    if (rubroDto == null) {
      throw new CategoryNotFoundException();
    }
    rubrosService.remove(rubroDto);
  }
}
