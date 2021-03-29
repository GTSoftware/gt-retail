package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PriceListController;
import ar.com.gtsoftware.api.response.PriceList;
import ar.com.gtsoftware.api.transformer.fromDomain.PriceListTransformer;
import ar.com.gtsoftware.dto.domain.ProductosListasPreciosDto;
import ar.com.gtsoftware.service.ProductosListasPreciosService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceListControllerImpl implements PriceListController {
  private final ProductosListasPreciosService listasPreciosService;
  private final PriceListTransformer priceListTransformer;

  @Override
  public List<PriceList> getPriceLists() {
    final List<ProductosListasPreciosDto> listasPrecio = listasPreciosService.findAll();

    return priceListTransformer.transform(listasPrecio);
  }
}
