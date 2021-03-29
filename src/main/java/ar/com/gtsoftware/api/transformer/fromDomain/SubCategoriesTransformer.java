package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductSubCategory;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosSubRubrosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class SubCategoriesTransformer
    implements Transformer<ProductosSubRubrosDto, ProductSubCategory> {

  @Override
  public List<ProductSubCategory> transform(List<ProductosSubRubrosDto> subRubrosDtos) {
    Objects.requireNonNull(subRubrosDtos);

    List<ProductSubCategory> subCategories = new LinkedList<>();

    for (ProductosSubRubrosDto subRubroDto : subRubrosDtos) {
      subCategories.add(transform(subRubroDto));
    }

    return subCategories;
  }

  @Override
  public ProductSubCategory transform(ProductosSubRubrosDto subRubroDto) {
    Objects.requireNonNull(subRubroDto);

    return ProductSubCategory.builder()
        .categoryId(subRubroDto.getIdRubro().getId())
        .subCategoryName(subRubroDto.getNombreSubRubro())
        .subCategoryId(subRubroDto.getId())
        .displayName(
            String.format(DISPLAY_NAME_FMT, subRubroDto.getId(), subRubroDto.getNombreSubRubro()))
        .build();
  }
}
