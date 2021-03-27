package ar.com.gtsoftware.api.transformer.fromDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriesTransformerTest {

  @Spy private ProductosRubrosDto rubroDto;

  private CategoriesTransformer transformer;

  @BeforeEach
  void setUp() {
    transformer = new CategoriesTransformer();

    when(rubroDto.getId()).thenReturn(1L);
    when(rubroDto.getNombreRubro()).thenReturn("TEST");
  }

  @Test
  void shouldTransformCategory() {
    final ProductCategory productCategory = transformer.transformCategory(rubroDto);

    assertThat(productCategory).isNotNull();
    categoryAssertions(productCategory);
  }

  @Test
  void shouldTransformCategories() {
    final List<ProductCategory> productCategories =
        transformer.transformCategories(List.of(rubroDto));

    assertThat(productCategories).isNotEmpty();
    assertThat(productCategories).hasSize(1);
    categoryAssertions(productCategories.get(0));
  }

  private void categoryAssertions(ProductCategory productCategory) {
    assertThat(productCategory.getDisplayName()).isEqualTo("[1] TEST");
    assertThat(productCategory.getCategoryId()).isEqualTo(1L);
    assertThat(productCategory.getCategoryName()).isEqualTo("TEST");
  }
}
