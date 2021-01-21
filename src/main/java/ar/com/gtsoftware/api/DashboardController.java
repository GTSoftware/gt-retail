package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.SaleReport;
import ar.com.gtsoftware.api.response.StockBreak;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface DashboardController {

  @GetMapping(path = "/dashboard/sales-quantity")
  Integer getSalesQuantity();

  @GetMapping(path = "/dashboard/new-customers")
  Integer getNewCustomersQuantity();

  @GetMapping(path = "/dashboard/monthly-sales")
  List<SaleReport> getMonthlySalesReport();

  @GetMapping(path = "/dashboard/stock-break")
  List<StockBreak> getProductsWithStockBreak();
}
