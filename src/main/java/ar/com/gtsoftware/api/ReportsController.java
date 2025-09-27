package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.SoldProduct;
import ar.com.gtsoftware.search.reportes.ReporteVentasSearchFilter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReportsController {

  @PostMapping(path = "/reports/sold-products")
  List<SoldProduct> getSoldProductsReport(@Valid @RequestBody ReporteVentasSearchFilter filter);
}
