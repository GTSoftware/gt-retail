package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductBrand;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface BrandsController {
  @GetMapping(path = "/products/brands")
  List<ProductBrand> getProductBrands();
}
