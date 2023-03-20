package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductSearchResult;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSearchResultTransformer
    implements Transformer<ProductosDto, ProductSearchResult> {

  private final BrandsTransformer brandsTransformer;
  private final CategoriesTransformer categoriesTransformer;
  private final SupplyTypesTransformer supplyTypesTransformer;
  private final SubCategoriesTransformer subCategoriesTransformer;

  @Override
  public ProductSearchResult transform(ProductosDto dto) {
    Objects.requireNonNull(dto);

    return ProductSearchResult.builder()
        .codigoFabricante(dto.getCodigoFabricante())
        .codigoPropio(dto.getCodigoPropio())
        .descripcion(dto.getDescripcion())
        .fechaUltimaModificacion(dto.getFechaUltimaModificacion())
        .productId(dto.getId())
        .brand(brandsTransformer.transform(dto.getIdMarca()))
        .category(categoriesTransformer.transform(dto.getIdRubro()))
        .subCategory(subCategoriesTransformer.transform(dto.getIdSubRubro()))
        .supplyType(supplyTypesTransformer.transform(dto.getIdTipoProveeduria()))
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

  @Override
  public List<ProductSearchResult> transform(List<ProductosDto> from) {
    Objects.requireNonNull(from);

    return from.stream().map(this::transform).toList();
  }
}
