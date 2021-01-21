package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.request.SaleSearchResult;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SaleSearchResultTransformer {

  public List<SaleSearchResult> transformSales(List<ComprobantesDto> comprobantesDtos) {
    List<SaleSearchResult> results = new ArrayList<>(comprobantesDtos.size());
    for (ComprobantesDto comprobantesDto : comprobantesDtos) {
      results.add(transformSale(comprobantesDto));
    }
    return results;
  }

  public SaleSearchResult transformSale(ComprobantesDto comprobantesDto) {
    return SaleSearchResult.builder()
        .saleId(comprobantesDto.getId())
        .branch(comprobantesDto.getIdSucursal().getNombreSucursal())
        .remainingAmount(comprobantesDto.getSaldo())
        .total(comprobantesDto.getTotal())
        .saleDate(comprobantesDto.getFechaComprobante())
        .user(comprobantesDto.getIdUsuario().getNombreUsuario())
        .saleType(comprobantesDto.getTipoComprobante().getNombreComprobante())
        .saleCondition(comprobantesDto.getIdCondicionComprobante().getNombreCondicion())
        .customer(comprobantesDto.getIdPersona().getRazonSocial())
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
