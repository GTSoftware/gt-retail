package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.ImportesAlicuotasIVA;
import ar.com.gtsoftware.dto.LibroIVADTO;
import ar.com.gtsoftware.dto.RegimenInformativoVentas;
import ar.com.gtsoftware.dto.RegistroIVADTO;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoCvVentasAlicuotas;
import ar.com.gtsoftware.dto.fiscal.reginfo.ReginfoCvCabecera;
import ar.com.gtsoftware.dto.fiscal.reginfo.ReginfoCvVentasCbte;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.impl.LibroIVAVentasServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegimenInformativoServiceImpl implements RegimenInformativoService {
    //    private static final BigDecimal MONTO_MAXIMO_SIN_IDENTIFICAR = BigDecimal.valueOf(1000);
//    private static final int CODIGO_DOCUMENTO_SIN_IDENTIFICAR = 99;
    private static final String SI = "S";
    private static final String NO = "N";
    private static final String PESOS = "PES";

    private final LibroIVAVentasServiceImpl libroIVAVentasServiceImpl;
    private final ParametrosService parametrosService;

    @Override
    public RegimenInformativoVentas getRegimenInformativoVentas(LibroIVASearchFilter filter) {
        final LibroIVADTO libroIVADTO = libroIVAVentasServiceImpl.obtenerLibroIVA(filter);

        ReginfoCvCabecera cabecera = buildCabecera(libroIVADTO);

        List<ReginfoCvVentasCbte> regVentas = new LinkedList<>();
        List<RegInfoCvVentasAlicuotas> regAlicuotas = new LinkedList<>();

        for (RegistroIVADTO factura : libroIVADTO.getFacturasList()) {
            ReginfoCvVentasCbte registro = buildRegistroComprobante(factura);

            for (ImportesAlicuotasIVA alicuotasIVA : factura.getTotalAlicuota()) {
                RegInfoCvVentasAlicuotas alicuotaComprobante = buildRegistroAlicuota(registro, alicuotasIVA);

                regAlicuotas.add(alicuotaComprobante);
            }

            regVentas.add(registro);
        }

        return RegimenInformativoVentas.builder()
                .cabecera(cabecera)
                .comprobantes(regVentas)
                .alicuotas(regAlicuotas)
                .build();
    }

    private RegInfoCvVentasAlicuotas buildRegistroAlicuota(ReginfoCvVentasCbte registro, ImportesAlicuotasIVA alicuotasIVA) {
        return RegInfoCvVentasAlicuotas.builder()
                .alicuota(alicuotasIVA.getAlicuota().getFiscalCodigoAlicuota())
                .importeNetoGravado(alicuotasIVA.getNetoGravado())
                .impuestoLiquidado(alicuotasIVA.getImporteIva())
                .numeroComprobante(registro.getNumeroComprobante())
                .puntoVenta(registro.getPuntoVenta())
                .tipoComprobante(registro.getTipoComprobante())
                .build();
    }

    private ReginfoCvVentasCbte buildRegistroComprobante(RegistroIVADTO factura) {
        return ReginfoCvVentasCbte.builder()
                .codigoMoneda(PESOS)
                .tipoCambio(BigDecimal.ONE)
                .cantidadAlicuotasIVA(factura.getTotalAlicuota().size())
                .codigoDocumentoComprador(factura.getTipoDocumentoFiscal())
                .denominacionComprador(factura.getRazonSocialCliente())
                .numeroIdentificacionComprador(factura.getDocumentoCliente())
                .fechaComprobante(factura.getFechaFactura().toLocalDate())
                .fechaVencimientoPago(factura.getFechaFactura().toLocalDate())
                .importeDePercepcionesDeImpuestosMunicipales(BigDecimal.ZERO)
                .importeDePercepcionesDeIngresosBrutos(BigDecimal.ZERO)
                .importeDePercepcionesOPagosAcuentaDeImpuestosNacionales(BigDecimal.ZERO)
                .importeOperacionesExentas(BigDecimal.ZERO)
                .importeTotalConceptosNoIntegranPrecioNetoGravado(BigDecimal.ZERO)
                .importeTotalOperacion(factura.getTotalFactura())
                .numeroComprobante(factura.getNumeroFactura())
                .puntoVenta(factura.getPuntoVenta())
                .numeroComprobanteHasta(factura.getNumeroComprobante())
                .otrosTributos(BigDecimal.ZERO)
                .percepcionANoCategorizados(BigDecimal.ZERO)
                .tipoComprobante(factura.getCodigoTipoComprobante())
                .codigoOperacion("3")//Productos y servicios
                .build();
    }

    private ReginfoCvCabecera buildCabecera(LibroIVADTO libroIVADTO) {
        final String cuitInformante = parametrosService.getStringParam(Parametros.CUIT_EMPRESA);

        return ReginfoCvCabecera.builder()
                .anioPeriodo(libroIVADTO.getFechaDesde().getYear())
                .mesPeriodo(libroIVADTO.getFechaDesde().getMonthValue())
                .cuitInformante(cuitInformante)
                .secuencia(0)
                .creditoFiscalComputableGlobalOPorComprobante("2") //Por comprobante
                .importeCreditoFiscalComputableContribSegSocyOtros(BigDecimal.ZERO)
                .prorratearCreditoFiscalComputable(NO)
                .sinMovimiento(CollectionUtils.isEmpty(libroIVADTO.getFacturasList()) ? SI : NO)
                .build();
    }
}
