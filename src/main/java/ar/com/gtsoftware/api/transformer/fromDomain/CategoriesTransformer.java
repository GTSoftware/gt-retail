package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductCategory;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CategoriesTransformer {
  private static final String DISPLAY_NAME_FMT = "[%d] %s";

  public List<ProductCategory> transformCategories(List<ProductosRubrosDto> rubrosDtos) {
    Objects.requireNonNull(rubrosDtos);

    List<ProductCategory> categories = new LinkedList<>();

    for (ProductosRubrosDto rubroDto : rubrosDtos) {
      categories.add(transformCategory(rubroDto));
    }

    return categories;
  }

  public ProductCategory transformCategory(ProductosRubrosDto rubroDto) {
    Objects.requireNonNull(rubroDto);

    return ProductCategory.builder()
        .categoryName(rubroDto.getNombreRubro())
        .categoryId(rubroDto.getId())
        .displayName(String.format(DISPLAY_NAME_FMT, rubroDto.getId(), rubroDto.getNombreRubro()))
        .build();
  }
}
