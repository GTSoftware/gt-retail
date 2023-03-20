package ar.com.gtsoftware.api.transformer.fromDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.response.ProductSearchResult;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosSubRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosTiposProveeduriaDto;
import ar.com.gtsoftware.dto.domain.ProductosTiposUnidadesDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductSearchResultTransformerTest {

  private static final String FACTORY_CODE = "F1";
  private static final String OWN_CODE = "P1";
  private static final String DESCRIPTION = "TEST";
  private static final LocalDateTime LAST_UPDATED = LocalDateTime.of(2020, 1, 1, 5, 23);
  private static final String OBSERVATIONS = "OBS";
  private static final BigDecimal SALE_PRICE = BigDecimal.valueOf(25);
  private static final BigDecimal COST = BigDecimal.valueOf(20);
  private ProductSearchResultTransformer transformer;

  @Mock private BrandsTransformer brandsTransformer;
  @Mock private CategoriesTransformer categoriesTransformer;
  @Mock private SupplyTypesTransformer supplyTypesTransformer;
  @Mock private SubCategoriesTransformer subCategoriesTransformer;

  @Spy private ProductosDto productoDto;
  @Spy private ProductosTiposUnidadesDto tiposUnidadesDto;
  @Spy private ProductosMarcasDto marcasDto;
  @Spy private ProductosRubrosDto rubrosDto;
  @Spy private ProductosSubRubrosDto subRubrosDto;
  @Spy private ProductosTiposProveeduriaDto tiposProveeduriaDto;

  @BeforeEach
  void setUp() {
    transformer =
        new ProductSearchResultTransformer(
            brandsTransformer,
            categoriesTransformer,
            supplyTypesTransformer,
            subCategoriesTransformer);

    when(productoDto.getId()).thenReturn(1L);
    when(productoDto.getCodigoFabricante()).thenReturn(FACTORY_CODE);
    when(productoDto.getCodigoPropio()).thenReturn(OWN_CODE);
    when(productoDto.getDescripcion()).thenReturn(DESCRIPTION);
    when(productoDto.getFechaUltimaModificacion()).thenReturn(LAST_UPDATED);
    when(productoDto.getIdTipoUnidadCompra()).thenReturn(tiposUnidadesDto);
    when(productoDto.getIdTipoUnidadVenta()).thenReturn(tiposUnidadesDto);
    when(productoDto.getObservaciones()).thenReturn(OBSERVATIONS);
    when(productoDto.getPrecioVenta()).thenReturn(SALE_PRICE);
    when(productoDto.getStockActual()).thenReturn(BigDecimal.ONE);
    when(productoDto.getStockActualEnSucursal()).thenReturn(BigDecimal.ONE);
    when(productoDto.getCostoFinal()).thenReturn(COST);
    when(productoDto.getCostoAdquisicionNeto()).thenReturn(COST);

    when(productoDto.getIdMarca()).thenReturn(marcasDto);
    when(productoDto.getIdRubro()).thenReturn(rubrosDto);
    when(productoDto.getIdSubRubro()).thenReturn(subRubrosDto);
    when(productoDto.getIdTipoProveeduria()).thenReturn(tiposProveeduriaDto);

    when(tiposUnidadesDto.getNombreUnidad()).thenReturn("UN");
  }

  @Test
  void shouldTransformProduct() {
    final ProductSearchResult productSearchResult = transformer.transform(productoDto);

    assertThat(productSearchResult).isNotNull();
    productSearchResultAssertions(productSearchResult);

    verifyTransformers();
  }

  @Test
  void shouldTransformProducts() {
    final List<ProductSearchResult> productSearchResults =
        transformer.transform(List.of(productoDto));

    assertThat(productSearchResults).isNotEmpty();
    assertThat(productSearchResults).hasSize(1);
    productSearchResultAssertions(productSearchResults.get(0));
    verifyTransformers();
  }

  private void productSearchResultAssertions(ProductSearchResult productSearchResult) {
    assertThat(productSearchResult.getProductId()).isEqualTo(1L);
    assertThat(productSearchResult.getCodigoFabricante()).isEqualTo(FACTORY_CODE);
    assertThat(productSearchResult.getCodigoPropio()).isEqualTo(OWN_CODE);
    assertThat(productSearchResult.getDescripcion()).isEqualTo(DESCRIPTION);
    assertThat(productSearchResult.getFechaUltimaModificacion()).isEqualTo(LAST_UPDATED);
    assertThat(productSearchResult.getPurchaseUnit()).isEqualTo("UN");
    assertThat(productSearchResult.getSaleUnit()).isEqualTo("UN");
    assertThat(productSearchResult.getObservaciones()).isEqualTo(OBSERVATIONS);
    assertThat(productSearchResult.getPrecioVenta()).isEqualTo(SALE_PRICE);
    assertThat(productSearchResult.getStockActual()).isEqualTo(BigDecimal.ONE);
    assertThat(productSearchResult.getStockActualEnSucursal()).isEqualTo(BigDecimal.ONE);
    assertThat(productSearchResult.getCostoFinal()).isEqualTo(COST);
    assertThat(productSearchResult.getCostoAdquisicion()).isEqualTo(COST);
  }

  private void verifyTransformers() {
    verify(brandsTransformer).transform(marcasDto);
    verify(categoriesTransformer).transform(rubrosDto);
    verify(subCategoriesTransformer).transform(subRubrosDto);
    verify(supplyTypesTransformer).transform(tiposProveeduriaDto);
  }
}
