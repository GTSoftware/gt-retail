package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.DeliveryNoteDetail;
import ar.com.gtsoftware.api.response.DeliveryNoteSearchResult;
import ar.com.gtsoftware.dto.domain.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DeliveryNoteSearchResultTransformer {

  private static final String INTERNAL_FORMAT = "INTERNO: [%d] %s Suc: %s";
  private static final String EXTERNAL_FORMAT = "EXTERNO: [%d] %s";

  public List<DeliveryNoteSearchResult> transformDeliveryNotes(List<RemitoDto> remitos) {
    List<DeliveryNoteSearchResult> deliveryNotes = new ArrayList<>(remitos.size());
    for (RemitoDto remito : remitos) {
      deliveryNotes.add(transformDeliveryNote(remito));
    }

    return deliveryNotes;
  }

  public DeliveryNoteSearchResult transformDeliveryNote(RemitoDto remito) {

    return DeliveryNoteSearchResult.builder()
        .deliveryNoteId(remito.getId())
        .createdDate(remito.getFechaAlta())
        .observations(remito.getObservaciones())
        .type(remito.getRemitoTipoMovimiento().getNombreTipo())
        .user(remito.getIdUsuario().getNombreUsuario())
        .origin(transformOrigin(remito))
        .destination(transformDestination(remito))
        .build();
  }

  private String transformOrigin(RemitoDto remito) {

    if (remito.getIsOrigenInterno()) {
      final DepositosDto deposito = remito.getIdOrigenInterno();

      return transformDeposito(deposito);
    }
    final PersonasDto external = remito.getIdOrigenExterno();

    return transformPersona(external);
  }

  private String transformDeposito(DepositosDto deposito) {
    return String.format(
        INTERNAL_FORMAT,
        deposito.getId(),
        deposito.getNombreDeposito(),
        deposito.getIdSucursal().getNombreSucursal());
  }

  private String transformDestination(RemitoDto remito) {

    if (remito.getIsDestinoInterno()) {
      final DepositosDto deposito = remito.getIdDestinoPrevistoInterno();

      return transformDeposito(deposito);
    }
    final PersonasDto external = remito.getIdDestinoPrevistoExterno();

    return transformPersona(external);
  }

  private String transformPersona(PersonasDto external) {
    return String.format(EXTERNAL_FORMAT, external.getId(), external.getRazonSocial());
  }

  public List<DeliveryNoteDetail> transformDeliveryNoteDetails(List<RemitoDetalleDto> detalleList) {
    List<DeliveryNoteDetail> details = new ArrayList<>(detalleList.size());
    for (RemitoDetalleDto remitoDetalleDto : detalleList) {
      final ProductosDto producto = remitoDetalleDto.getIdProducto();
      details.add(
          DeliveryNoteDetail.builder()
              .detailId(remitoDetalleDto.getId())
              .productId(producto.getId())
              .quantity(remitoDetalleDto.getCantidad())
              .saleUnit(producto.getIdTipoUnidadVenta().getNombreUnidad())
              .product(
                  String.format("[%s] %s", producto.getCodigoPropio(), producto.getDescripcion()))
              .build());
    }

    return details;
  }
}
