package ar.com.gtsoftware.api.transformer.fromDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.response.ProductSubCategory;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosSubRubrosDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubCategoriesTransformerTest {

  @Spy private ProductosSubRubrosDto subRubroDto;
  @Spy private ProductosRubrosDto rubroDto;

  private SubCategoriesTransformer transformer;

  @BeforeEach
  void setUp() {
    transformer = new SubCategoriesTransformer();

    when(subRubroDto.getId()).thenReturn(1L);
    when(subRubroDto.getIdRubro()).thenReturn(rubroDto);
    when(subRubroDto.getNombreSubRubro()).thenReturn("TEST");

    when(rubroDto.getId()).thenReturn(2L);
  }

  @Test
  void shouldTransformSubCategory() {
    final ProductSubCategory productSubCategory = transformer.transform(subRubroDto);

    assertThat(productSubCategory).isNotNull();
    subCategoryAssertions(productSubCategory);
  }

  @Test
  void shouldTransformSubCategories() {
    final List<ProductSubCategory> productSubCategories =
        transformer.transform(List.of(subRubroDto));

    assertThat(productSubCategories).isNotEmpty();
    assertThat(productSubCategories).hasSize(1);
    subCategoryAssertions(productSubCategories.get(0));
  }

  private void subCategoryAssertions(ProductSubCategory productSubCategory) {
    assertThat(productSubCategory.getDisplayName()).isEqualTo("[1] TEST");
    assertThat(productSubCategory.getSubCategoryId()).isEqualTo(1L);
    assertThat(productSubCategory.getCategoryId()).isEqualTo(2L);
    assertThat(productSubCategory.getSubCategoryName()).isEqualTo("TEST");
  }
}
