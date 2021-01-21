package ar.com.gtsoftware.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import ar.com.gtsoftware.api.exception.PointOfSaleNotFoundException;
import ar.com.gtsoftware.api.exception.SaleNotFoundException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

class InvoiceControllerImplTest {

  private InvoiceControllerImpl controller;
  @Mock private SecurityUtils securityUtilsMock;
  @Mock private FiscalPuntosVentaService puntosVentaServiceMock;
  @Mock private ComprobantesService comprobantesServiceMock;
  @Mock private FacturacionVentasService facturacionServiceMock;

  @Captor private ArgumentCaptor<FiscalPuntosVentaSearchFilter> captor;

  @BeforeEach
  void setUp() {
    initMocks(this);
    controller =
        new InvoiceControllerImpl(
            securityUtilsMock,
            puntosVentaServiceMock,
            comprobantesServiceMock,
            facturacionServiceMock);
  }

  @Test
  public void shouldGetPointsOfSale() {
    final JwtUserDetails userDetails = JwtUserDetails.builder().sucursalId(1L).build();
    when(securityUtilsMock.getUserDetails()).thenReturn(userDetails);
    when(puntosVentaServiceMock.findAllBySearchFilter(any(FiscalPuntosVentaSearchFilter.class)))
        .thenReturn(dummyPuntosVenta());

    final List<PointOfSale> pointsOfSale = controller.getPointsOfSale();

    verify(securityUtilsMock).getUserDetails();

    verify(puntosVentaServiceMock).findAllBySearchFilter(captor.capture());

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
  public void shouldInvoice() throws ServiceException {
    final FiscalPuntosVentaDto puntoVentaDummy =
        FiscalPuntosVentaDto.builder().nroPuntoVenta(1).build();
    when(comprobantesServiceMock.find(1L))
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
    when(puntosVentaServiceMock.find(1)).thenReturn(puntoVentaDummy);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    final InvoiceResponse invoiceResponse = controller.invoiceSale(invoiceRequest);

    assertThat(invoiceResponse.getInvoiceNumber(), is("B 0001-00000001"));

    verify(facturacionServiceMock)
        .registrarFacturaVenta(eq(1L), eq(puntoVentaDummy), eq(0L), isNull());
    verify(comprobantesServiceMock, times(2)).find(eq(1L));
    verify(puntosVentaServiceMock).find(eq(1));
  }

  @Test
  public void shouldThrowSaleNotFoundWhenInvoiceWithInvalidSaleId() {
    final FiscalPuntosVentaDto puntoVentaDummy =
        FiscalPuntosVentaDto.builder().nroPuntoVenta(1).build();
    when(comprobantesServiceMock.find(1L)).thenReturn(null);
    when(puntosVentaServiceMock.find(1)).thenReturn(puntoVentaDummy);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    Assertions.assertThrows(
        SaleNotFoundException.class, () -> controller.invoiceSale(invoiceRequest));

    verify(comprobantesServiceMock).find(eq(1L));
    verify(puntosVentaServiceMock).find(eq(1));
  }

  @Test
  public void shouldThrowPointOfSaleNotFoundWhenInvoiceWithInvalidPointOfSale() {
    when(comprobantesServiceMock.find(1L)).thenReturn(ComprobantesDto.builder().id(1L).build());
    when(puntosVentaServiceMock.find(1)).thenReturn(null);

    final InvoiceRequest invoiceRequest =
        InvoiceRequest.builder().pointOfSale(1).saleId(1L).build();

    Assertions.assertThrows(
        PointOfSaleNotFoundException.class, () -> controller.invoiceSale(invoiceRequest));

    verify(comprobantesServiceMock).find(eq(1L));
    verify(puntosVentaServiceMock).find(eq(1));
  }
}
