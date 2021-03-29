package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductUnitType;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosTiposUnidadesDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class ProductUnitTypeTransformer
    implements Transformer<ProductosTiposUnidadesDto, ProductUnitType> {

  @Override
  public ProductUnitType transform(ProductosTiposUnidadesDto from) {
    Objects.requireNonNull(from);

    return ProductUnitType.builder()
        .unitTypeId(from.getId())
        .unitName(from.getNombreUnidad())
        .build();
  }

  @Override
  public List<ProductUnitType> transform(List<ProductosTiposUnidadesDto> from) {
    Objects.requireNonNull(from);

    List<ProductUnitType> unitTypes = new LinkedList<>();
    for (ProductosTiposUnidadesDto productosTiposUnidadesDto : from) {
      unitTypes.add(transform(productosTiposUnidadesDto));
    }

    return unitTypes;
  }
}
