package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductBrand;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BrandsController {
  @GetMapping(path = "/products/brands")
  List<ProductBrand> getProductBrands();

  @GetMapping(path = "/products/brand/{brandId}")
  ProductBrand getProductBrand(@PathVariable Long brandId);

  @PutMapping(path = "/products/brand/{brandId}")
  ProductBrand updateBrand(@PathVariable Long brandId, @RequestBody ProductBrand productBrand);

  @PostMapping(path = "/products/categories")
  ProductBrand createBrand(@RequestBody ProductBrand productBrand);

  @DeleteMapping(path = "/products/brand/{brandId}")
  void deleteBrand(@PathVariable Long brandId);
}
