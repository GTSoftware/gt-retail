package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SalesToPayRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.api.response.PrepareToPayResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.PaymentPendingSaleTransformer;
import ar.com.gtsoftware.auth.JwtUserDetails;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.ComprobantesPagosDto;
import ar.com.gtsoftware.dto.domain.NegocioFormasPagoDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.PaymentsService;
import ar.com.gtsoftware.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class PaymentPendingSalesControllerImplTest {

    private PaymentPendingSalesControllerImpl controller;

    @Mock
    private SecurityUtils mockSecurityUtils;
    @Mock
    private ComprobantesService mockComprobantesService;
    @Mock
    private PaymentPendingSaleTransformer mockPaymentPendingSaleTransformer;
    @Mock
    private PaymentsService mockPaymentsService;


    @BeforeEach
    void setUp() {
        initMocks(this);
        controller = new PaymentPendingSalesControllerImpl(
                mockSecurityUtils,
                mockComprobantesService,
                mockPaymentsService,
                mockPaymentPendingSaleTransformer);

        when(mockSecurityUtils.getUserDetails()).thenReturn(JwtUserDetails.builder().sucursalId(3L).build());
        when(mockComprobantesService.countBySearchFilter(any())).thenReturn(1);
        when(mockPaymentPendingSaleTransformer.transformSales(any())).thenReturn(Collections.singletonList(
                PaymentPendingSale.builder().saleId(1L).build()));
    }

    @Test
    void shouldSetBranchIdWhenRoleIsNotAdmin() {
        when(mockSecurityUtils.userHasRole(Roles.ADMINISTRADORES)).thenReturn(false);

        final PaginatedSearchRequest<ComprobantesSearchFilter> sf = new PaginatedSearchRequest<>();
        final ComprobantesSearchFilter searchFilter = new ComprobantesSearchFilter();
        sf.setSearchFilter(searchFilter);
        sf.setFirstResult(0);
        sf.setMaxResults(1);
        final PaginatedResponse<PaymentPendingSale> paginatedResponse = controller.findBySearchFilter(sf);

        assertThat(paginatedResponse, is(notNullValue()));
        assertThat(paginatedResponse.getData(), is(notNullValue()));
        assertThat(paginatedResponse.getData().get(0).getSaleId(), is(1L));

        assertThat(searchFilter.getConSaldo(), is(true));
        assertThat(searchFilter.getIdSucursal(), is(3L));
    }

    @Test
    void shouldFindBySearchFilterWhenUserRoleIsAdmin() {
        when(mockSecurityUtils.userHasRole(Roles.ADMINISTRADORES)).thenReturn(true);

        final PaginatedSearchRequest<ComprobantesSearchFilter> sf = new PaginatedSearchRequest<>();
        final ComprobantesSearchFilter searchFilter = new ComprobantesSearchFilter();
        sf.setSearchFilter(searchFilter);
        sf.setFirstResult(0);
        sf.setMaxResults(1);
        final PaginatedResponse<PaymentPendingSale> paginatedResponse = controller.findBySearchFilter(sf);

        assertThat(paginatedResponse, is(notNullValue()));
        assertThat(paginatedResponse.getData(), is(notNullValue()));
        assertThat(paginatedResponse.getData().get(0).getSaleId(), is(1L));

        assertThat(searchFilter.getConSaldo(), is(true));
        assertThat(searchFilter.getIdSucursal(), is(nullValue()));
    }

    @Test
    void shouldPrepareToPay() {
        SaleToPayDto saleToPayDto = SaleToPayDto.builder()
                .totalPayment(BigDecimal.TEN)
                .payment(ComprobantesPagosDto.builder()
                        .idFormaPago(NegocioFormasPagoDto.builder()
                                .nombreFormaPago("EFECTIVO")
                                .id(1L)
                                .build())
                        .build())
                .sale(ComprobantesDto.builder()
                        .id(1L)
                        .build())
                .build();
        PreparedPaymentDto preparedPaymentDto = PreparedPaymentDto.builder()
                .customer(PersonasDto.builder().id(1L)
                        .documento("1234")
                        .razonSocial("Test, Test")
                        .build())
                .salesToPay(Collections.singletonList(saleToPayDto))
                .build();
        final List<Long> salesIds = Collections.singletonList(1L);
        when(mockPaymentsService.prepareToPay(salesIds)).thenReturn(preparedPaymentDto);

        SalesToPayRequest request = new SalesToPayRequest();
        request.setSalesIds(salesIds);
        final PrepareToPayResponse response = controller.prepareToPay(request);

        assertThat(response, is(notNullValue()));
        assertThat(response.getSalesToPay().size(), is(1));
        assertThat(response.getCustomer(), is("[1234] Test, Test"));

        verify(mockPaymentsService).prepareToPay(salesIds);
    }

}