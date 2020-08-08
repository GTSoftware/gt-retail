package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PaymentPendingSalesController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SalesToPayRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.api.response.PrepareToPayResponse;
import ar.com.gtsoftware.api.response.SaleToPay;
import ar.com.gtsoftware.api.transformer.fromDomain.PaymentPendingSaleTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.PaymentsService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PaymentPendingSalesControllerImpl implements PaymentPendingSalesController {
    private static final String DISPLAY_NAME_FORMAT = "[%s] %s";
    private final SecurityUtils securityUtils;
    private final ComprobantesService comprobantesService;
    private final PaymentsService paymentsService;
    private final PaymentPendingSaleTransformer pendingSaleTransformer;

    @Override
    public PaginatedResponse<PaymentPendingSale> findBySearchFilter(@Valid PaginatedSearchRequest<ComprobantesSearchFilter> request) {
        final ComprobantesSearchFilter searchFilter = request.getSearchFilter();
        searchFilter.setConSaldo(true);
        searchFilter.addSortField("fechaComprobante", false);
        if (!securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
            searchFilter.setIdSucursal(securityUtils.getUserDetails().getSucursalId());
        }

        final int count = comprobantesService.countBySearchFilter(searchFilter);
        final PaginatedResponse<PaymentPendingSale> response = PaginatedResponse.<PaymentPendingSale>builder()
                .totalResults(count).build();

        if (count > 0) {
            final List<ComprobantesDto> comprobantes = comprobantesService.findBySearchFilter(searchFilter,
                    request.getFirstResult(),
                    request.getMaxResults());
            response.setData(pendingSaleTransformer.transformSales(comprobantes));
        }

        return response;
    }

    @Override
    public PrepareToPayResponse prepareToPay(@Valid SalesToPayRequest salesToPayRequest) {
        final List<Long> salesIds = salesToPayRequest.getSalesIds();

        final PreparedPaymentDto preparedPaymentDto = paymentsService.prepareToPay(salesIds);

        return PrepareToPayResponse.builder()
                .customer(transformCustomer(preparedPaymentDto.getCustomer()))
                .salesToPay(transformSalesToPay(preparedPaymentDto.getSalesToPay()))
                .build();
    }

    private String transformCustomer(PersonasDto customer) {
        return String.format(DISPLAY_NAME_FORMAT, customer.getDocumento(), customer.getRazonSocial());
    }

    private List<SaleToPay> transformSalesToPay(List<SaleToPayDto> salesToPayDto) {
        List<SaleToPay> salesToPay = new ArrayList<>(salesToPayDto.size());
        for (SaleToPayDto saleToPayDto : salesToPayDto) {
            salesToPay.add(
                    SaleToPay.builder()
                            .maxPayment(saleToPayDto.getMaxAllowedPayment())
                            .minPayment(saleToPayDto.getMinAllowedPayment())
                            .editableAmount(saleToPayDto.isEditableAmount())
                            .paymentId(saleToPayDto.getPayment().getId())
                            .paymentMethodDescription(saleToPayDto.getPayment().getIdFormaPago().getNombreFormaPago())
                            .totalPayment(saleToPayDto.getTotalPayment())
                            .saleId(saleToPayDto.getSale().getId())
                            .paymentMethodId(saleToPayDto.getPayment().getIdFormaPago().getId())
                            .build()
            );
        }

        return salesToPay;
    }

}
