package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ReportsController;
import ar.com.gtsoftware.api.response.SoldProduct;
import ar.com.gtsoftware.dto.reportes.VentaPorProducto;
import ar.com.gtsoftware.dto.reportes.VentaPorProductoReport;
import ar.com.gtsoftware.search.reportes.ReporteVentasSearchFilter;
import ar.com.gtsoftware.service.ReportesVentaService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReportsControllerImpl implements ReportsController {

  private final ReportesVentaService service;

  @Override
  public List<SoldProduct> getSoldProductsReport(@Valid ReporteVentasSearchFilter filter) {
    filter.setPageSize(Integer.MAX_VALUE);
    final VentaPorProductoReport report = service.obtenerReporte(filter);

    return transformSoldProducts(report);
  }

  private List<SoldProduct> transformSoldProducts(VentaPorProductoReport report) {
    List<SoldProduct> soldProducts = new ArrayList<>(report.getTotalRows());
    for (VentaPorProducto row : report.getPageRows()) {
      soldProducts.add(
          SoldProduct.builder()
              .productId(row.getIdProducto())
              .minimumStock(row.getStockMinimo())
              .productCode(row.getCodigoProducto())
              .productDescription(row.getDescripcionProducto())
              .salePriceTotal(row.getTotalAPrecioVenta())
              .saleUnit(row.getUnidadVenta())
              .soldQuantity(row.getCantidadVendida())
              .supplier(row.getProveedor())
              .supplierCode(row.getCodigoFabrica())
              .totalSales(row.getCantidadComprobantes())
              .totalSalesCost(row.getCostoTotalVentas())
              .totalStock(row.getStockTotalActual())
              .build());
    }

    return soldProducts;
  }
}
