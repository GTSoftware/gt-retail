package ar.com.gtsoftware.rules.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.PromotionCartItem;
import ar.com.gtsoftware.dto.domain.ComprobantesLineasDto;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.rules.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
class OfertasHelperTest {

  private OfertasHelper helper;

  @Mock private OfertasFinder ofertasFinderMock;
  @Mock private CacheManager cacheManager;
  @Mock private Cache cache;

  @BeforeEach
  void setUp() {
    helper = new OfertasHelper(ofertasFinderMock, cacheManager);

    when(cacheManager.getCache("offers")).thenReturn(cache);
    when(cache.get("offersList", List.class)).thenReturn(null);
  }

  @Test
  public void shouldAddPercentDiscount() {
    when(ofertasFinderMock.existsActiveOffers()).thenReturn(true);
    when(ofertasFinderMock.findOfertas()).thenReturn(buildDiscountOffer());

    PromotionCartItem cartItem = new PromotionCartItem();
    cartItem.setLinea(ComprobantesLineasDto.builder().descripcion("TORNILLO").build());
    helper.ejecutarReglasOferta(cartItem);

    assertThat(cartItem.getDescuento(), is(BigDecimal.TEN));
    verify(ofertasFinderMock).findOfertas();
  }

  @Test
  public void shouldAddPercentDiscountWhenModuleConditionApplies() {
    when(ofertasFinderMock.existsActiveOffers()).thenReturn(true);
    when(ofertasFinderMock.findOfertas()).thenReturn(buildModuleDiscountOffer());

    PromotionCartItem cartItem = new PromotionCartItem();
    cartItem.setLinea(
        ComprobantesLineasDto.builder()
            .cantidad(BigDecimal.valueOf(100L))
            .descripcion("TORNILLO")
            .build());

    helper.ejecutarReglasOferta(cartItem);

    assertThat(cartItem.getDescuento(), is(BigDecimal.TEN));
    verify(ofertasFinderMock).findOfertas();
  }

  @Test
  public void shouldAddPercentDiscountWhenModuleAndCategoryConditionApplies() {
    when(ofertasFinderMock.existsActiveOffers()).thenReturn(true);
    when(ofertasFinderMock.findOfertas()).thenReturn(buildModuleCategoryDiscountOffer());

    PromotionCartItem cartItem = new PromotionCartItem();
    cartItem.setLinea(
        ComprobantesLineasDto.builder()
            .idProducto(
                ProductosDto.builder()
                    .idRubro(ProductosRubrosDto.builder().id(42L).build())
                    .build())
            .cantidad(BigDecimal.valueOf(100L))
            .descripcion("TORNILLO test")
            .build());

    helper.ejecutarReglasOferta(cartItem);

    assertThat(cartItem.getDescuento(), is(BigDecimal.TEN));
    verify(ofertasFinderMock).findOfertas();
  }

  private List<OfertaDto> buildDiscountOffer() {
    return List.of(
        OfertaDto.builder()
            .descuento(BigDecimal.TEN)
            .id(1L)
            .textoOferta("TEST")
            .tipoAccion(TipoAccion.DESCUENTO_PORCENTAJE)
            .condiciones(
                List.of(
                    Condicion.builder()
                        .campo(Campo.DESCRIPCION)
                        .operacion(Operacion.CONTIENE)
                        .valor("TORNILLO")
                        .build()))
            .build());
  }

  private List<OfertaDto> buildModuleDiscountOffer() {
    return List.of(
        OfertaDto.builder()
            .descuento(BigDecimal.TEN)
            .id(2L)
            .textoOferta("TEST Module")
            .tipoAccion(TipoAccion.DESCUENTO_PORCENTAJE)
            .condiciones(
                List.of(
                    Condicion.builder()
                        .campo(Campo.CANTIDAD)
                        .operacion(Operacion.MULTIPLO)
                        .valor("100")
                        .build()))
            .build());
  }

  private List<OfertaDto> buildModuleCategoryDiscountOffer() {
    return List.of(
        OfertaDto.builder()
            .descuento(BigDecimal.TEN)
            .id(2L)
            .textoOferta("TEST Module category")
            .tipoAccion(TipoAccion.DESCUENTO_PORCENTAJE)
            .condiciones(
                List.of(
                    Condicion.builder()
                        .campo(Campo.CANTIDAD)
                        .operacion(Operacion.MULTIPLO)
                        .valor("100")
                        .build(),
                    Condicion.builder()
                        .campo(Campo.ID_RUBRO)
                        .operacion(Operacion.IGUAL)
                        .valor("42")
                        .build()))
            .build());
  }
}
