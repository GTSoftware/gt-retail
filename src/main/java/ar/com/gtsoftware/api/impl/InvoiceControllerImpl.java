package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.InvoiceController;
import ar.com.gtsoftware.api.exception.PointOfSaleNotFoundException;
import ar.com.gtsoftware.api.exception.SaleNotFoundException;
import ar.com.gtsoftware.api.idempotence.IdempotenceHandler;
import ar.com.gtsoftware.api.request.InvoiceRequest;
import ar.com.gtsoftware.api.response.InvoiceResponse;
import ar.com.gtsoftware.api.response.PointOfSale;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import ar.com.gtsoftware.dto.domain.FiscalPuntosVentaDto;
import ar.com.gtsoftware.enums.TiposPuntosVenta;
import ar.com.gtsoftware.search.FiscalPuntosVentaSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.FacturacionVentasService;
import ar.com.gtsoftware.service.FiscalPuntosVentaService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvoiceControllerImpl implements InvoiceController {

  private final SecurityUtils securityUtils;
  private final FiscalPuntosVentaService puntosVentaService;
  private final ComprobantesService comprobantesService;
  private final FacturacionVentasService facturacionService;

  private final IdempotenceHandler idempotenceHandler;

  @Override
  public List<PointOfSale> getPointsOfSale() {
    FiscalPuntosVentaSearchFilter sf =
        FiscalPuntosVentaSearchFilter.builder()
            .activo(true)
            .idSucursal(securityUtils.getUserDetails().getSucursalId())
            .tiposPuntoVenta(Arrays.asList(TiposPuntosVenta.ELECTRONICO, TiposPuntosVenta.MANUAL))
            .build();

    final List<FiscalPuntosVentaDto> puntosVenta = puntosVentaService.findAllBySearchFilter(sf);

    return transformPointsOfSale(puntosVenta);
  }

  private List<PointOfSale> transformPointsOfSale(List<FiscalPuntosVentaDto> puntosVenta) {
    return puntosVenta.stream().map(this::buildPointOfSale).collect(Collectors.toList());
  }

  private PointOfSale buildPointOfSale(FiscalPuntosVentaDto fiscalPuntosVentaDto) {
    return PointOfSale.builder()
        .posNumber(fiscalPuntosVentaDto.getNroPuntoVenta())
        .posType(fiscalPuntosVentaDto.getTipo().name())
        .byDefault(TiposPuntosVenta.ELECTRONICO == fiscalPuntosVentaDto.getTipo())
        .displayName(
            String.format(
                "[%s-%s] %s",
                fiscalPuntosVentaDto.getNroPuntoVenta(),
                fiscalPuntosVentaDto.getTipo().name(),
                fiscalPuntosVentaDto.getDescripcion()))
        .build();
  }

  @Override
  public InvoiceResponse invoiceSale(@Valid InvoiceRequest request) {
    idempotenceHandler.verifyIdempotence(request.getInvoiceRequestId());

    final ComprobantesDto comprobantesDto = comprobantesService.find(request.getSaleId());
    final FiscalPuntosVentaDto puntosVentaDto = puntosVentaService.find(request.getPointOfSale());

    if (Objects.isNull(comprobantesDto)) {
      throw new SaleNotFoundException();
    }
    if (Objects.isNull(puntosVentaDto)) {
      throw new PointOfSaleNotFoundException();
    }

    try {
      facturacionService.registrarFacturaVenta(comprobantesDto.getId(), puntosVentaDto, 0, null);
    } catch (ServiceException e) {
      throw new RuntimeException(e);
    }

    final ComprobantesDto comprobanteFacturado = comprobantesService.find(request.getSaleId());
    final FiscalLibroIvaVentasDto idRegistro = comprobanteFacturado.getIdRegistro();

    idempotenceHandler.setNonce(
        request.getInvoiceRequestId(), comprobanteFacturado.getId().toString());

    return InvoiceResponse.builder()
        .invoiceNumber(
            String.format(
                "%s %s-%s",
                idRegistro.getLetraFactura(),
                idRegistro.getPuntoVentaFactura(),
                idRegistro.getNumeroFactura()))
        .build();
  }
}
