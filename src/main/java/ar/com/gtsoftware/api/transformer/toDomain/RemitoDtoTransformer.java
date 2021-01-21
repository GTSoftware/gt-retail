package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.AddDeliveryNoteRequest;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemitoDtoTransformer {

  private final SecurityUtils securityUtils;
  private final RemitoDetalleDtoTransformer remitoDetalleTransformer;

  public RemitoDto transformAddDeliveryNote(AddDeliveryNoteRequest request) {
    final RemitoDto remitoDto =
        RemitoDto.builder()
            .idUsuario(UsuariosDto.builder().id(securityUtils.getUserDetails().getId()).build())
            .observaciones(request.getObservations())
            .remitoTipoMovimiento(
                RemitoTipoMovimientoDto.builder().id(request.getDeliveryTypeId()).build())
            .build();

    if (request.getExternalDestinationId() != null) {
      remitoDto.setIdDestinoPrevistoExterno(
          PersonasDto.builder().id(request.getExternalDestinationId()).build());
      remitoDto.setIsDestinoInterno(false);
    }
    if (request.getExternalOriginId() != null) {
      remitoDto.setIdOrigenExterno(PersonasDto.builder().id(request.getExternalOriginId()).build());
      remitoDto.setIsOrigenInterno(false);
    }
    if (request.getInternalDestinationId() != null) {
      remitoDto.setIdDestinoPrevistoInterno(
          DepositosDto.builder().id(request.getInternalDestinationId()).build());
      remitoDto.setIsDestinoInterno(true);
    }
    if (request.getInternalOriginId() != null) {
      remitoDto.setIdOrigenInterno(
          DepositosDto.builder().id(request.getInternalOriginId()).build());
      remitoDto.setIsOrigenInterno(true);
    }

    remitoDto.setDetalleList(
        remitoDetalleTransformer.transformDeliveryItems(request.getDeliveryNoteItems()));

    return remitoDto;
  }
}
