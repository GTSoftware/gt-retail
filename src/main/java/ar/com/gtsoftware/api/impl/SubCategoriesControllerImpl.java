package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SubCategoriesController;
import ar.com.gtsoftware.api.exception.CategoryNotFoundException;
import ar.com.gtsoftware.api.exception.SubCategoryNotFoundException;
import ar.com.gtsoftware.api.response.ProductSubCategory;
import ar.com.gtsoftware.api.transformer.fromDomain.SubCategoriesTransformer;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosSubRubrosDto;
import ar.com.gtsoftware.search.SubRubroSearchFilter;
import ar.com.gtsoftware.service.ProductosRubrosService;
import ar.com.gtsoftware.service.ProductosSubRubrosService;

import java.util.List;

import ar.com.gtsoftware.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SubCategoriesControllerImpl implements SubCategoriesController {


  private final ProductosSubRubrosService subRubrosService;
  private final ProductosRubrosService rubrosService;
  private final SubCategoriesTransformer subCategoriesTransformer;

  @Override
  public List<ProductSubCategory> getProductSubCategories(Long categoryId) {
    final SubRubroSearchFilter sf =
            SubRubroSearchFilter.builder().idProductosRubros(categoryId).build();
    sf.addSortField("nombreSubRubro", true);

    return subCategoriesTransformer.transform(subRubrosService.findAllBySearchFilter(sf));
  }

  @Override
  @Transactional
  public ProductSubCategory updateSubCategory(Long categoryId, Long subCategoryId, ProductSubCategory productSubCategory) {
    ProductosSubRubrosDto existingSubCategory = subRubrosService.find(subCategoryId);

    if (existingSubCategory == null) {
      throw new SubCategoryNotFoundException();
    }

    existingSubCategory.setNombreSubRubro(TextUtils.upperCaseTrim(productSubCategory.getSubCategoryName()));
    existingSubCategory = subRubrosService.createOrEdit(existingSubCategory);

    return subCategoriesTransformer.transform(existingSubCategory);
  }

  @Override
  @Transactional
  public ProductSubCategory createSubCategory(Long categoryId,@Valid ProductSubCategory productSubCategory) {
    ProductosRubrosDto productosRubrosDto = rubrosService.find(categoryId);

    if(productosRubrosDto==null) {
      throw new CategoryNotFoundException();
    }

    ProductosSubRubrosDto newSubRubro = new ProductosSubRubrosDto();

    newSubRubro.setNombreSubRubro(TextUtils.upperCaseTrim(productSubCategory.getSubCategoryName()));
    newSubRubro.setIdRubro(productosRubrosDto);

    newSubRubro = subRubrosService.createOrEdit(newSubRubro);

    return subCategoriesTransformer.transform(newSubRubro);
  }

  @Override
  @Transactional
  public void deleteSubCategory(Long categoryId, Long subCategoryId) {

    ProductosSubRubrosDto subCategory = subRubrosService.find(subCategoryId);

    if (subCategory == null) {
      throw new SubCategoryNotFoundException();
    }

    subRubrosService.remove(subCategory);
  }
}
