package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.ProductPercentType;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface ProductPercentTypeController {

  @GetMapping(path = "/products/pricing/percent-types")
  List<ProductPercentType> getPercentTypes();
}
