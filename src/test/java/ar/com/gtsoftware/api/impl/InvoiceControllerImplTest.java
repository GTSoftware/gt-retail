package ar.com.gtsoftware.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ar.com.gtsoftware.api.exception.PointOfSaleNotFoundException;
import ar.com.gtsoftware.api.exception.SaleNotFoundException;
import ar.com.gtsoftware.api.idempotence.IdempotenceHandler;
import ar.com.gtsoftware.api.request.InvoiceRequest;
import ar.com.gtsoftware.api.response.InvoiceResponse;
import ar.com.gtsoftware.api.response.PointOfSale;
import ar.com.gtsoftware.auth.JwtUserDetails;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import ar.com.gtsoftware.dto.domain.FiscalPuntosVentaDto;
import ar.com.gtsoftware.enums.TiposPuntosVenta;
import ar.com.gtsoftware.search.FiscalPuntosVentaSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.FacturacionVentasService;
import ar.com.gtsoftware.service.FiscalPuntosVentaService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerImplTest {

  private InvoiceControllerImpl controller;

  @Mock private SecurityUtils securityUtils;
  @Mock private FiscalPuntosVentaService puntosVentaService;
  @Mock private ComprobantesService comprobantesService;
  @Mock private FacturacionVentasService facturacionService;
  @Mock private IdempotenceHandler idempotenceHandler;

  @Captor private ArgumentCaptor<FiscalPuntosVentaSearchFilter> captor;

  @Spy private JwtUserDetails userDetails;

  @BeforeEach
  void setUp() {
    controller =
        new InvoiceControllerImpl(
            securityUtils,
            puntosVentaService,
            comprobantesService,
            facturacionService,
            idempotenceHandler);
  }

  @Test
  void shouldGetPointsOfSale() {
    when(userDetails.getSucursalId()).thenReturn(1L);
    when(securityUtils.getUserDetails()).thenReturn(userDetails);
    when(puntosVentaService.findAllBySearchFilter(any(FiscalPuntosVentaSearchFilter.class)))
        .thenReturn(dummyPuntosVenta());

    final List<PointOfSale> pointsOfSale = controller.getPointsOfSale();

    verify(puntosVentaService).findAllBySearchFilter(captor.capture());

    final FiscalPuntosVentaSearchFilter filter = captor.getValue();
    assertThat(filter.getActivo(), is(true));
    assertThat(filter.getIdSucursal(), is(1L));
    assertThat(filter.getTiposPuntoVenta(), Matchers.hasSize(2));
    assertThat(
        filter.getTiposPuntoVenta(),
        containsInAnyOrder(TiposPuntosVenta.ELECTRONICO, TiposPuntosVenta.MANUAL));

    assertThat(pointsOfSale, Matchers.hasSize(2));
    assertThat(pointsOfSale.get(0).getDisplayName(), is("[1-MANUAL] Punto Venta Manual"));
    assertThat(pointsOfSale.get(1).isByDefault(), is(true));
  }

  private List<FiscalPuntosVentaDto> dummyPuntosVenta() {
    List<FiscalPuntosVentaDto> puntosVentaDtoList = new ArrayList<>(2);
    puntosVentaDtoList.add(
        FiscalPuntosVentaDto.builder()
            .activo(true)
            .descripcion("Punto Venta Manual")
            .nroPuntoVenta(1)
            .tipo(TiposPuntosVenta.MANUAL)
            .build());

    puntosVentaDtoList.add(
        FiscalPuntosVentaDto.builder()
            .activo(true)
            .descripcion("Punto Venta Electronico")
            .nroPuntoVenta(2)
            .tipo(TiposPuntosVenta.ELECTRONICO)
            .build());

    return puntosVentaDtoList;
  }

  @Test
  void shouldInvoiceSale() throws ServiceException {
    final FiscalPuntosVentaDto puntoVentaDummy =
        FiscalPuntosVentaDto.builder().nroPuntoVenta(1).build();
    when(comprobantesService.find(1L))
        .thenReturn(ComprobantesDto.builder().id(1L).build())
        .thenReturn(
            ComprobantesDto.builder()
                .id(1L)
                .idRegistro(
                    FiscalLibroIvaVentasDto.builder()
                        .letraFactura("B")
                        .puntoVentaFactura("0001")
                        .numeroFactura("00000001")
                        .build())
                .build());
    when(puntosVentaService.find(1)).thenReturn(puntoVentaDummy);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    final InvoiceResponse invoiceResponse = controller.invoiceSale(invoiceRequest);

    assertThat(invoiceResponse.getInvoiceNumber(), is("B 0001-00000001"));

    verify(facturacionService).registrarFacturaVenta(eq(1L), eq(puntoVentaDummy), eq(0L), isNull());
    verify(comprobantesService, times(2)).find(eq(1L));
    verify(puntosVentaService).find(eq(1));
  }

  @Test
  void shouldThrowSaleNotFoundWhenInvoiceWithInvalidSaleId() {
    final FiscalPuntosVentaDto puntoVentaDummy =
        FiscalPuntosVentaDto.builder().nroPuntoVenta(1).build();
    when(comprobantesService.find(1L)).thenReturn(null);
    when(puntosVentaService.find(1)).thenReturn(puntoVentaDummy);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    Assertions.assertThrows(
        SaleNotFoundException.class, () -> controller.invoiceSale(invoiceRequest));

    verify(comprobantesService).find(eq(1L));
    verify(puntosVentaService).find(eq(1));
  }

  @Test
  void shouldThrowPointOfSaleNotFoundWhenInvoiceWithInvalidPointOfSale() {
    when(comprobantesService.find(1L)).thenReturn(ComprobantesDto.builder().id(1L).build());
    when(puntosVentaService.find(1)).thenReturn(null);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    Assertions.assertThrows(
        PointOfSaleNotFoundException.class, () -> controller.invoiceSale(invoiceRequest));

    verify(comprobantesService).find(eq(1L));
    verify(puntosVentaService).find(eq(1));
  }
}
