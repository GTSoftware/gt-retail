package ar.com.gtsoftware.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.ComprobantesPagosDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.entity.Comprobantes;
import ar.com.gtsoftware.entity.ComprobantesPagos;
import ar.com.gtsoftware.entity.NegocioFormasPago;
import ar.com.gtsoftware.entity.Personas;
import ar.com.gtsoftware.mappers.*;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class PaymentsServiceImplTest {

  private PaymentsServiceImpl paymentsService;

  @Mock private RecibosFacade mockRecibosFacade;
  @Mock private ComprobantesFacade mockComprobantesFacade;
  @Mock private CajasMovimientosFacade mockCajasMovimientosFacade;
  @Mock private CajasFacade mockCajasFacade;
  @Mock private ComprobantesPagosFacade mockComprobantesPagosFacade;
  @Mock private PersonasCuentaCorrienteService mockPersonasCuentaCorrienteService;
  @Mock private UsuariosFacade mockUsuariosFacade;
  @Mock private ComprobantesPagosMapper mockComprobantesPagosMapper;
  @Mock private CuponesMapper mockCuponesMapper;
  @Mock private ChequesTercerosMapper mockChequesTercerosMapper;
  @Mock private RecibosMapper mockRecibosMapper;
  @Mock private PersonasMapper mockPersonasMapper;
  @Mock private ComprobantesMapper mockComprobantesMapper;

  @BeforeEach
  void setUp() {
    initMocks(this);
    paymentsService =
        new PaymentsServiceImpl(
            mockRecibosFacade,
            mockComprobantesFacade,
            mockCajasMovimientosFacade,
            mockCajasFacade,
            mockComprobantesPagosFacade,
            mockPersonasCuentaCorrienteService,
            mockUsuariosFacade,
            mockComprobantesPagosMapper,
            mockCuponesMapper,
            mockChequesTercerosMapper,
            mockRecibosMapper,
            mockPersonasMapper,
            mockComprobantesMapper);
  }

  @Test
  void shouldPrepareToPayWhenOneCashPaymentIsPresent() {
    Comprobantes comp = buildDummySale();
    final ComprobantesPagos pago = comp.getPagosList().get(0);
    pago.getIdFormaPago().setRequiereValores(false);

    when(mockComprobantesFacade.find(1L)).thenReturn(comp);
    when(mockPersonasMapper.entityToDto(eq(comp.getIdPersona()), any()))
        .thenReturn(PersonasDto.builder().id(2L).build());
    when(mockComprobantesPagosMapper.entityToDto(eq(pago), any()))
        .thenReturn(ComprobantesPagosDto.builder().build());
    when(mockComprobantesMapper.entityToDto(eq(comp), any()))
        .thenReturn(ComprobantesDto.builder().build());
    final PreparedPaymentDto preparedPaymentDto =
        paymentsService.prepareToPay(Collections.singletonList(1L));

    assertThat(preparedPaymentDto, is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer(), is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer().getId(), is(2L));
    assertThat(preparedPaymentDto.getSalesToPay(), is(notNullValue()));
    assertThat(preparedPaymentDto.getSalesToPay().size(), is(1));

    final SaleToPayDto saleToPayDto = preparedPaymentDto.getSalesToPay().get(0);
    assertThat(saleToPayDto.getPayment(), is(notNullValue()));
    assertThat(saleToPayDto.getSale(), is(notNullValue()));
    assertThat(saleToPayDto.getTotalPayment(), is(BigDecimal.TEN));
    assertThat(saleToPayDto.getMaxAllowedPayment(), is(BigDecimal.valueOf(15)));
    assertThat(saleToPayDto.getMinAllowedPayment(), is(BigDecimal.valueOf(5)));
  }

  @Test
  void shouldPrepareToPayWhenRequiredValuePaymentIsPresent() {
    Comprobantes comp = buildDummySale();
    comp.getPagosList().get(0).getIdFormaPago().setRequiereValores(true);

    when(mockComprobantesFacade.find(1L)).thenReturn(comp);
    when(mockPersonasMapper.entityToDto(eq(comp.getIdPersona()), any()))
        .thenReturn(PersonasDto.builder().id(2L).build());
    when(mockComprobantesPagosMapper.entityToDto(eq(comp.getPagosList().get(0)), any()))
        .thenReturn(ComprobantesPagosDto.builder().build());
    when(mockComprobantesMapper.entityToDto(eq(comp), any()))
        .thenReturn(ComprobantesDto.builder().build());
    final PreparedPaymentDto preparedPaymentDto =
        paymentsService.prepareToPay(Collections.singletonList(1L));

    assertThat(preparedPaymentDto, is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer(), is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer().getId(), is(2L));
    assertThat(preparedPaymentDto.getSalesToPay(), is(notNullValue()));
    assertThat(preparedPaymentDto.getSalesToPay().size(), is(1));

    final SaleToPayDto saleToPayDto = preparedPaymentDto.getSalesToPay().get(0);
    assertThat(saleToPayDto.getPayment(), is(notNullValue()));
    assertThat(saleToPayDto.getSale(), is(notNullValue()));
    assertThat(saleToPayDto.getTotalPayment(), is(BigDecimal.TEN));
    assertThat(saleToPayDto.getMaxAllowedPayment(), is(BigDecimal.TEN));
    assertThat(saleToPayDto.getMinAllowedPayment(), is(BigDecimal.TEN));
  }

  @Test
  void shouldPrepareToPayWhenNoPaymentIsPresent() {
    Comprobantes comp = buildDummySale();
    comp.setPagosList(Collections.emptyList());
    when(mockComprobantesFacade.find(1L)).thenReturn(comp);
    when(mockPersonasMapper.entityToDto(eq(comp.getIdPersona()), any()))
        .thenReturn(PersonasDto.builder().id(2L).build());
    when(mockComprobantesMapper.entityToDto(eq(comp), any()))
        .thenReturn(ComprobantesDto.builder().build());
    final PreparedPaymentDto preparedPaymentDto =
        paymentsService.prepareToPay(Collections.singletonList(1L));

    assertThat(preparedPaymentDto, is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer(), is(notNullValue()));
    assertThat(preparedPaymentDto.getCustomer().getId(), is(2L));
    assertThat(preparedPaymentDto.getSalesToPay(), is(notNullValue()));
    assertThat(preparedPaymentDto.getSalesToPay().size(), is(1));

    final SaleToPayDto saleToPayDto = preparedPaymentDto.getSalesToPay().get(0);
    assertThat(saleToPayDto.getPayment(), is(nullValue()));
    assertThat(saleToPayDto.getSale(), is(notNullValue()));
    assertThat(saleToPayDto.getTotalPayment(), is(BigDecimal.TEN));
    assertThat(saleToPayDto.getMinAllowedPayment(), is(BigDecimal.ZERO));
    assertThat(saleToPayDto.getMaxAllowedPayment(), is(BigDecimal.TEN));
  }

  @Test
  void shouldNotValidatePrepareToPayWhenSalesAreForDifferentCustomers() {
    Comprobantes comp1 = buildDummySale();
    Comprobantes comp2 = buildDummySale();
    comp1.getIdPersona().setId(1L);
    comp2.getIdPersona().setId(2L);
    when(mockComprobantesFacade.find(any(Long.class))).thenReturn(comp1).thenReturn(comp2);

    Assertions.assertThrows(
        RuntimeException.class, () -> paymentsService.prepareToPay(Arrays.asList(1L, 2L)));
  }

  @Test
  void shouldNotValidatePrepareToPayWhenSalesHaveNoRemainingAmount() {
    Comprobantes comp1 = buildDummySale();
    Comprobantes comp2 = buildDummySale();

    comp2.setSaldo(BigDecimal.ZERO);
    when(mockComprobantesFacade.find(any(Long.class))).thenReturn(comp1).thenReturn(comp2);

    Assertions.assertThrows(
        RuntimeException.class, () -> paymentsService.prepareToPay(Arrays.asList(1L, 2L)));
  }

  private Comprobantes buildDummySale() {
    Comprobantes comp = new Comprobantes();
    comp.setId(1L);
    Personas cliente = new Personas();
    cliente.setId(2L);
    comp.setIdPersona(cliente);
    comp.setSaldo(BigDecimal.TEN);
    ComprobantesPagos pago = new ComprobantesPagos();
    pago.setMontoPagado(BigDecimal.ZERO);
    pago.setMontoPago(BigDecimal.TEN);
    NegocioFormasPago formasPago = new NegocioFormasPago();
    formasPago.setRequiereValores(false);
    formasPago.setId(1L);
    pago.setIdFormaPago(formasPago);
    comp.setPagosList(Collections.singletonList(pago));

    return comp;
  }
}
