package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ProductsController;
import ar.com.gtsoftware.api.exception.UserNotAllowedException;
import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.api.transformer.fromDomain.ProductSearchResultTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductsControllerImpl implements ProductsController {

  private final ProductosService productosService;
  private final ParametrosService parametrosService;
  private final ProductSearchResultTransformer productSearchResultTransformer;
  private final SecurityUtils securityUtils;
  private final ProductosTiposPorcentajesService productosTiposPorcentajesService;

  @Override
  public PaginatedResponse<ProductSearchResult> findBySearchFilter(
      @Valid PaginatedSearchRequest<ProductosSearchFilter> searchRequest) {
    final ProductosSearchFilter searchFilter = searchRequest.getSearchFilter();
    if (Objects.isNull(searchFilter.getIdListaPrecio())) {
      searchFilter.setIdListaPrecio(parametrosService.getLongParam(Parametros.ID_LISTA_VENTA));
    }

    final int count = productosService.countBySearchFilter(searchFilter);
    final PaginatedResponse<ProductSearchResult> response =
        PaginatedResponse.<ProductSearchResult>builder().totalResults(count).build();

    if (count > 0) {
      final List<ProductosDto> productos =
          productosService.findBySearchFilter(
              searchFilter, searchRequest.getFirstResult(), searchRequest.getMaxResults());
      response.setData(productSearchResultTransformer.transformProducts(productos));
    }

    return response;
  }

  @Override
  public void batchUpdatePrices(@Valid BatchPricingUpdateRequest batchUpdateRequest) {
    if (securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
      productosService.updatePrices(batchUpdateRequest);
    } else {
      throw new UserNotAllowedException();
    }
  }

  @Override
  public List<ProductosTiposPorcentajesDto> getPercentTypes() {
    return productosTiposPorcentajesService.findAll();
  }

  @Override
  public ProductResponse getProductById(Long productId) {
    return null;
  }
}
