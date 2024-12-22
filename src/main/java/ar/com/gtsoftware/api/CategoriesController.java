package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductCategory;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategoriesController {
  @GetMapping(path = "/products/categories")
  List<ProductCategory> getProductCategories();

  @GetMapping(path = "/products/category/{categoryId}")
  ProductCategory getProductCategory(@PathVariable Long categoryId);

  @PutMapping(path = "/products/category/{categoryId}")
  ProductCategory updateCategory(
      @PathVariable Long categoryId, @RequestBody ProductCategory productCategory);

  @PostMapping(path = "/products/categories")
  ProductCategory createCategory(@RequestBody ProductCategory productCategory);

  @DeleteMapping(path = "/products/category/{categoryId}")
  void deleteCategory(@PathVariable Long categoryId);
}
