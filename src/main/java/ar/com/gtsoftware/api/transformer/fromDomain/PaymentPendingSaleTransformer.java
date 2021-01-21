package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentPendingSaleTransformer {

  public List<PaymentPendingSale> transformSales(List<ComprobantesDto> comprobantesDtos) {
    List<PaymentPendingSale> results = new ArrayList<>(comprobantesDtos.size());
    for (ComprobantesDto comprobantesDto : comprobantesDtos) {
      results.add(transformSale(comprobantesDto));
    }
    return results;
  }

  public PaymentPendingSale transformSale(ComprobantesDto comprobantesDto) {
    return PaymentPendingSale.builder()
        .saleId(comprobantesDto.getId())
        .branch(comprobantesDto.getIdSucursal().getNombreSucursal())
        .remainingAmount(comprobantesDto.getSaldo())
        .total(comprobantesDto.getTotal())
        .saleDate(comprobantesDto.getFechaComprobante())
        .user(comprobantesDto.getIdUsuario().getNombreUsuario())
        .saleType(comprobantesDto.getTipoComprobante().getNombreComprobante())
        .saleCondition(comprobantesDto.getIdCondicionComprobante().getNombreCondicion())
        .customer(comprobantesDto.getIdPersona().getRazonSocial())
        .customerId(comprobantesDto.getIdPersona().getId())
        .invoiceNumber(transformInvoiceNumber(comprobantesDto))
        .build();
  }

  private String transformInvoiceNumber(ComprobantesDto comprobantesDto) {
    final FiscalLibroIvaVentasDto regFiscal = comprobantesDto.getIdRegistro();
    if (regFiscal != null) {
      return String.format(
          "%s %s-%s",
          comprobantesDto.getLetra(),
          regFiscal.getPuntoVentaFactura(),
          regFiscal.getNumeroFactura());
    }

    return null;
  }
}
