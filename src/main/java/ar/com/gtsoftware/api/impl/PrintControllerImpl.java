package ar.com.gtsoftware.api.impl;

import static ar.com.gtsoftware.enums.Parametros.EMPRESA_CUIT;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_DIRECCION;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_EMAIL;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_FECHA_INICIO;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_LOCALIDAD;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_NOMBRE;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_NOMBRE_FANTASIA;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_PROVINCIA;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_RAZON_SOCIAL;
import static ar.com.gtsoftware.enums.Parametros.EMPRESA_TELEFONO;
import static ar.com.gtsoftware.enums.Parametros.FORMATO_IMPRESION;
import static ar.com.gtsoftware.enums.Parametros.PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS;

import ar.com.gtsoftware.api.PrintController;
import ar.com.gtsoftware.api.PrintFormat;
import ar.com.gtsoftware.api.exception.FileGenerationException;
import ar.com.gtsoftware.api.exception.FileNotFoundException;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.RemitoService;
import ar.com.gtsoftware.service.VentasService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PrintControllerImpl implements PrintController {

  private final VentasService ventasService;
  private final ParametrosService parametrosService;
  private final RemitoService remitoService;
  private final HttpServletResponse response;
  private final ResourceLoader resourceLoader;

  private final transient Map<String, JasperReport> REPORTS_MAP = new HashMap<>();

  @Override
  public void getSaleBudget(Long saleId) {
    ComprobantesDto comprobante = ventasService.obtenerComprobante(saleId);
    if (Objects.isNull(comprobante)) {
      handleEntityNotFound("saleId", saleId);
    }
    List<ComprobantesDto> ventas = List.of(comprobante);
    boolean mostrarDetallePrecios =
        parametrosService.getBooleanParam(PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS);

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(ventas);

    Map<String, Object> parameters = loadCompanyParameters();
    parameters.put(PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS.getNombreParametro(), mostrarDetallePrecios);
    parameters.put("subreport", REPORTS_MAP.get("vistaVentas_lineas"));
    String fileName = String.format("venta-%d", saleId);

    handlePDFExport(fileName, beanCollectionDataSource, "presupuesto", parameters);
  }

  @Override
  public void getInvoice(Long saleId, PrintFormat format) {
    ComprobantesDto comprobante = ventasService.obtenerComprobante(saleId);
    if (comprobante == null) {
      handleEntityNotFound("saleId", saleId);
      return;
    }
    List<ComprobantesDto> ventas = List.of(comprobante);

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(ventas);
    JRBeanCollectionDataSource beanCollectionDataSource1 = new JRBeanCollectionDataSource(ventas);
    JRBeanCollectionDataSource beanCollectionDataSource2 = new JRBeanCollectionDataSource(ventas);

    Map<String, Object> parameters = loadCompanyParameters();

    parameters.put("logoAfip", "classpath:images/afip.png");
    parameters.put("codigobarras", comprobante.getCodigoBarrasFactura());
    parameters.put("facturaReport", REPORTS_MAP.get(getReportName("factura", format)));
    parameters.put(
        "alicuotasSubReport", REPORTS_MAP.get(getReportName("vistaVentas_alicuotas", format)));

    if (comprobante.getIdRegistro().getLetraFactura().equals("A")) {
      parameters.put("subreport", REPORTS_MAP.get(getReportName("vistaVentas_lineasNeto", format)));
    } else {
      parameters.put("subreport", REPORTS_MAP.get(getReportName("vistaVentas_lineas", format)));
    }
    parameters.put("subDataSource", beanCollectionDataSource1);
    parameters.put("subDataSource2", beanCollectionDataSource2);

    String fileName = String.format("factura-%d", saleId);

    handlePDFExport(
        fileName, beanCollectionDataSource, getReportName("factura", format), parameters);
  }

  private String getReportName(String baseReportName, PrintFormat desiredFormat) {
    final String ticketToken = "_ticket";
    PrintFormat format =
        Optional.ofNullable(desiredFormat)
            .orElse(PrintFormat.valueOf(parametrosService.getStringParam(FORMATO_IMPRESION)));

    if (PrintFormat.TICKET.equals(format)) {
      return baseReportName + ticketToken;
    }

    return baseReportName;
  }

  @Override
  public void getDeliveryNote(Long deliveryNoteId) {
    RemitoDto remito = remitoService.find(deliveryNoteId);
    if (remito == null) {
      handleEntityNotFound("deliveryNoteId", deliveryNoteId);
    }
    List<RemitoDto> remitos = List.of(remito);

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(remitos);

    Map<String, Object> parameters = loadCompanyParameters();
    parameters.put("subreportParameter", REPORTS_MAP.get("remito_lineas"));

    String fileName = String.format("remito-%d", deliveryNoteId);

    handlePDFExport(fileName, beanCollectionDataSource, "remito", parameters);
  }

  private void handlePDFExport(
      String fileName,
      JRBeanCollectionDataSource beanCollectionDataSource,
      String reportName,
      Map<String, Object> parameters) {

    try {

      JasperPrint jasperPrint =
          JasperFillManager.fillReport(
              REPORTS_MAP.get(reportName), parameters, beanCollectionDataSource);

      response.addHeader(
          HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.pdf", fileName));
      response.setContentType(MediaType.APPLICATION_PDF_VALUE);

      ServletOutputStream servletStream = response.getOutputStream();
      JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);
    } catch (JRException | IOException e) {
      log.error("Error al generar reporte", e);
      throw new FileGenerationException();
    }
  }

  private void handleEntityNotFound(String fieldId, Long id) {
    log.error("Entity not found with {}={}", fieldId, id);
    throw new FileNotFoundException();
  }

  private Map<String, Object> loadCompanyParameters() {
    final Map<String, Object> parametros = new HashMap<>();

    parametros.put(
        EMPRESA_CUIT.getNombreParametro(), parametrosService.getStringParam(EMPRESA_CUIT));
    parametros.put(
        EMPRESA_DIRECCION.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_DIRECCION));
    parametros.put(
        EMPRESA_EMAIL.getNombreParametro(), parametrosService.getStringParam(EMPRESA_EMAIL));
    parametros.put(
        EMPRESA_FECHA_INICIO.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_FECHA_INICIO));
    parametros.put(
        EMPRESA_LOCALIDAD.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_LOCALIDAD));
    parametros.put(
        EMPRESA_NOMBRE.getNombreParametro(), parametrosService.getStringParam(EMPRESA_NOMBRE));
    parametros.put(
        EMPRESA_NOMBRE_FANTASIA.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_NOMBRE_FANTASIA));
    parametros.put(
        EMPRESA_PROVINCIA.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_PROVINCIA));
    parametros.put(
        EMPRESA_RAZON_SOCIAL.getNombreParametro(),
        parametrosService.getStringParam(EMPRESA_RAZON_SOCIAL));
    parametros.put(
        EMPRESA_TELEFONO.getNombreParametro(), parametrosService.getStringParam(EMPRESA_TELEFONO));

    return parametros;
  }

  @PostConstruct
  private void compileReports() {

    final List<String> reports =
        List.of(
            "classpath:reports/factura.jrxml",
            "classpath:reports/factura_ticket.jrxml",
            "classpath:reports/presupuesto.jrxml",
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
    try {
      for (String reportFilePath : reports) {
        log.debug("Compiling report:" + reportFilePath);

        final Resource reportResource = resourceLoader.getResource(reportFilePath);
        JasperReport compiledReport =
            JasperCompileManager.compileReport(reportResource.getInputStream());
        REPORTS_MAP.put(reportResource.getFilename().replace(".jrxml", ""), compiledReport);
      }

    } catch (IOException | JRException ex) {
      log.error("Error compiling reports", ex);
    }
  }
}
