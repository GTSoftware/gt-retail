package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.cashbox.ControlArqueoRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.cashbox.CashBoxArqueo;
import ar.com.gtsoftware.api.response.cashbox.CashBoxArqueoSearchResult;
import ar.com.gtsoftware.search.CajasArqueosSearchFilter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CashBoxArqueosController {

  @PostMapping(path = "/cashboxes/arqueos/search")
  PaginatedResponse<CashBoxArqueoSearchResult> search(
      @Valid @RequestBody PaginatedSearchRequest<CajasArqueosSearchFilter> searchRequest);

  @GetMapping(path = "/cashboxes/arqueos/{id}")
  CashBoxArqueo getById(@PathVariable("id") Long id);

  @PostMapping(path = "/cashboxes/arqueos/{id}/control")
  void controlArqueo(@PathVariable("id") Long id, @RequestBody ControlArqueoRequest controlRequest);
}
