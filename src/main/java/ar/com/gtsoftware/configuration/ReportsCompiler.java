package ar.com.gtsoftware.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ReportsCompiler {

  private final ResourceLoader resourceLoader;

  private static final List<String> REPORTS =
          List.of(
                  "classpath:reports/factura.jrxml",
                  "classpath:reports/factura_ticket.jrxml",
                  "classpath:reports/presupuesto.jrxml",
                  "classpath:reports/presupuesto_ticket.jrxml",
                  "classpath:reports/productoEtiqueta.jrxml",
                  "classpath:reports/recibo.jrxml",
                  "classpath:reports/reciboDetalle.jrxml",
                  "classpath:reports/remito.jrxml",
                  "classpath:reports/remito_lineas.jrxml",
                  "classpath:reports/vistaVentas_alicuotas.jrxml",
                  "classpath:reports/vistaVentas_alicuotas_ticket.jrxml",
                  "classpath:reports/vistaVentas_lineas.jrxml",
                  "classpath:reports/vistaVentas_lineas_ticket.jrxml",
                  "classpath:reports/vistaVentas_lineasNeto.jrxml",
                  "classpath:reports/vistaVentas_lineasNeto_ticket.jrxml");

  @Bean
  public Map<String, JasperReport> getCompiledReports() {
    Map<String, JasperReport> reportsMap = new HashMap<>();

      for (String reportFilePath : REPORTS) {
        log.debug("Compiling report:" + reportFilePath);
        try {
          Resource reportResource = getReportResource(reportFilePath);
          JasperReport compiledReport = tryCompileReport(reportResource);
          reportsMap.put(getKeyName(reportResource), compiledReport);
        } catch (IOException | JRException ex) {
          log.error(String.format("Error compiling report: %s", reportFilePath), ex);
        }
      }

    return Collections.unmodifiableMap(reportsMap);
  }

  private String getKeyName(Resource reportResource) {
    return reportResource.getFilename().replace(".jrxml", "");
  }

  private Resource getReportResource(String reportFilePath) {
    return resourceLoader.getResource(reportFilePath);
  }
  private JasperReport tryCompileReport(Resource reportResource) throws IOException, JRException {
    return JasperCompileManager.compileReport(reportResource.getInputStream());
  }
}
