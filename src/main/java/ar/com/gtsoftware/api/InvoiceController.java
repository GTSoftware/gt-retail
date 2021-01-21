package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.InvoiceRequest;
import ar.com.gtsoftware.api.response.InvoiceResponse;
import ar.com.gtsoftware.api.response.PointOfSale;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface InvoiceController {

  @GetMapping(path = "/points-of-sale")
  List<PointOfSale> getPointsOfSale();

  @PostMapping(path = "/invoice")
  InvoiceResponse invoiceSale(@Valid @RequestBody InvoiceRequest request);
}
