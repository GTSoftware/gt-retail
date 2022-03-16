package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.DigitalFiscalBookRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FiscalBookController {

  @PostMapping(path = "/fiscal/digital-book")
  void getInformativeRegime(@Valid @RequestBody DigitalFiscalBookRequest filter);

  @PostMapping(path = "/fiscal/book")
  void getFiscalBook(@Valid @RequestBody DigitalFiscalBookRequest filter);
}
