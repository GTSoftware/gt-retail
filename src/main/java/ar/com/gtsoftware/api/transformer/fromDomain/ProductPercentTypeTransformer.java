package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductPercentType;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosTiposPorcentajesDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class ProductPercentTypeTransformer
    implements Transformer<ProductosTiposPorcentajesDto, ProductPercentType> {

  @Override
  public ProductPercentType transform(ProductosTiposPorcentajesDto from) {
    Objects.requireNonNull(from);

    return ProductPercentType.builder()
        .percent(from.isPorcentaje())
        .percentName(from.getNombreTipo())
        .percentTypeId(from.getId())
        .displayName(String.format(DISPLAY_NAME_FMT, from.getId(), from.getNombreTipo()))
        .build();
  }

  @Override
  public List<ProductPercentType> transform(List<ProductosTiposPorcentajesDto> from) {
    Objects.requireNonNull(from);

    List<ProductPercentType> percentTypes = new LinkedList<>();
    for (ProductosTiposPorcentajesDto tiposPorcentajesDto : from) {
      percentTypes.add(transform(tiposPorcentajesDto));
    }

    return percentTypes;
  }
}
