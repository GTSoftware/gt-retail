package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.domain.Depositos;
import ar.com.gtsoftware.domain.Productos;
import ar.com.gtsoftware.domain.Remito;
import ar.com.gtsoftware.domain.RemitoDetalle;
import ar.com.gtsoftware.dto.ProductoMovimiento;
import ar.com.gtsoftware.mappers.ProductosMapper;
import ar.com.gtsoftware.mappers.RemitoMapper;
import ar.com.gtsoftware.search.RemitoDetalleSearchFilter;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RemitoServiceImplTest {

    @Mock
    private RemitoFacade remitoFacadeMock;
    @Mock
    private RemitoDetalleFacade remitoDetalleFacadeMock;
    @Mock
    private RemitoMapper remitoMapperMock;
    @Mock
    private UsuariosFacade usuariosFacadeMock;
    @Mock
    private RemitoTipoMovimientoFacade tipoMovimientoFacadeMock;
    @Mock
    private DepositosFacade depositosFacadeMock;
    @Mock
    private BusinessDateUtils dateUtilsMock;
    @Mock
    private ProductosFacade productosFacadeMock;
    @Mock
    private PersonasFacade personasFacadeMock;
    @Mock
    private ProductosMapper productosMapperMock;
    @Mock
    private ProductoXDepositoFacade stockFacadeMock;
    private RemitoServiceImpl service;

    @BeforeEach
    void setUp() {
        initMocks(this);
        service = new RemitoServiceImpl(remitoFacadeMock,
                remitoDetalleFacadeMock,
                remitoMapperMock,
                usuariosFacadeMock,
                tipoMovimientoFacadeMock,
                depositosFacadeMock,
                dateUtilsMock,
                productosFacadeMock,
                personasFacadeMock,
                productosMapperMock,
                stockFacadeMock);
    }

    @Test
    void shouldGetProperPartialStockWhenInternalExternalDelivery() {
        final RemitoDetalleSearchFilter remitoDetalleSearchFilter = new RemitoDetalleSearchFilter();
        when(stockFacadeMock.getStockBySearchFilter(any())).thenReturn(BigDecimal.TEN);
        when(remitoDetalleFacadeMock.findAllBySearchFilter(remitoDetalleSearchFilter)).thenReturn(buildDummyDetalles());
        final List<ProductoMovimiento> movimientosProducto = service.getMovimientosProducto(remitoDetalleSearchFilter);

        assertThat(movimientosProducto.get(0).getStockParcial(), is(BigDecimal.TEN));
        assertThat(movimientosProducto.get(1).getStockParcial(), is(new BigDecimal(13)));
        assertThat(movimientosProducto.get(2).getStockParcial(), is(new BigDecimal(17)));
        assertThat(movimientosProducto.get(3).getStockParcial(), is(new BigDecimal(7)));
    }

    @Test
    void shouldGetProperPartialStockWhenInternalDelivery() {
        final RemitoDetalleSearchFilter remitoDetalleSearchFilter = RemitoDetalleSearchFilter.builder()
                .idDepositoMovimiento(1L)
                .build();

        when(stockFacadeMock.getStockBySearchFilter(any())).thenReturn(BigDecimal.TEN);
        when(remitoDetalleFacadeMock.findAllBySearchFilter(remitoDetalleSearchFilter)).thenReturn(buildDummyDetallesInterno());
        final List<ProductoMovimiento> movimientosProducto = service.getMovimientosProducto(remitoDetalleSearchFilter);

        assertThat(movimientosProducto.get(0).getStockParcial(), is(BigDecimal.TEN));
        assertThat(movimientosProducto.get(1).getStockParcial(), is(new BigDecimal(13)));
        assertThat(movimientosProducto.get(2).getStockParcial(), is(new BigDecimal(17)));
        assertThat(movimientosProducto.get(3).getStockParcial(), is(new BigDecimal(7)));
    }

    private List<RemitoDetalle> buildDummyDetalles() {
        List<RemitoDetalle> detalles = new ArrayList<>(4);

        RemitoDetalle mov4 = new RemitoDetalle();
        mov4.setCantidad(new BigDecimal(3));
        mov4.setIdProducto(new Productos());
        mov4.setRemitoCabecera(buildDummyRemitoEgreso());
        detalles.add(mov4);

        RemitoDetalle mov3 = new RemitoDetalle();
        mov3.setCantidad(new BigDecimal(4));
        mov3.setIdProducto(new Productos());
        mov3.setRemitoCabecera(buildDummyRemitoEgreso());
        detalles.add(mov3);

        RemitoDetalle mov2 = new RemitoDetalle();
        mov2.setCantidad(new BigDecimal(10));
        mov2.setIdProducto(new Productos());
        mov2.setRemitoCabecera(buildDummyRemitoIngreso());
        detalles.add(mov2);

        RemitoDetalle mov1 = new RemitoDetalle();
        mov1.setCantidad(new BigDecimal(10));
        mov1.setIdProducto(new Productos());
        mov1.setRemitoCabecera(buildDummyRemitoIngreso());
        detalles.add(mov1);

        return detalles;
    }

    private List<RemitoDetalle> buildDummyDetallesInterno() {
        List<RemitoDetalle> detalles = new ArrayList<>(4);
        Depositos depoBase = new Depositos();
        depoBase.setId(1L);

        Depositos depoOtro = new Depositos();
        depoOtro.setId(2L);

        RemitoDetalle mov4 = new RemitoDetalle();
        mov4.setCantidad(new BigDecimal(3));
        mov4.setIdProducto(new Productos());
        mov4.setRemitoCabecera(buildDummyRemitoInterno(depoBase, depoOtro));
        detalles.add(mov4);

        RemitoDetalle mov3 = new RemitoDetalle();
        mov3.setCantidad(new BigDecimal(4));
        mov3.setIdProducto(new Productos());
        mov3.setRemitoCabecera(buildDummyRemitoInterno(depoBase, depoOtro));
        detalles.add(mov3);

        RemitoDetalle mov2 = new RemitoDetalle();
        mov2.setCantidad(new BigDecimal(10));
        mov2.setIdProducto(new Productos());
        mov2.setRemitoCabecera(buildDummyRemitoInterno(depoOtro, depoBase));
        detalles.add(mov2);

        RemitoDetalle mov1 = new RemitoDetalle();
        mov1.setCantidad(new BigDecimal(10));
        mov1.setIdProducto(new Productos());
        mov1.setRemitoCabecera(buildDummyRemitoInterno(depoOtro, depoBase));
        detalles.add(mov1);

        return detalles;
    }

    private Remito buildDummyRemitoEgreso() {
        Remito remito = new Remito();
        remito.setIsOrigenInterno(true);
        remito.setIsDestinoInterno(false);

        return remito;
    }

    private Remito buildDummyRemitoIngreso() {
        Remito remito = new Remito();
        remito.setIsOrigenInterno(false);
        remito.setIsDestinoInterno(true);

        return remito;
    }

    private Remito buildDummyRemitoInterno(Depositos origen, Depositos destino) {
        Remito remito = new Remito();
        remito.setIsOrigenInterno(true);
        remito.setIsDestinoInterno(true);
        remito.setIdOrigenInterno(origen);
        remito.setIdDestinoPrevistoInterno(destino);

        return remito;
    }
}