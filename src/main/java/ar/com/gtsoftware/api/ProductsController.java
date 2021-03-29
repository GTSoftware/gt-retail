package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface ProductsController {

  @PostMapping(path = "/products/search")
  PaginatedResponse<ProductSearchResult> findBySearchFilter(
      @Valid @RequestBody PaginatedSearchRequest<ProductosSearchFilter> searchRequest);

  @PutMapping(path = "/products/pricing")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void batchUpdatePrices(@RequestBody BatchPricingUpdateRequest batchUpdateRequest);

  @GetMapping(path = "/products/{productId}")
  ProductResponse getProductById(@PathVariable Long productId);
}
