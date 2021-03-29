package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.PriceList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface PriceListController {
  @GetMapping(path = "/products/price-list")
  List<PriceList> getPriceLists();
}
