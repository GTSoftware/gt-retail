package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PaymentPendingSalesController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SalesToPayRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.api.transformer.fromDomain.PaymentPendingSaleTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.BancosService;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.NoExtraCostPaymentMethodsService;
import ar.com.gtsoftware.service.PaymentsService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@RestController
public class PaymentPendingSalesControllerImpl implements PaymentPendingSalesController {
    private static final String DISPLAY_NAME_FORMAT = "[%s] %s";
    private final SecurityUtils securityUtils;
    private final ComprobantesService comprobantesService;
    private final PaymentsService paymentsService;
    private final PaymentPendingSaleTransformer pendingSaleTransformer;
    private final BancosService bancosService;
    private final NoExtraCostPaymentMethodsService noExtraCostPaymentMethodsService;

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

        final List<SaleToPayDto> salesToPay = preparedPaymentDto.getSalesToPay();
        return PrepareToPayResponse.builder()
                .customer(transformCustomer(preparedPaymentDto.getCustomer()))
                .salesToPay(transformSalesToPay(salesToPay))
                .banks(getBanks(salesToPay))
                .noExtraCostPaymentMethods(getNoExtraCostPaymentMethods(salesToPay))
                .build();
    }

    private List<PaymentMethod> getNoExtraCostPaymentMethods(List<SaleToPayDto> salesToPay) {
        final Optional<SaleToPayDto> undefinedPayment = salesToPay.stream()
                .filter(sale -> isNull(sale.getPayment()))
                .findAny();
        List<PaymentMethod> noExtraCostPaymentMethods = Collections.emptyList();

        if (undefinedPayment.isPresent()) {
            noExtraCostPaymentMethods = noExtraCostPaymentMethodsService.getNoExtraCostPaymentMethods();
        }

        return noExtraCostPaymentMethods;
    }

    private List<Bank> getBanks(List<SaleToPayDto> salesToPay) {
        final Optional<SaleToPayDto> cheque = salesToPay.stream()
                .filter(sale -> nonNull(sale.getPayment())
                        && Objects.equals(sale.getPayment().getIdFormaPago().getId(), 4L))
                .findAny();
        List<Bank> banks = Collections.emptyList();

        if (cheque.isPresent()) {
            final List<BancosDto> bancos = bancosService.findAll();
            banks = new ArrayList<>(bancos.size());
            banks.addAll(
                    bancos.stream().map(bank ->
                            Bank.builder()
                                    .bankId(bank.getId())
                                    .bankName(bank.getRazonSocial())
                                    .build()
                    ).collect(Collectors.toUnmodifiableList()));
        }

        return banks;
    }

    private String transformCustomer(PersonasDto customer) {
        return String.format(DISPLAY_NAME_FORMAT, customer.getDocumento(), customer.getRazonSocial());
    }

    private List<SaleToPay> transformSalesToPay(List<SaleToPayDto> salesToPayDto) {
        final List<SaleToPay> salesToPay = new ArrayList<>(salesToPayDto.size());
        salesToPayDto.forEach(saleToPayDto ->
                salesToPay.add(
                        buildSaleToPay(saleToPayDto)
                ));

        return salesToPay;
    }

    private SaleToPay buildSaleToPay(SaleToPayDto saleToPayDto) {

        final SaleToPay saleToPay = SaleToPay.builder()
                .maxPayment(saleToPayDto.getMaxAllowedPayment())
                .minPayment(saleToPayDto.getMinAllowedPayment())
                .editableAmount(saleToPayDto.isEditableAmount())
                .totalPayment(saleToPayDto.getTotalPayment())
                .saleId(saleToPayDto.getSale().getId())
                .build();

        final ComprobantesPagosDto payment = saleToPayDto.getPayment();
        if (nonNull(payment)) {
            saleToPay.setPaymentId(payment.getId());
            saleToPay.setPaymentMethodDescription(payment.getIdFormaPago().getNombreFormaPago());
            saleToPay.setPaymentPlan(transformPaymentPlan(saleToPayDto));
            saleToPay.setPaymentMethodId(payment.getIdFormaPago().getId());
        }

        return saleToPay;
    }

    private String transformPaymentPlan(SaleToPayDto saleToPayDto) {
        final NegocioPlanesPagoDto idPlan = saleToPayDto.getPayment().getIdPlan();
        if (nonNull(idPlan)) {
            return String.format("%s en %d pago/s",
                    idPlan.getNombre(),
                    saleToPayDto.getPayment().getIdDetallePlan().getCuotas());
        }

        return StringUtils.EMPTY;
    }

}
