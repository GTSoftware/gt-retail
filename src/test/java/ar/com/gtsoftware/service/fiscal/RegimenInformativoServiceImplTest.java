package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.ImportesAlicuotasIVA;
import ar.com.gtsoftware.dto.LibroIVADTO;
import ar.com.gtsoftware.dto.RegimenInformativoVentas;
import ar.com.gtsoftware.dto.RegistroIVADTO;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import ar.com.gtsoftware.dto.fiscal.reginfo.ReginfoCvCabecera;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.impl.LibroIVAVentasServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RegimenInformativoServiceImplTest {

    private RegimenInformativoService service;

    @Mock
    private LibroIVAVentasServiceImpl libroIVAVentasServiceMock;
    @Mock
    private ParametrosService parametrosServiceMock;

    @BeforeEach
    void setUp() {
        initMocks(this);
        service = new RegimenInformativoServiceImpl(libroIVAVentasServiceMock,
                parametrosServiceMock);
    }

    @Test
    void shouldGetRegimenInformativo() {
        final LibroIVASearchFilter sf = LibroIVASearchFilter.builder()
                .idPeriodo(1L).build();
        when(libroIVAVentasServiceMock.obtenerLibroIVA(sf)).thenReturn(buildLibroIva());
        when(parametrosServiceMock.getStringParam(Parametros.CUIT_EMPRESA)).thenReturn("209999999991");

        final RegimenInformativoVentas regimenInformativoVentas = service.getRegimenInformativoVentas(sf);

        assertNotNull(regimenInformativoVentas);
        final ReginfoCvCabecera cabecera = regimenInformativoVentas.getCabecera();
        assertNotNull(cabecera);
        assertThat(cabecera.getAnioPeriodo(), is(2020));
        assertThat(cabecera.getMesPeriodo(), is(1));
        assertThat(cabecera.getCuitInformante(), is("209999999991"));
        assertThat(cabecera.getCreditoFiscalComputableGlobalOPorComprobante(), is("2"));
        assertThat(cabecera.getSecuencia(), is(0));
        assertThat(cabecera.getProrratearCreditoFiscalComputable(), is("N"));
        assertThat(cabecera.getImporteCreditoFiscalComputableContribSegSocyOtros(), is(BigDecimal.ZERO));

        assertThat(CollectionUtils.isNotEmpty(regimenInformativoVentas.getComprobantes()), is(true));
        assertThat(CollectionUtils.isNotEmpty(regimenInformativoVentas.getAlicuotas()), is(true));

        assertThat(regimenInformativoVentas.getComprobantes().size(), is(1));
        assertThat(regimenInformativoVentas.getAlicuotas().size(), is(1));

    }

    private LibroIVADTO buildLibroIva() {
        return LibroIVADTO.builder()
                .fechaDesde(LocalDateTime.of(2020, 1, 1, 0, 0))
                .fechaHasta(LocalDateTime.of(2020, 1, 31, 23, 59))
                .facturasList(List.of(RegistroIVADTO.builder()
                        .codigoTipoComprobante("99")
                        .documentoCliente("13245678")
                        .fechaFactura(LocalDateTime.of(2020, 1, 2, 14, 0))
                        .letraFactura("B")
                        .numeroComprobante("00000005")
                        .puntoVenta("0001")
                        .tipoDocumentoFiscal(99)
                        .totalFactura(BigDecimal.valueOf(500))
                        .razonSocialCliente("TEST CUSTOMER")
                        .totalAlicuota(List.of(ImportesAlicuotasIVA.builder()
                                .alicuota(FiscalAlicuotasIvaDto.builder().fiscalCodigoAlicuota(95).build())
                                .importeIva(BigDecimal.valueOf(105))
                                .netoGravado(BigDecimal.valueOf(395))
                                .build()))
                        .build()))
                .build();
    }
}