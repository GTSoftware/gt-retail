package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.PriceList;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosListasPreciosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PriceListTransformer implements Transformer<ProductosListasPreciosDto, PriceList> {

  @Override
  public PriceList transform(ProductosListasPreciosDto from) {
    Objects.requireNonNull(from);

    return PriceList.builder()
        .priceListId(from.getId())
        .priceListName(from.getNombreLista())
        .displayName(String.format(DISPLAY_NAME_FMT, from.getId(), from.getNombreLista()))
        .build();
  }

  @Override
  public List<PriceList> transform(List<ProductosListasPreciosDto> from) {
    Objects.requireNonNull(from);

    List<PriceList> priceList = new LinkedList<>();
    for (ProductosListasPreciosDto listPrecio : from) {
      priceList.add(transform(listPrecio));
    }

    return priceList;
  }
}
