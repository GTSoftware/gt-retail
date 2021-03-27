package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductBrand;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class BrandsTransformer {
  private static final String DISPLAY_NAME_FMT = "[%d] %s";

  public List<ProductBrand> transformBrands(List<ProductosMarcasDto> marcasDtos) {
    Objects.requireNonNull(marcasDtos);

    List<ProductBrand> brands = new LinkedList<>();

    for (ProductosMarcasDto marca : marcasDtos) {
      brands.add(transformBrand(marca));
    }

    return brands;
  }

  public ProductBrand transformBrand(ProductosMarcasDto marcaDto) {
    Objects.requireNonNull(marcaDto);

    return ProductBrand.builder()
        .brandId(marcaDto.getId())
        .brandName(marcaDto.getNombreMarca())
        .displayName(String.format(DISPLAY_NAME_FMT, marcaDto.getId(), marcaDto.getNombreMarca()))
        .build();
  }
}
