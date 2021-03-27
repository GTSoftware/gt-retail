package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductSearchResult;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSearchResultTransformer {

  private final BrandsTransformer brandsTransformer;
  private final CategoriesTransformer categoriesTransformer;
  private final SupplyTypesTransformer supplyTypesTransformer;
  private final SubCategoriesTransformer subCategoriesTransformer;

  public List<ProductSearchResult> transformProducts(List<ProductosDto> productos) {
    Objects.requireNonNull(productos);

    List<ProductSearchResult> productSearchResults = new LinkedList<>();

    for (ProductosDto dto : productos) {
      productSearchResults.add(transformProduct(dto));
    }

    return productSearchResults;
  }

  public ProductSearchResult transformProduct(ProductosDto dto) {
    Objects.requireNonNull(dto);

    return ProductSearchResult.builder()
        .codigoFabricante(dto.getCodigoFabricante())
        .codigoPropio(dto.getCodigoPropio())
        .descripcion(dto.getDescripcion())
        .fechaUltimaModificacion(dto.getFechaUltimaModificacion())
        .productId(dto.getId())
        .brand(brandsTransformer.transformBrand(dto.getIdMarca()))
        .category(categoriesTransformer.transformCategory(dto.getIdRubro()))
        .subCategory(subCategoriesTransformer.transformSubCategory(dto.getIdSubRubro()))
        .supplyType(supplyTypesTransformer.transformSupplyType(dto.getIdTipoProveeduria()))
        .purchaseUnit(dto.getIdTipoUnidadCompra().getNombreUnidad())
        .saleUnit(dto.getIdTipoUnidadVenta().getNombreUnidad())
        .observaciones(dto.getObservaciones())
        .precioVenta(dto.getPrecioVenta())
        .stockActual(dto.getStockActual())
        .stockActualEnSucursal(dto.getStockActualEnSucursal())
        .costoFinal(dto.getCostoFinal())
        .costoAdquisicion(dto.getCostoAdquisicionNeto())
        .build();
  }
}
