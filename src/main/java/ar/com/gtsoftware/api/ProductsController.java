package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.products.CreateOrUpdateProductRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.exceptions.ServiceException;
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

  @PatchMapping(path = "/products/{productId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void updateProduct(
      @PathVariable Long productId, @RequestBody CreateOrUpdateProductRequest updateProductRequest);

  @PostMapping(path = "/products")
  ProductResponse createProduct(@RequestBody CreateOrUpdateProductRequest updateProductRequest);

  @GetMapping(path = "/products/valid-code")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void validateCode(
      @RequestParam(name = "productCode") String code,
      @RequestParam(name = "productId", required = false) Long id)
      throws ServiceException;
}
