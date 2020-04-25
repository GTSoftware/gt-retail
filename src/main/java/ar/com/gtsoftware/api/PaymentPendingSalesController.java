package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface PaymentPendingSalesController {

    @PostMapping(path = "/sales/pending-payment")
    PaginatedResponse<PaymentPendingSale> findPendingSalesBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<ComprobantesSearchFilter> request);
}
