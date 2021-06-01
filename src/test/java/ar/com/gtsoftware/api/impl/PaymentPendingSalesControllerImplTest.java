package ar.com.gtsoftware.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.api.transformer.fromDomain.PaymentPendingSaleTransformer;
import ar.com.gtsoftware.auth.JwtUserDetails;
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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentPendingSalesControllerImplTest {

  private PaymentPendingSalesControllerImpl controller;

  @Mock private SecurityUtils mockSecurityUtils;
  @Mock private ComprobantesService comprobantesService;
  @Mock private PaymentPendingSaleTransformer paymentPendingSaleTransformer;
  @Mock private PaymentsService mockPaymentsService;
  @Mock private BancosService mockBancosService;
  @Mock private NoExtraCostPaymentMethodsService mockNoExtraCostPaymentMethodsService;

  @Spy private ComprobantesSearchFilter searchFilter;
  @Spy private PaginatedSearchRequest<ComprobantesSearchFilter> searchRequest;
  @Spy private JwtUserDetails jwtUserDetails;

  @Spy private ComprobantesDto comprobantesDto;
  @Spy private PaymentPendingSale paymentPendingSale;

  @BeforeEach
  void setUp() {
    controller =
        new PaymentPendingSalesControllerImpl(
            mockSecurityUtils,
            comprobantesService,
            mockPaymentsService,
            paymentPendingSaleTransformer,
            mockBancosService,
            mockNoExtraCostPaymentMethodsService);
  }

  @Test
  void shouldSetBranchIdWhenRoleIsNotAdmin() {
    when(mockSecurityUtils.userHasRole(Roles.ADMINISTRADORES)).thenReturn(false);
    when(mockSecurityUtils.getUserDetails()).thenReturn(jwtUserDetails);
    when(searchRequest.getSearchFilter()).thenReturn(searchFilter);
    when(jwtUserDetails.getSucursalId()).thenReturn(3L);

    controller.findBySearchFilter(searchRequest);

    verify(searchFilter).setIdSucursal(3L);
  }

  @Test
  void shouldFindBySearchFilterWhenResultsExists() {
    when(mockSecurityUtils.userHasRole(Roles.ADMINISTRADORES)).thenReturn(true);
    when(searchRequest.getSearchFilter()).thenReturn(searchFilter);
    when(searchRequest.getFirstResult()).thenReturn(0);
    when(searchRequest.getMaxResults()).thenReturn(20);
    when(comprobantesService.countBySearchFilter(searchFilter)).thenReturn(1);
    when(comprobantesService.findBySearchFilter(searchFilter, 0, 20))
        .thenReturn(List.of(comprobantesDto));
    when(paymentPendingSaleTransformer.transform(List.of(comprobantesDto)))
        .thenReturn(List.of(paymentPendingSale));

    final PaginatedResponse<PaymentPendingSale> paginatedResponse =
        controller.findBySearchFilter(searchRequest);

    assertThat(paginatedResponse).isNotNull();
    assertThat(paginatedResponse.getTotalResults()).isEqualTo(1);
    assertThat(paginatedResponse.getData()).isEqualTo(List.of(paymentPendingSale));
  }

  @Test
  void shouldReturnEmptyResponseWhenNoResultsExists() {
    when(mockSecurityUtils.userHasRole(Roles.ADMINISTRADORES)).thenReturn(true);
    when(searchRequest.getSearchFilter()).thenReturn(searchFilter);
    when(comprobantesService.countBySearchFilter(searchFilter)).thenReturn(0);

    final PaginatedResponse<PaymentPendingSale> paginatedResponse =
        controller.findBySearchFilter(searchRequest);

    assertThat(paginatedResponse).isNotNull();
    assertThat(paginatedResponse.getTotalResults()).isEqualTo(0);
    assertThat(paginatedResponse.getData()).isNull();
  }
  //
  //  @Test
  //  void shouldPrepareToPayInCash() {
  //    SaleToPayDto saleToPayDto = buildDummySaleToPay("EFECTIVO", 1L);
  //    PreparedPaymentDto preparedPaymentDto = buildDummyPreparedPayment(saleToPayDto);
  //    final List<Long> salesIds = Collections.singletonList(1L);
  //    when(mockPaymentsService.prepareToPay(salesIds)).thenReturn(preparedPaymentDto);
  //
  //    SalesToPayRequest request = new SalesToPayRequest();
  //    request.setSalesIds(salesIds);
  //    final PrepareToPayResponse response = controller.prepareToPay(request);
  //
  //    assertThat(response, is(notNullValue()));
  //    assertThat(response.getSalesToPay().size(), is(1));
  //    assertThat(response.getCustomer(), is("[1234] Test, Test"));
  //    assertThat(response.getBanks().size(), is(0));
  //    assertThat(response.getSalesToPay().get(0).getPaymentPlan(), is(""));
  //
  //    verify(mockPaymentsService).prepareToPay(salesIds);
  //  }
  //
  //  @Test
  //  void shouldPrepareToPayInCreditCards() {
  //    SaleToPayDto saleToPayDto = buildDummySaleToPay("TARJETA DE CREDITO", 2L);
  //    saleToPayDto
  //        .getPayment()
  //        .setIdPlan(NegocioPlanesPagoDto.builder().nombre("MASTERCARD").build());
  //    saleToPayDto
  //        .getPayment()
  //        .setIdDetallePlan(NegocioPlanesPagoDetalleDto.builder().cuotas(6).build());
  //
  //    PreparedPaymentDto preparedPaymentDto = buildDummyPreparedPayment(saleToPayDto);
  //    final List<Long> salesIds = Collections.singletonList(1L);
  //    when(mockPaymentsService.prepareToPay(salesIds)).thenReturn(preparedPaymentDto);
  //
  //    SalesToPayRequest request = new SalesToPayRequest();
  //    request.setSalesIds(salesIds);
  //    final PrepareToPayResponse response = controller.prepareToPay(request);
  //
  //    assertThat(response, is(notNullValue()));
  //    assertThat(response.getSalesToPay().size(), is(1));
  //    assertThat(response.getCustomer(), is("[1234] Test, Test"));
  //    assertThat(response.getBanks().size(), is(0));
  //    assertThat(response.getSalesToPay().get(0).getPaymentPlan(), is("MASTERCARD en 6 pago/s"));
  //
  //    verify(mockPaymentsService).prepareToPay(salesIds);
  //  }
  //
  //  @Test
  //  void shouldAddBanksToPrepareToPayWhenChequesArePresent() {
  //    SaleToPayDto saleToPayDto = buildDummySaleToPay("CHEQUE", 4L);
  //    PreparedPaymentDto preparedPaymentDto = buildDummyPreparedPayment(saleToPayDto);
  //    final List<Long> salesIds = Collections.singletonList(1L);
  //    when(mockPaymentsService.prepareToPay(salesIds)).thenReturn(preparedPaymentDto);
  //    when(mockBancosService.findAll())
  //        .thenReturn(
  //            Collections.singletonList(BancosDto.builder().id(1L).razonSocial("Test
  // Bank").build()));
  //
  //    SalesToPayRequest request = new SalesToPayRequest();
  //    request.setSalesIds(salesIds);
  //    final PrepareToPayResponse response = controller.prepareToPay(request);
  //
  //    assertThat(response, is(notNullValue()));
  //    assertThat(response.getSalesToPay().size(), is(1));
  //    assertThat(response.getCustomer(), is("[1234] Test, Test"));
  //    assertThat(response.getBanks().size(), is(1));
  //
  //    verify(mockPaymentsService).prepareToPay(salesIds);
  //  }
  //
  //  @Test
  //  void shouldAddNoExtraCostToPrepareToPayWhenUndefinedPaymentIsPresent() {
  //    SaleToPayDto saleToPayDto = buildDummySaleToPay("EFECTIVO", 1L);
  //    saleToPayDto.setPayment(null);
  //    PreparedPaymentDto preparedPaymentDto = buildDummyPreparedPayment(saleToPayDto);
  //    final List<Long> salesIds = Collections.singletonList(1L);
  //    when(mockPaymentsService.prepareToPay(salesIds)).thenReturn(preparedPaymentDto);
  //    when(mockNoExtraCostPaymentMethodsService.getNoExtraCostPaymentMethods())
  //
  // .thenReturn(Collections.singletonList(PaymentMethod.builder().paymentMethodId(2L).build()));
  //
  //    SalesToPayRequest request = new SalesToPayRequest();
  //    request.setSalesIds(salesIds);
  //    final PrepareToPayResponse response = controller.prepareToPay(request);
  //
  //    assertThat(response, is(notNullValue()));
  //    assertThat(response.getSalesToPay().size(), is(1));
  //    assertThat(response.getCustomer(), is("[1234] Test, Test"));
  //    assertThat(response.getBanks(), hasSize(0));
  //    assertThat(response.getNoExtraCostPaymentMethods(), hasSize(1));
  //
  //    verify(mockPaymentsService).prepareToPay(salesIds);
  //    verify(mockNoExtraCostPaymentMethodsService).getNoExtraCostPaymentMethods();
  //  }

  private SaleToPayDto buildDummySaleToPay(String paymentMethodName, long paymentMethodId) {
    return SaleToPayDto.builder()
        .totalPayment(BigDecimal.TEN)
        .payment(
            ComprobantesPagosDto.builder()
                .idFormaPago(
                    NegocioFormasPagoDto.builder()
                        .nombreFormaPago(paymentMethodName)
                        .id(paymentMethodId)
                        .build())
                .build())
        .sale(ComprobantesDto.builder().id(1L).build())
        .build();
  }

  private PreparedPaymentDto buildDummyPreparedPayment(SaleToPayDto saleToPayDto) {
    return PreparedPaymentDto.builder()
        .customer(PersonasDto.builder().id(1L).documento("1234").razonSocial("Test, Test").build())
        .salesToPay(Collections.singletonList(saleToPayDto))
        .build();
  }
}
