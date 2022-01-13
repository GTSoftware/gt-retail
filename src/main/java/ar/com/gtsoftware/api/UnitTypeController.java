package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductUnitType;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface UnitTypeController {
  @GetMapping(path = "/products/unit-types")
  List<ProductUnitType> getProductUnitTypes();
}
