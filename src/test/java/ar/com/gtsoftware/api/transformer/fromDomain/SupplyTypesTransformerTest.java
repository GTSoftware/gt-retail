package ar.com.gtsoftware.api.transformer.fromDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.response.ProductSupplyType;
import ar.com.gtsoftware.dto.domain.ProductosTiposProveeduriaDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupplyTypesTransformerTest {

  @Spy private ProductosTiposProveeduriaDto tiposProveeduriaDto;

  private SupplyTypesTransformer transformer;

  @BeforeEach
  void setUp() {
    transformer = new SupplyTypesTransformer();

    when(tiposProveeduriaDto.getId()).thenReturn(1L);
    when(tiposProveeduriaDto.getNombreTipoProveeduria()).thenReturn("TEST");
  }

  @Test
  void shouldTransformSupplyType() {
    final ProductSupplyType productSupplyType =
        transformer.transformSupplyType(tiposProveeduriaDto);

    assertThat(productSupplyType).isNotNull();
    supplyTypeAssertions(productSupplyType);
  }

  @Test
  void shouldTransformSupplyTypes() {
    final List<ProductSupplyType> productSupplyTypes =
        transformer.transformSupplyTypes(List.of(tiposProveeduriaDto));

    assertThat(productSupplyTypes).isNotEmpty();
    assertThat(productSupplyTypes).hasSize(1);
    supplyTypeAssertions(productSupplyTypes.get(0));
  }

  private void supplyTypeAssertions(ProductSupplyType supplyType) {
    assertThat(supplyType.getDisplayName()).isEqualTo("[1] TEST");
    assertThat(supplyType.getSupplyTypeId()).isEqualTo(1L);
    assertThat(supplyType.getSupplyTypeName()).isEqualTo("TEST");
  }
}
