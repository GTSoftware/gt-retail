package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.ProductSearchResponse;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ProductsController {

    @PostMapping(path = "/products/search")
    PaginatedResponse<ProductSearchResponse> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<ProductosSearchFilter> searchRequest);
}
