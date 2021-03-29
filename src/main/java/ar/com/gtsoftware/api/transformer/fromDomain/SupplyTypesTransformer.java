package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductSupplyType;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosTiposProveeduriaDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class SupplyTypesTransformer
    implements Transformer<ProductosTiposProveeduriaDto, ProductSupplyType> {

  @Override
  public List<ProductSupplyType> transform(
      List<ProductosTiposProveeduriaDto> tiposProveeduriaDtos) {
    Objects.requireNonNull(tiposProveeduriaDtos);

    List<ProductSupplyType> supplyTypes = new LinkedList<>();

    for (ProductosTiposProveeduriaDto tipoProveeduria : tiposProveeduriaDtos) {
      supplyTypes.add(transform(tipoProveeduria));
    }

    return supplyTypes;
  }

  @Override
  public ProductSupplyType transform(ProductosTiposProveeduriaDto tipoProveeduria) {
    Objects.requireNonNull(tipoProveeduria);

    return ProductSupplyType.builder()
        .supplyTypeId(tipoProveeduria.getId())
        .supplyTypeName(tipoProveeduria.getNombreTipoProveeduria())
        .displayName(
            String.format(
                DISPLAY_NAME_FMT,
                tipoProveeduria.getId(),
                tipoProveeduria.getNombreTipoProveeduria()))
        .build();
  }
}
