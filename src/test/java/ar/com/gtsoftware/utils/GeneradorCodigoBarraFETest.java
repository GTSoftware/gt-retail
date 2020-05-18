package ar.com.gtsoftware.utils;

import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;
import ar.com.gtsoftware.domain.FiscalTiposComprobante;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class GeneradorCodigoBarraFETest {

    @Test
    public void shouldGetCodeBar() {

        FiscalLibroIvaVentas registro = new FiscalLibroIvaVentas();
        registro.setPuntoVentaFactura("0002");
        registro.setCae(66107895270467L);
        registro.setFechaVencimientoCae(LocalDate.parse("20160314", DateTimeFormatter.ofPattern("yyyyMMdd")));
        FiscalTiposComprobante tipoComp = new FiscalTiposComprobante();
        tipoComp.setCodigoTipoComprobante("11");
        registro.setCodigoTipoComprobante(tipoComp);
        String cuitEmpresa = "20342577157";

        String expResult = "2034257715711000266107895270467201603148";
        String result = GeneradorCodigoBarraFE.calcularCodigoBarras(registro, cuitEmpresa);

        Assertions.assertEquals(expResult, result);
    }

    @Test
    public void shouldGetCodeBarWhenNoCaePresent() {

        FiscalLibroIvaVentas registro = new FiscalLibroIvaVentas();
        registro.setPuntoVentaFactura("0002");
        registro.setCae(null);
        registro.setFechaVencimientoCae(null);
        FiscalTiposComprobante tipoComp = new FiscalTiposComprobante();
        tipoComp.setCodigoTipoComprobante("11");
        registro.setCodigoTipoComprobante(tipoComp);
        String cuitEmpresa = "20342577157";

        String expResult = "2034257715711000200000000000000000000003";
        String result = GeneradorCodigoBarraFE.calcularCodigoBarras(registro, cuitEmpresa);

        Assertions.assertEquals(expResult, result);
    }
}