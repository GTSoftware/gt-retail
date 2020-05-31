package ar.com.gtsoftware.rules.helper;

import ar.com.gtsoftware.api.PromotionCartItem;
import ar.com.gtsoftware.dto.domain.ComprobantesLineasDto;
import ar.com.gtsoftware.rules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class OfertasHelperTest {

    private OfertasHelper helper;

    @Mock
    private OfertasFinder ofertasFinderMock;

    @BeforeEach
    void setUp() {
        initMocks(this);

        helper = new OfertasHelper(ofertasFinderMock);
    }

    @Test
    public void shouldEjecutarReglas() {
        when(ofertasFinderMock.findOfertas())
                .thenReturn(buildDummyOfertas());

        PromotionCartItem cartItem = new PromotionCartItem();
        cartItem.setLinea(ComprobantesLineasDto.builder()
                .descripcion("TORNILLO")
                .build());
        helper.ejecutarReglasOferta(cartItem);

        assertThat(cartItem.getDescuento(), is(BigDecimal.TEN));
        verify(ofertasFinderMock).findOfertas();
    }

    private List<OfertaDto> buildDummyOfertas() {
        List<OfertaDto> ofertas = new ArrayList<>(1);
        ofertas.add(
                OfertaDto.builder()
                        .descuento(BigDecimal.TEN)
                        .id(1L)
                        .textoOferta("TEST")
                        .tipoAccion(TipoAccion.DESCUENTO_PORCENTAJE)
                        .condiciones(List.of(Condicion.builder().campo(Campo.DESCRIPCION)
                                .operacion(Operacion.CONTIENE)
                                .valor("TORNILLO").build()))
                        .build()
        );

        return ofertas;
    }
}