package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.cashbox.CloseBoxRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.cashbox.CashBox;
import ar.com.gtsoftware.api.response.cashbox.CashMovementSearchResult;
import ar.com.gtsoftware.search.CajasMovimientosSearchFilter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CashBoxesController {
  @GetMapping(path = "/cashboxes/my-box")
  CashBox getCurrentUserCashBox();

  @PostMapping(path = "/cashboxes/movements/search")
  PaginatedResponse<CashMovementSearchResult> searchMovements(
      @Valid @RequestBody PaginatedSearchRequest<CajasMovimientosSearchFilter> searchRequest);

  @PostMapping(path = "/cashboxes/my-box/open")
  CashBox openBox();

  @PostMapping(path = "/cashboxes/my-box/close")
  void closeBox(CloseBoxRequest closeBoxRequest);
}
