package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductBrand;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class BrandsTransformer implements Transformer<ProductosMarcasDto, ProductBrand> {

  @Override
  public List<ProductBrand> transform(List<ProductosMarcasDto> marcasDtos) {
    Objects.requireNonNull(marcasDtos);

    List<ProductBrand> brands = new ArrayList<>(marcasDtos.size());

    for (ProductosMarcasDto marca : marcasDtos) {
      brands.add(transform(marca));
    }

    return brands;
  }

  @Override
  public ProductBrand transform(ProductosMarcasDto marcaDto) {
    Objects.requireNonNull(marcaDto);

    return ProductBrand.builder()
        .brandId(marcaDto.getId())
        .brandName(marcaDto.getNombreMarca())
        .displayName(String.format(DISPLAY_NAME_FMT, marcaDto.getId(), marcaDto.getNombreMarca()))
        .version(marcaDto.getVersion())
        .build();
  }
}
