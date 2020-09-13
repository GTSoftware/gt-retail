package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.api.response.PaymentMethod;
import ar.com.gtsoftware.api.response.PaymentPlan;
import ar.com.gtsoftware.api.response.PlanDetail;
import ar.com.gtsoftware.dto.domain.NegocioFormasPagoDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDetalleDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDto;
import ar.com.gtsoftware.search.FormasPagoSearchFilter;
import ar.com.gtsoftware.search.PlanesPagoSearchFilter;
import ar.com.gtsoftware.service.NegocioFormasPagoService;
import ar.com.gtsoftware.service.NegocioPlanesPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class NoExtraCostPaymentMethodsServiceImplTest {

    private NoExtraCostPaymentMethodsServiceImpl service;
    @Mock
    private NegocioFormasPagoService mockFormasPagoService;
    @Mock
    private NegocioPlanesPagoService mockPlanesPagoService;
    @Captor
    private ArgumentCaptor<FormasPagoSearchFilter> formasPagoSearchFilterArgumentCaptor;
    @Captor
    private ArgumentCaptor<PlanesPagoSearchFilter> planesPagoSearchFilterArgumentCaptor;

    @BeforeEach
    void setUp() {
        initMocks(this);
        service = new NoExtraCostPaymentMethodsServiceImpl(mockFormasPagoService, mockPlanesPagoService);

        when(mockFormasPagoService.findAllBySearchFilter(any(FormasPagoSearchFilter.class)))
                .thenReturn(buildMockFormasPago(true));
    }

    @Test
    void shouldGetNoExtraCostPaymentMethodsWithNoRequiredPaymentPlan() {

        final List<PaymentMethod> noExtraCostPaymentMethods = service.getNoExtraCostPaymentMethods();

        assertNotNull(noExtraCostPaymentMethods);
        assertThat(noExtraCostPaymentMethods, hasSize(1));
        final PaymentMethod paymentMethod = noExtraCostPaymentMethods.get(0);
        assertThat(paymentMethod.getPaymentPlans(), is(empty()));

        verify(mockFormasPagoService).findAllBySearchFilter(formasPagoSearchFilterArgumentCaptor.capture());

        final FormasPagoSearchFilter formasPagoSearchFilterValue = formasPagoSearchFilterArgumentCaptor.getValue();
        assertTrue(formasPagoSearchFilterValue.getDisponibleVenta());
    }

    @Test
    void shouldGetNoExtraCostPaymentMethodsWithRequiredPaymentPlan() {

        when(mockPlanesPagoService.findAllBySearchFilter(any(PlanesPagoSearchFilter.class)))
                .thenReturn(buildMockPlanesPago());
        final List<PaymentMethod> noExtraCostPaymentMethods = service.getNoExtraCostPaymentMethods();

        assertNotNull(noExtraCostPaymentMethods);
        assertThat(noExtraCostPaymentMethods, hasSize(1));
        final PaymentMethod paymentMethod = noExtraCostPaymentMethods.get(0);

        assertThat(paymentMethod.getPaymentPlans(), hasSize(1));
        final PaymentPlan paymentPlan = paymentMethod.getPaymentPlans().get(0);

        assertThat(paymentPlan.getPaymentPlanDetails(), hasSize(1));
        final PlanDetail planDetail = paymentPlan.getPaymentPlanDetails().get(0);

        assertThat(planDetail.getRate(), is(BigDecimal.ONE));

        verify(mockFormasPagoService).findAllBySearchFilter(formasPagoSearchFilterArgumentCaptor.capture());
        verify(mockPlanesPagoService).findAllBySearchFilter(planesPagoSearchFilterArgumentCaptor.capture());

        final FormasPagoSearchFilter formasPagoSearchFilterValue = formasPagoSearchFilterArgumentCaptor.getValue();
        assertTrue(formasPagoSearchFilterValue.getDisponibleVenta());

        final PlanesPagoSearchFilter planesPagoSearchFilter = planesPagoSearchFilterArgumentCaptor.getValue();
        assertThat(planesPagoSearchFilter.getActivo(), is(true));
        assertThat(planesPagoSearchFilter.getIdFormaPago(), is(2L));
    }

    private List<NegocioPlanesPagoDto> buildMockPlanesPago() {
        return Collections.singletonList(
                NegocioPlanesPagoDto.builder()
                        .nombre("TC MASTER")
                        .id(2L)
                        .negocioPlanesPagoDetalles(Arrays.asList(
                                NegocioPlanesPagoDetalleDto.builder()
                                        .cuotas(12)
                                        .activo(true)
                                        .coeficienteInteres(BigDecimal.TEN)
                                        .build(),
                                NegocioPlanesPagoDetalleDto.builder()
                                        .cuotas(1)
                                        .activo(true)
                                        .coeficienteInteres(BigDecimal.ONE)
                                        .build(),
                                NegocioPlanesPagoDetalleDto.builder()
                                        .cuotas(5)
                                        .activo(false)
                                        .coeficienteInteres(BigDecimal.ONE)
                                        .build()
                        ))
                        .build()
        );
    }

    private List<NegocioFormasPagoDto> buildMockFormasPago(boolean requierePlan) {
        List<NegocioFormasPagoDto> formasPagoDtos = new ArrayList<>(1);
        if (!requierePlan) {
            formasPagoDtos.add(NegocioFormasPagoDto.builder()
                    .id(1L)
                    .nombreFormaPago("EFECTIVO")
                    .requiereValores(false)
                    .requierePlan(false)
                    .nombreCorto("EFE")
                    .venta(true)
                    .compra(true)
                    .build());
        } else {
            formasPagoDtos.add(NegocioFormasPagoDto.builder()
                    .id(2L)
                    .nombreFormaPago("TARJETA DE CREDITO")
                    .requierePlan(true)
                    .nombreCorto("TC")
                    .venta(true)
                    .compra(false)
                    .build());
        }

        return formasPagoDtos;
    }
}