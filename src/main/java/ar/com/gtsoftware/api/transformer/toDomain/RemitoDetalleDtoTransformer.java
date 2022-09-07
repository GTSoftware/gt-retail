package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.DeliveryNoteItem;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.RemitoDetalleDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemitoDetalleDtoTransformer {

  public List<RemitoDetalleDto> transformDeliveryItems(List<DeliveryNoteItem> deliveryNoteItems) {
    List<RemitoDetalleDto> detalleDtos = new ArrayList<>(deliveryNoteItems.size());

    int lineNumber = 1;
    for (DeliveryNoteItem item : deliveryNoteItems) {
      detalleDtos.add(transformDeliveryItem(item, lineNumber));
      lineNumber++;
    }

    return detalleDtos;
  }

  public RemitoDetalleDto transformDeliveryItem(DeliveryNoteItem item, int lineNumber) {
    return RemitoDetalleDto.builder()
        .cantidad(item.getQuantity())
        .nroLinea(lineNumber)
        .idProducto(ProductosDto.builder().id(item.getProductId()).build())
        .build();
  }
}
