package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CategoriesTransformer implements Transformer<ProductosRubrosDto, ProductCategory> {

  @Override
  public List<ProductCategory> transform(List<ProductosRubrosDto> rubrosDtos) {
    Objects.requireNonNull(rubrosDtos);

    List<ProductCategory> categories = new ArrayList<>(rubrosDtos.size());

    for (ProductosRubrosDto rubroDto : rubrosDtos) {
      categories.add(transform(rubroDto));
    }

    return categories;
  }

  @Override
  public ProductCategory transform(ProductosRubrosDto rubroDto) {
    Objects.requireNonNull(rubroDto);

    return ProductCategory.builder()
        .categoryName(rubroDto.getNombreRubro())
        .categoryId(rubroDto.getId())
        .displayName(String.format(DISPLAY_NAME_FMT, rubroDto.getId(), rubroDto.getNombreRubro()))
        .version(rubroDto.getVersion())
        .build();
  }
}
