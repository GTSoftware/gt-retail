package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ProductsController;
import ar.com.gtsoftware.api.exception.DuplicatedProductCodeException;
import ar.com.gtsoftware.api.exception.ProductNotFoundException;
import ar.com.gtsoftware.api.exception.UserNotAllowedException;
import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.products.CreateOrUpdateProductRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.ProductResponse;
import ar.com.gtsoftware.api.response.ProductSearchResult;
import ar.com.gtsoftware.api.transformer.fromDomain.ProductResponseTransformer;
import ar.com.gtsoftware.api.transformer.fromDomain.ProductSearchResultTransformer;
import ar.com.gtsoftware.api.transformer.toDomain.ProductoDtoTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.ProductosService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductsControllerImpl implements ProductsController {

  private final ProductosService productosService;
  private final ParametrosService parametrosService;
  private final ProductSearchResultTransformer productSearchResultTransformer;
  private final ProductResponseTransformer productResponseTransformer;
  private final ProductoDtoTransformer productoDtoTransformer;
  private final SecurityUtils securityUtils;
  private final IdempotenceHandler idempotenceHandler;

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
    idempotenceHandler.verifyIdempotence(batchUpdateRequest.batchId());
    if (securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
      productosService.updatePrices(batchUpdateRequest);
      idempotenceHandler.setNonce(batchUpdateRequest.batchId(), batchUpdateRequest.batchId());
    } else {
      throw new UserNotAllowedException();
    }
  }

  @Override
  public ProductResponse getProductById(Long productId) {
    final ProductosDto productosDto = productosService.find(productId);
    if (Objects.isNull(productosDto)) {
      throw new ProductNotFoundException();
    }

    return productResponseTransformer.transform(productosDto);
  }

  @Override
  @Transactional
  public void updateProduct(
      Long productId, @Valid CreateOrUpdateProductRequest updateProductRequest) {
    final ProductosDto productosDto = productosService.find(productId);
    if (Objects.isNull(productosDto)) {
      throw new ProductNotFoundException();
    }

    validateProductCode(updateProductRequest.getCode(), updateProductRequest.getProductId());

    productosService.createOrEdit(
        productoDtoTransformer.transformFromExisting(updateProductRequest, productosDto));
  }

  @Override
  @Transactional
  public ProductResponse createProduct(CreateOrUpdateProductRequest updateProductRequest) {
    validateProductCode(updateProductRequest.getCode(), updateProductRequest.getProductId());

    final ProductosDto createdProduct =
        productosService.createOrEdit(productoDtoTransformer.transform(updateProductRequest));

    return productResponseTransformer.transform(createdProduct);
  }

  @Override
  public void validateCode(String code, Long id) {
    validateProductCode(code, id);
  }

  private void validateProductCode(String code, Long id) {
    try {
      productosService.validateProductCode(code, id);
    } catch (ServiceException e) {
      throw new DuplicatedProductCodeException(e.getMessage());
    }
  }
}
