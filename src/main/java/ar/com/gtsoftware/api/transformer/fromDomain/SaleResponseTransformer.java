package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.SaleDetail;
import ar.com.gtsoftware.api.response.SaleResponse;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.ComprobantesLineasDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SaleResponseTransformer {

  public SaleResponse transformSale(ComprobantesDto comprobante) {
    return SaleResponse.builder()
        .customer(
            String.format(
                "[%s] %s",
                comprobante.getIdPersona().getDocumento(),
                comprobante.getIdPersona().getRazonSocial()))
        .branch(comprobante.getIdSucursal().getNombreSucursal())
        .remainingAmount(comprobante.getSaldo())
        .total(comprobante.getTotal())
        .saleDate(comprobante.getFechaComprobante())
        .saleId(comprobante.getId())
        .saleType(comprobante.getTipoComprobante().getNombreComprobante())
        .saleCondition(comprobante.getIdCondicionComprobante().getNombreCondicion())
        .invoiceNumber(transformInvoice(comprobante.getIdRegistro()))
        .user(comprobante.getIdUsuario().getNombreUsuario())
        .details(transformDetails(comprobante.getComprobantesLineasList()))
        .observations(comprobante.getObservaciones())
        .remitente(comprobante.getRemitente())
        .remito(comprobante.getNroRemito())
        .invoiceChar(comprobante.getLetra())
        .build();
  }

  private List<SaleDetail> transformDetails(List<ComprobantesLineasDto> comprobantesLineasList) {
    List<SaleDetail> saleDetails = new ArrayList<>(comprobantesLineasList.size());
    for (ComprobantesLineasDto linea : comprobantesLineasList) {
      saleDetails.add(
          SaleDetail.builder()
              .description(linea.getDescripcion())
              .productCode(linea.getIdProducto().getCodigoPropio())
              .quantity(linea.getCantidad())
              .subTotal(linea.getSubTotal())
              .saleUnit(linea.getIdProducto().getIdTipoUnidadVenta().getNombreUnidad())
              .build());
    }

    return saleDetails;
  }

  private String transformInvoice(FiscalLibroIvaVentasDto idRegistro) {
    if (idRegistro == null) {
      return null;
    }

    return String.format(
        "%s %s-%s",
        idRegistro.getLetraFactura(),
        idRegistro.getPuntoVentaFactura(),
        idRegistro.getNumeroFactura());
  }
}
