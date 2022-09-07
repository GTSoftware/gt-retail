package ar.com.gtsoftware.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PrintController {

  @GetMapping(path = "/downloads/budget")
  void getSaleBudget(@RequestParam Long saleId);

  @GetMapping(path = "/downloads/invoice")
  void getInvoice(
      @RequestParam Long saleId, @RequestParam(required = false) PrintFormat printFormat);

  @GetMapping(path = "/downloads/deliveryNote")
  void getDeliveryNote(@RequestParam Long deliveryNoteId);
}
