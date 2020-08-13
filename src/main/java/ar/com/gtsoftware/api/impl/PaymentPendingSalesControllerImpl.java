package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PaymentPendingSalesController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SalesToPayRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.api.transformer.fromDomain.PaymentPendingSaleTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.BancosDto;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.BancosService;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.PaymentsService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class PaymentPendingSalesControllerImpl implements PaymentPendingSalesController {
    private static final String DISPLAY_NAME_FORMAT = "[%s] %s";
    private final SecurityUtils securityUtils;
    private final ComprobantesService comprobantesService;
    private final PaymentsService paymentsService;
    private final PaymentPendingSaleTransformer pendingSaleTransformer;
    private final BancosService bancosService;

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
                .banks(getBanks(preparedPaymentDto.getSalesToPay()))
                .build();
    }

    private List<Bank> getBanks(List<SaleToPayDto> salesToPay) {
        final Optional<SaleToPayDto> cheque = salesToPay.stream()
                .filter(s -> s.getPayment().getIdFormaPago().getId().equals(4L))
                .findAny();
        if (cheque.isPresent()) {
            final List<BancosDto> bancos = bancosService.findAll();
            final List<Bank> banks = new ArrayList<>(bancos.size());
            bancos.forEach(b -> banks.add(
                    Bank.builder()
                            .bankId(b.getId())
                            .bankName(b.getRazonSocial())
                            .build()
            ));

            return banks;
        }

        return Collections.emptyList();
    }

    private String transformCustomer(PersonasDto customer) {
        return String.format(DISPLAY_NAME_FORMAT, customer.getDocumento(), customer.getRazonSocial());
    }

    private List<SaleToPay> transformSalesToPay(List<SaleToPayDto> salesToPayDto) {
        final List<SaleToPay> salesToPay = new ArrayList<>(salesToPayDto.size());
        salesToPayDto.forEach(saleToPayDto ->
                salesToPay.add(
                        SaleToPay.builder()
                                .maxPayment(saleToPayDto.getMaxAllowedPayment())
                                .minPayment(saleToPayDto.getMinAllowedPayment())
                                .editableAmount(saleToPayDto.isEditableAmount())
                                .paymentId(saleToPayDto.getPayment().getId())
                                .paymentMethodDescription(saleToPayDto.getPayment().getIdFormaPago().getNombreFormaPago())
                                .paymentPlan(transformPaymentPlan(saleToPayDto))
                                .totalPayment(saleToPayDto.getTotalPayment())
                                .saleId(saleToPayDto.getSale().getId())
                                .paymentMethodId(saleToPayDto.getPayment().getIdFormaPago().getId())
                                .build()
                ));

        return salesToPay;
    }

    private String transformPaymentPlan(SaleToPayDto saleToPayDto) {
        final NegocioPlanesPagoDto idPlan = saleToPayDto.getPayment().getIdPlan();
        if (idPlan != null) {
            return String.format("%s en %d pago/s",
                    idPlan.getNombre(),
                    saleToPayDto.getPayment().getIdDetallePlan().getCuotas());
        }

        return StringUtils.EMPTY;
    }

}
