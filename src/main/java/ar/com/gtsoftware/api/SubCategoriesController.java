package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.api.response.ProductSubCategory;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SubCategoriesController {
  @GetMapping(path = "/products/category/{categoryId}/sub-categories")
  List<ProductSubCategory> getProductSubCategories(@PathVariable Long categoryId);


  @PutMapping(path = "/products/categories/{categoryId}/sub-category/{subCategoryId}")
  ProductSubCategory updateSubCategory(@PathVariable Long categoryId, @PathVariable Long subCategoryId, @RequestBody ProductSubCategory productSubCategory);

  @PostMapping(path = "/products/category/{categoryId}/sub-categories")
  ProductSubCategory createSubCategory(@PathVariable Long categoryId,  @RequestBody ProductSubCategory productCategory);

  @DeleteMapping(path = "/products/categories/{categoryId}/sub-category/{subCategoryId}")
  void deleteSubCategory(@PathVariable Long categoryId, @PathVariable Long subCategoryId);
}
