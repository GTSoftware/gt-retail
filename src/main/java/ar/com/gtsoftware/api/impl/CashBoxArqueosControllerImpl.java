package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.CashBoxArqueosController;
import ar.com.gtsoftware.api.exception.CashBoxNotFoundException;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.cashbox.ControlArqueoRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.cashbox.CashBoxArqueo;
import ar.com.gtsoftware.api.response.cashbox.CashBoxArqueoDetail;
import ar.com.gtsoftware.api.response.cashbox.CashBoxArqueoSearchResult;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.CajasArqueosDto;
import ar.com.gtsoftware.search.CajasArqueosSearchFilter;
import ar.com.gtsoftware.service.CajasArqueosService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional
public class CashBoxArqueosControllerImpl implements CashBoxArqueosController {

  private final CajasArqueosService arqueosService;
  private final SecurityUtils securityUtils;
  private final BusinessDateUtils dateUtils;

  private static final ArqueoSearchResultTransformer searchResultTransformer =
      new ArqueoSearchResultTransformer();
  private static final ArqueoTransformer arqueoTransformer = new ArqueoTransformer();

  @Override
  public PaginatedResponse<CashBoxArqueoSearchResult> search(
      PaginatedSearchRequest<CajasArqueosSearchFilter> searchRequest) {
    return PaginatedResponseBuilder.build(arqueosService, searchRequest, searchResultTransformer);
  }

  @Override
  public CashBoxArqueo getById(Long id) {
    var arqueoDto = arqueosService.find(id);
    if (arqueoDto == null) {
      throw new CashBoxNotFoundException("Arqueo no encontrado");
    }
    return arqueoTransformer.transform(arqueoDto);
  }

  @Override
  public void controlArqueo(Long id, ControlArqueoRequest controlRequest) {
    var user = securityUtils.getCurrentUser();
    arqueosService.controlar(id, user, dateUtils.getCurrentDateTime(), controlRequest.notes());
  }

  private static class ArqueoSearchResultTransformer
      implements Transformer<CajasArqueosDto, CashBoxArqueoSearchResult> {
    @Override
    public CashBoxArqueoSearchResult transform(CajasArqueosDto from) {
      return new CashBoxArqueoSearchResult(
          from.getId(),
          from.getIdUsuario().getId(),
          from.getIdUsuario().getNombreUsuario(),
          from.getIdSucursal().getId(),
          from.getIdCaja().getId(),
          from.getFechaArqueo(),
          from.getSaldoInicial(),
          from.getSaldoFinal(),
          from.getFechaControl() != null);
    }
  }

  private static class ArqueoTransformer implements Transformer<CajasArqueosDto, CashBoxArqueo> {
    @Override
    public CashBoxArqueo transform(CajasArqueosDto from) {
      var details =
          Optional.ofNullable(from.getDetalleArqueo()).orElse(Collections.emptyList()).stream()
              .map(
                  d ->
                      new CashBoxArqueoDetail(
                          d.getId(),
                          d.getIdFormaPago().getId(),
                          d.getIdFormaPago().getNombreFormaPago(),
                          d.getMontoSistema(),
                          d.getMontoDeclarado(),
                          d.getDiferencia(),
                          d.getDescargo()))
              .toList();

      return new CashBoxArqueo(
          from.getId(),
          from.getIdUsuario().getId(),
          from.getIdUsuario().getNombreUsuario(),
          from.getIdUsuarioControl() != null ? from.getIdUsuarioControl().getId() : null,
          from.getIdUsuarioControl() != null ? from.getIdUsuarioControl().getNombreUsuario() : null,
          from.getIdSucursal().getId(),
          from.getIdCaja().getId(),
          from.getFechaArqueo(),
          from.getFechaControl(),
          from.getSaldoInicial(),
          from.getSaldoFinal(),
          from.getObvervacionesControl(),
          details);
    }
  }
}
