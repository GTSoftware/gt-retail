package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductCategory;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface CategoriesController {
  @GetMapping(path = "/products/categories")
  List<ProductCategory> getProductCategories();
}
