package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ProductsController;
import ar.com.gtsoftware.api.exception.UserNotAllowedException;
import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.MarcasSearchFilter;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.search.RubrosSearchFilter;
import ar.com.gtsoftware.search.SubRubroSearchFilter;
import ar.com.gtsoftware.service.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductsControllerImpl implements ProductsController {

  private static final String DISPLAY_NAME_FMT = "[%d] %s";
  private final ProductosService productosService;
  private final ParametrosService parametrosService;
  private final ProductosRubrosService rubrosService;
  private final ProductosSubRubrosService subRubrosService;
  private final ProductosTiposProveeduriaService tiposProveeduriaService;
  private final ProductosMarcasService productosMarcasService;
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
      response.setData(transformProducts(productos));
    }

    return response;
  }

  private List<ProductSearchResult> transformProducts(List<ProductosDto> productos) {
    List<ProductSearchResult> productSearchResults = new LinkedList<>();

    for (ProductosDto dto : productos) {
      final ProductSearchResult productSearchResult =
          ProductSearchResult.builder()
              .codigoFabricante(dto.getCodigoFabricante())
              .codigoPropio(dto.getCodigoPropio())
              .descripcion(dto.getDescripcion())
              .fechaUltimaModificacion(dto.getFechaUltimaModificacion())
              .productId(dto.getId())
              .brand(transformBrand(dto.getIdMarca()))
              .category(transformCategory(dto.getIdRubro()))
              .subCategory(transformSubCategory(dto.getIdSubRubro()))
              .supplyType(transformSupplyType(dto.getIdTipoProveeduria()))
              .purchaseUnit(dto.getIdTipoUnidadCompra().getNombreUnidad())
              .saleUnit(dto.getIdTipoUnidadVenta().getNombreUnidad())
              .observaciones(dto.getObservaciones())
              .precioVenta(dto.getPrecioVenta())
              .stockActual(dto.getStockActual())
              .stockActualEnSucursal(dto.getStockActualEnSucursal())
              .build();
      if (securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
        productSearchResult.setCostoFinal(dto.getCostoFinal());
        productSearchResult.setCostoAdquisicion(dto.getCostoAdquisicionNeto());
      }

      productSearchResults.add(productSearchResult);
    }

    return productSearchResults;
  }

  @Override
  public List<ProductCategory> getProductCategories() {
    final RubrosSearchFilter sf = new RubrosSearchFilter();
    sf.addSortField("nombreRubro", true);

    return transformCategories(rubrosService.findAllBySearchFilter(sf));
  }

  private List<ProductCategory> transformCategories(List<ProductosRubrosDto> rubrosDtos) {
    List<ProductCategory> categories = new LinkedList<>();
    for (ProductosRubrosDto rubroDto : rubrosDtos) {
      categories.add(transformCategory(rubroDto));
    }

    return categories;
  }

  private ProductCategory transformCategory(ProductosRubrosDto rubroDto) {
    return ProductCategory.builder()
        .categoryName(rubroDto.getNombreRubro())
        .categoryId(rubroDto.getId())
        .displayName(String.format(DISPLAY_NAME_FMT, rubroDto.getId(), rubroDto.getNombreRubro()))
        .build();
  }

  @Override
  public List<ProductSubCategory> getProductSubCategories(Long categoryId) {
    final SubRubroSearchFilter sf =
        SubRubroSearchFilter.builder().idProductosRubros(categoryId).build();
    sf.addSortField("nombreSubRubro", true);

    return transformSubCategories(subRubrosService.findAllBySearchFilter(sf));
  }

  private List<ProductSubCategory> transformSubCategories(
      List<ProductosSubRubrosDto> subRubrosDtos) {
    List<ProductSubCategory> subCategories = new LinkedList<>();
    for (ProductosSubRubrosDto subRubroDto : subRubrosDtos) {
      subCategories.add(transformSubCategory(subRubroDto));
    }

    return subCategories;
  }

  private ProductSubCategory transformSubCategory(ProductosSubRubrosDto subRubroDto) {
    return ProductSubCategory.builder()
        .categoryId(subRubroDto.getIdRubro().getId())
        .subCategoryName(subRubroDto.getNombreSubRubro())
        .subCategoryId(subRubroDto.getId())
        .displayName(
            String.format(DISPLAY_NAME_FMT, subRubroDto.getId(), subRubroDto.getNombreSubRubro()))
        .build();
  }

  @Override
  public List<ProductSupplyType> getProductSupplyTypes() {
    return transformSupplyTypes(tiposProveeduriaService.findAll());
  }

  private List<ProductSupplyType> transformSupplyTypes(
      List<ProductosTiposProveeduriaDto> tiposProveeduriaDtos) {
    List<ProductSupplyType> supplyTypes = new LinkedList<>();
    for (ProductosTiposProveeduriaDto tipoProveeduria : tiposProveeduriaDtos) {
      supplyTypes.add(transformSupplyType(tipoProveeduria));
    }

    return supplyTypes;
  }

  private ProductSupplyType transformSupplyType(ProductosTiposProveeduriaDto tipoProveeduria) {
    return ProductSupplyType.builder()
        .supplyTypeId(tipoProveeduria.getId())
        .supplyTypeName(tipoProveeduria.getNombreTipoProveeduria())
        .displayName(
            String.format(
                DISPLAY_NAME_FMT,
                tipoProveeduria.getId(),
                tipoProveeduria.getNombreTipoProveeduria()))
        .build();
  }

  @Override
  public List<ProductBrand> getProductBrands() {
    MarcasSearchFilter sf = new MarcasSearchFilter();
    sf.addSortField("nombreMarca", true);

    return transformBrands(productosMarcasService.findAllBySearchFilter(sf));
  }

  private List<ProductBrand> transformBrands(List<ProductosMarcasDto> marcasDtos) {
    List<ProductBrand> brands = new LinkedList<>();
    for (ProductosMarcasDto marca : marcasDtos) {
      brands.add(transformBrand(marca));
    }

    return brands;
  }

  private ProductBrand transformBrand(ProductosMarcasDto marcaDto) {
    return ProductBrand.builder()
        .brandId(marcaDto.getId())
        .brandName(marcaDto.getNombreMarca())
        .displayName(String.format(DISPLAY_NAME_FMT, marcaDto.getId(), marcaDto.getNombreMarca()))
        .build();
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
}
