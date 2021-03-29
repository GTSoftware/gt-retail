package ar.com.gtsoftware.api.transformer.fromDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.response.ProductBrand;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandsTransformerTest {

  @Spy private ProductosMarcasDto marcaDto;

  private BrandsTransformer transformer;

  @BeforeEach
  void setUp() {
    transformer = new BrandsTransformer();

    when(marcaDto.getId()).thenReturn(1L);
    when(marcaDto.getNombreMarca()).thenReturn("TEST");
  }

  @Test
  void shouldTransformBrand() {

    final ProductBrand productBrand = transformer.transform(marcaDto);

    assertThat(productBrand).isNotNull();
    brandAssertions(productBrand);
  }

  @Test
  void shouldTransformBrands() {

    final List<ProductBrand> productBrands = transformer.transform(List.of(marcaDto));

    assertThat(productBrands).isNotEmpty();
    assertThat(productBrands).hasSize(1);
    brandAssertions(productBrands.get(0));
  }

  private void brandAssertions(ProductBrand productBrand) {
    assertThat(productBrand.getDisplayName()).isEqualTo("[1] TEST");
    assertThat(productBrand.getBrandId()).isEqualTo(1L);
    assertThat(productBrand.getBrandName()).isEqualTo("TEST");
  }
}
