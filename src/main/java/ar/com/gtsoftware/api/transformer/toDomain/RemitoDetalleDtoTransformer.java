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

    for (DeliveryNoteItem item : deliveryNoteItems) {
      detalleDtos.add(transformDeliveryItem(item));
    }

    return detalleDtos;
  }

  public RemitoDetalleDto transformDeliveryItem(DeliveryNoteItem item) {
    return RemitoDetalleDto.builder()
        .cantidad(item.getQuantity())
        .nroLinea(item.getItemNumber())
        .idProducto(ProductosDto.builder().id(item.getProductId()).build())
        .build();
  }
}
