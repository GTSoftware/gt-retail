package ar.com.gtsoftware.api.transformer.toDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ar.com.gtsoftware.api.request.DeliveryNoteItem;
import ar.com.gtsoftware.dto.domain.RemitoDetalleDto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class RemitoDetalleDtoTransformerTest {

  private RemitoDetalleDtoTransformer transformer = new RemitoDetalleDtoTransformer();

  @Test
  void transformDeliveryItems() {
    DeliveryNoteItem deliveryItem1 = new DeliveryNoteItem(1L, BigDecimal.ONE);
    DeliveryNoteItem deliveryItem2 = new DeliveryNoteItem(2L, BigDecimal.TEN);

    final List<RemitoDetalleDto> remitoDetalleDtos =
        transformer.transformDeliveryItems(List.of(deliveryItem1, deliveryItem2));

    assertThat(remitoDetalleDtos).hasSize(2);

    final RemitoDetalleDto remitoDetalleDto1 = remitoDetalleDtos.get(0);
    final RemitoDetalleDto remitoDetalleDto2 = remitoDetalleDtos.get(1);

    assertThat(remitoDetalleDto1.getNroLinea()).isEqualTo(1);
    assertThat(remitoDetalleDto1.getIdProducto().getId()).isEqualTo(1L);
    assertThat(remitoDetalleDto1.getCantidad()).isEqualTo(BigDecimal.ONE);

    assertThat(remitoDetalleDto2.getNroLinea()).isEqualTo(2);
    assertThat(remitoDetalleDto2.getIdProducto().getId()).isEqualTo(2L);
    assertThat(remitoDetalleDto2.getCantidad()).isEqualTo(BigDecimal.TEN);
  }

  @Test
  void shouldTransformDeliveryItem() {
    DeliveryNoteItem deliveryItem = new DeliveryNoteItem(1L, BigDecimal.ONE);

    final RemitoDetalleDto remitoDetalleDto = transformer.transformDeliveryItem(deliveryItem, 5);

    assertThat(remitoDetalleDto).isNotNull();
    assertThat(remitoDetalleDto.getNroLinea()).isEqualTo(5);
    assertThat(remitoDetalleDto.getIdProducto().getId()).isEqualTo(1L);
    assertThat(remitoDetalleDto.getCantidad()).isEqualTo(BigDecimal.ONE);
  }
}
