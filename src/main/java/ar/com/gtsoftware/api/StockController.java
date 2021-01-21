package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.ProductMovementRequest;
import ar.com.gtsoftware.api.response.ProductMovement;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface StockController {

  @PostMapping(path = "/stock/product-movements")
  List<ProductMovement> getProductMovements(@Valid @RequestBody ProductMovementRequest request);
}
