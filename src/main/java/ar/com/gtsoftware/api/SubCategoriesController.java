package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductSubCategory;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface SubCategoriesController {
  @GetMapping(path = "/products/categories/{categoryId}/sub-categories")
  List<ProductSubCategory> getProductSubCategories(@PathVariable Long categoryId);
}
