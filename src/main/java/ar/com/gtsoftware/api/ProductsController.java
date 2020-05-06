package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.dto.domain.ProductosTiposPorcentajesDto;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface ProductsController {

    @PostMapping(path = "/products/search")
    PaginatedResponse<ProductSearchResult> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<ProductosSearchFilter> searchRequest);

    @GetMapping(path = "/products/categories")
    List<ProductCategory> getProductCategories();

    @GetMapping(path = "/products/sub-categories")
    List<ProductSubCategory> getProductSubCategories(@RequestParam Long categoryId);

    @GetMapping(path = "/products/supply-types")
    List<ProductSupplyType> getProductSupplyTypes();

    @GetMapping(path = "/products/brands")
    List<ProductBrand> getProductBrands();

    @PutMapping(path = "/products/pricing")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void batchUpdatePrices(@RequestBody BatchPricingUpdateRequest batchUpdateRequest);

    @GetMapping(path = "/products/pricing/percent-types")
    List<ProductosTiposPorcentajesDto> getPercentTypes();
}
