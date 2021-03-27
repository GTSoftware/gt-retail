package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductSupplyType;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface SupplyTypesController {
  @GetMapping(path = "/products/supply-types")
  List<ProductSupplyType> getProductSupplyTypes();
}
