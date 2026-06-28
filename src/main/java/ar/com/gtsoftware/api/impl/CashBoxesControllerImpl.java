package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CashBoxesController;
import ar.com.gtsoftware.api.exception.CashBoxNotFoundException;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.cashbox.CloseBoxRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.cashbox.CashBox;
import ar.com.gtsoftware.api.response.cashbox.CashMovementSearchResult;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.CajasDto;
import ar.com.gtsoftware.dto.domain.CajasMovimientosDto;
import ar.com.gtsoftware.search.CajasMovimientosSearchFilter;
import ar.com.gtsoftware.search.CajasSearchFilter;
import ar.com.gtsoftware.service.CajasMovimientosService;
import ar.com.gtsoftware.service.CajasService;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional
public class CashBoxesControllerImpl implements CashBoxesController {

  private final CajasService cajasService;
  private final SecurityUtils securityUtils;
  private final CajasMovimientosService cajasMovimientosService;
  private static final CashMovementTransformer movementTransformer = new CashMovementTransformer();

  @Override
  public CashBox getCurrentUserCashBox() {
    var user = securityUtils.getCurrentUser();
    var cajaDto = cajasService.obtenerCajaActual(user);
    if (cajaDto == null) {
      throw new CashBoxNotFoundException();
    }
    var csf = new CajasSearchFilter();
    csf.setIdCaja(cajaDto.getId());
    var currentAmount = cajasService.obtenerTotalEnCaja(csf);

    return transformCajaDtoToCashBox(cajaDto, currentAmount);
  }

  private CashBox transformCajaDtoToCashBox(CajasDto cajaDto, BigDecimal currentAmount) {
    return new CashBox(
        cajaDto.getId(),
        cajaDto.getIdSucursal().getId(),
        cajaDto.getFechaApertura(),
        cajaDto.getFechaCierre(),
        cajaDto.getSaldoInicial(),
        currentAmount,
        cajaDto.getVersion());
  }

  @Override
  public PaginatedResponse<CashMovementSearchResult> searchMovements(
      PaginatedSearchRequest<CajasMovimientosSearchFilter> searchRequest) {
    // TODO check permissions etc.
    return PaginatedResponseBuilder.build(
        cajasMovimientosService, searchRequest, movementTransformer);
  }

  @Override
  public CashBox openBox() {
    var user = securityUtils.getCurrentUser();

    var cajaDto = cajasService.abrirCaja(user);
    var csf = new CajasSearchFilter();
    csf.setIdCaja(cajaDto.getId());
    var currentAmount = cajasService.obtenerTotalEnCaja(csf);

    return transformCajaDtoToCashBox(cajaDto, currentAmount);
  }

  @Override
  public void closeBox(CloseBoxRequest closeBoxRequest) {
    //TODO isn't this the arqueo?
    var user = securityUtils.getCurrentUser();

    var currentBox = cajasService.obtenerCajaActual(user);
    if (currentBox == null) {
      throw new CashBoxNotFoundException("El usuario no posee una caja abierta");
    }
    cajasService.cerrarCaja(currentBox.getId());
  }
}

class CashMovementTransformer
    implements Transformer<CajasMovimientosDto, CashMovementSearchResult> {
  @Override
  public CashMovementSearchResult transform(CajasMovimientosDto from) {
    return new CashMovementSearchResult(
        from.getId(),
        from.getIdCaja().getId(),
        from.getFechaMovimiento(),
        from.getMontoMovimiento(),
        from.getDescripcion());
  }
}
