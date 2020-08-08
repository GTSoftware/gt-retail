package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SalesToPayRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.api.response.PrepareToPayResponse;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface PaymentPendingSalesController {

    @PostMapping(path = "/sales/payment-pending")
    PaginatedResponse<PaymentPendingSale> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<ComprobantesSearchFilter> request);

    @PostMapping(path = "/sales/prepare-to-pay")
    PrepareToPayResponse prepareToPay(@Valid @RequestBody SalesToPayRequest salesToPayRequest);
}
