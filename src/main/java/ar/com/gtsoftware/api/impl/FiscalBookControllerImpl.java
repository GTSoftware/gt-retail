package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.FiscalBookController;
import ar.com.gtsoftware.api.exception.FileGenerationException;
import ar.com.gtsoftware.api.request.DigitalFiscalBookRequest;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegimenInformativoCompras;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegimenInformativoVentas;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.fiscal.RegimenInformativoService;
import ar.com.gtsoftware.service.fiscal.WorkBookFiscalBookService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FiscalBookControllerImpl implements FiscalBookController {
  private static final String CRLF = StringUtils.CR + StringUtils.LF;
  private static final String APPLICATION_ZIP_CONTENT_TYPE = "application/zip";
  private static final String APPLICATION_MS_EXCEL_2007_CONTENT_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  private final RegimenInformativoService regimenInformativoService;
  private final WorkBookFiscalBookService workBookFiscalBookService;
  private final HttpServletResponse response;

  private static LibroIVASearchFilter mapToLibroIvaFilter(DigitalFiscalBookRequest filter) {
    return LibroIVASearchFilter.builder()
        .idPeriodo(filter.getFiscalPeriodId())
        .fechaDesde(filter.getFromDate())
        .fechaHasta(filter.getToDate())
        .anuladas(false)
        .build();
  }

  @Override
  public void getInformativeRegime(@Valid DigitalFiscalBookRequest filter) {
    LibroIVASearchFilter sf = mapToLibroIvaFilter(filter);
    RegimenInformativoVentas regimenInformativoVentas = null;
    RegimenInformativoCompras regimenInformativoCompras = null;

    switch (filter.getKind()) {
      case SALES:
        regimenInformativoVentas = regimenInformativoService.getRegimenInformativoVentas(sf);
        break;
      case PURCHASES:
        regimenInformativoCompras = regimenInformativoService.getRegimenInformativoCompras(sf);
        break;
      default:
        regimenInformativoVentas = regimenInformativoService.getRegimenInformativoVentas(sf);
        regimenInformativoCompras = regimenInformativoService.getRegimenInformativoCompras(sf);
        break;
    }

    Map<String, String> textFiles =
        buildTextFiles(regimenInformativoCompras, regimenInformativoVentas);

    handleZipExport(textFiles);
  }

  @Override
  public void getFiscalBook(@Valid DigitalFiscalBookRequest filter) {
    LibroIVASearchFilter sf = mapToLibroIvaFilter(filter);

    try (XSSFWorkbook wb = new XSSFWorkbook();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        CountingOutputStream countOs = new CountingOutputStream(servletOutputStream)) {
      switch (filter.getKind()) {
        case SALES:
          workBookFiscalBookService.addSalesFiscalBookSheet(wb, sf);
          break;
        case PURCHASES:
          workBookFiscalBookService.addPurchasesFiscalBookSheet(wb, sf);
          break;
        default:
          workBookFiscalBookService.addPurchasesFiscalBookSheet(wb, sf);
          workBookFiscalBookService.addSalesFiscalBookSheet(wb, sf);
          break;
      }
      handleWorkBookExport(wb, countOs);
    } catch (IOException ioe) {
      final String message = "There was an error trying to generate XLSX file";
      log.error(message, ioe);

      throw new FileGenerationException(message);
    }
  }

  private void handleWorkBookExport(XSSFWorkbook wb, CountingOutputStream os) throws IOException {
    response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=LibroIVA.xlsx");
    response.setContentType(APPLICATION_MS_EXCEL_2007_CONTENT_TYPE);
    wb.write(os);
    response.setContentLengthLong(os.getByteCount());
  }

  private void handleZipExport(Map<String, String> textFiles) {
    response.addHeader(
        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=RegimenInformativo.zip");
    response.setContentType(APPLICATION_ZIP_CONTENT_TYPE);

    try (ServletOutputStream servletOutputStream = response.getOutputStream();
        CountingOutputStream countOs = new CountingOutputStream(servletOutputStream);
        ZipOutputStream zout = new ZipOutputStream(countOs)) {
      zout.setComment("Generated on: " + LocalDateTime.now());

      for (Map.Entry<String, String> fileEntry : textFiles.entrySet()) {
        ZipEntry entry = new ZipEntry(fileEntry.getKey() + ".txt");
        zout.putNextEntry(entry);
        zout.write(fileEntry.getValue().getBytes(StandardCharsets.US_ASCII));
        zout.closeEntry();
      }
      zout.finish();

      response.setContentLengthLong(countOs.getByteCount());
    } catch (IOException ioe) {
      final String message = "There was an error trying to generate ZIP file";
      log.error(message, ioe);

      throw new FileGenerationException(message);
    }
  }

  private Map<String, String> buildTextFiles(
      RegimenInformativoCompras compras, RegimenInformativoVentas ventas) {
    Map<String, String> textFiles = new HashMap<>();
    if (Objects.nonNull(compras)) {
      textFiles.put("REGINFO_CV_COMPRAS_CBTE", buildTextFile(compras.getComprobantes(), 325));
      textFiles.put("REGINFO_CV_COMPRAS_ALICUOTAS", buildTextFile(compras.getAlicuotas(), 84));
    }
    if (Objects.nonNull(ventas)) {
      textFiles.put("REGINFO_CV_VENTAS_CBTE", buildTextFile(ventas.getComprobantes(), 266));
      textFiles.put("REGINFO_CV_VENTAS_ALICUOTAS", buildTextFile(ventas.getAlicuotas(), 62));
      textFiles.put("REGINFO_CV_CABECERA", ventas.getCabecera().toString());
    }

    return textFiles;
  }

  private <T> String buildTextFile(List<T> records, int lineLength) {
    StringBuilder text = new StringBuilder((lineLength + CRLF.length()) * records.size());
    for (T record : records) {
      text.append(record.toString()).append(CRLF);
    }
    return text.toString();
  }
}
