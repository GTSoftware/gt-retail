package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.ImportesAlicuotasIVA;
import ar.com.gtsoftware.dto.LibroIVADTO;
import ar.com.gtsoftware.dto.RegistroIVADTO;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.FiscalAlicuotasIvaService;
import ar.com.gtsoftware.service.impl.LibroIVAComprasServiceImpl;
import ar.com.gtsoftware.service.impl.LibroIVAVentasServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WorkBookFiscalBookService {
  private static final String DD_MM_YYYY_CELL_FORMAT = "dd/mm/yyyy";
  private final LibroIVAVentasServiceImpl libroIVAVentasServiceImpl;
  private final LibroIVAComprasServiceImpl libroIVAComprasServiceImpl;
  private final FiscalAlicuotasIvaService alicuotasIvaService;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public void addSalesFiscalBookSheet(XSSFWorkbook wb, LibroIVASearchFilter sf) {
    LibroIVADTO libro = libroIVAVentasServiceImpl.obtenerLibroIVA(sf);
    createSheet(wb, "IVA Ventas", libro);
  }

  public void addPurchasesFiscalBookSheet(XSSFWorkbook wb, LibroIVASearchFilter sf) {
    LibroIVADTO libro = libroIVAComprasServiceImpl.obtenerLibroIVA(sf);
    createSheet(wb, "IVA Compras", libro);
  }

  private void createSheet(XSSFWorkbook wb, String sheetName, LibroIVADTO libro) {
    XSSFCellStyle styleHeader = wb.createCellStyle();
    XSSFFont fontHeader = wb.createFont();
    fontHeader.setBold(true);
    styleHeader.setFont(fontHeader);

    XSSFCellStyle dateStyle = wb.createCellStyle();
    XSSFCellStyle moneyStyle = wb.createCellStyle();
    CreationHelper createHelper = wb.getCreationHelper();
    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(DD_MM_YYYY_CELL_FORMAT));
    moneyStyle.setDataFormat(createHelper.createDataFormat().getFormat("$ #.##"));

    Sheet sheet = wb.createSheet(sheetName);
    int nroFila = 0;

    Row row = null;
    Cell cell = null;

    row = sheet.createRow(nroFila++);
    cell = row.createCell(0);
    cell.setCellValue(sheetName);
    cell.setCellStyle(styleHeader);

    row = sheet.createRow(nroFila++);
    cell = row.createCell(0);
    cell.setCellValue("Fecha de impresión:");
    cell = row.createCell(1);

    cell.setCellValue(LocalDateTime.now());
    cell.setCellStyle(dateStyle);

    cell = row.createCell(5);
    cell.setCellValue("Periodo:");
    cell = row.createCell(6);
    cell.setCellValue(
        String.format(
            "%s al %s",
            DATE_TIME_FORMATTER.format(libro.getFechaDesde()),
            DATE_TIME_FORMATTER.format(libro.getFechaHasta())));

    row = sheet.createRow(nroFila++);
    List<String> columnHeaderNames = buildBaseColumnHeaderNames();
    final List<FiscalAlicuotasIvaDto> alicuotasIva = alicuotasIvaService.findAll();
    Map<FiscalAlicuotasIvaDto, Integer> columnaAlicuota = new HashMap<>(alicuotasIva.size());
    // Nombres de columna para encabezados por cada alicuota
    for (FiscalAlicuotasIvaDto al : alicuotasIva) {
      columnHeaderNames.add(al.getNombreAlicuotaIva());
      columnaAlicuota.put(al, columnHeaderNames.size() - 1);
    }
    columnHeaderNames.add("Total");

    // Formateo los encabezados
    for (int i = 0; i < columnHeaderNames.size(); i++) {
      cell = row.createCell(i);
      cell.setCellValue(columnHeaderNames.get(i));
      cell.setCellStyle(styleHeader);
    }
    // Armo el detalle de cada comprobante
    for (RegistroIVADTO fact : libro.getFacturasList()) {
      int colNum = 0;
      row = sheet.createRow(nroFila++);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getFechaFactura());
      cell.setCellStyle(dateStyle);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getTipoComprobante());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getNumeroFactura());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getTipoDocumento());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getDocumentoCliente());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getRazonSocialCliente());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getCategoriaIVACliente());

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getNetoGravado().doubleValue());
      cell.setCellStyle(moneyStyle);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getNoGravado().doubleValue());
      cell.setCellStyle(moneyStyle);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getTotalIva().doubleValue());
      cell.setCellStyle(moneyStyle);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getPercepcionIva().doubleValue());
      cell.setCellStyle(moneyStyle);

      cell = row.createCell(colNum++);
      cell.setCellValue(fact.getPercepcionIngresosBrutos().doubleValue());
      cell.setCellStyle(moneyStyle);

      for (ImportesAlicuotasIVA al : fact.getTotalAlicuota()) {

        cell = row.createCell(columnaAlicuota.get(al.getAlicuota()));
        cell.setCellValue(al.getImporteIva().doubleValue());
        cell.setCellStyle(moneyStyle);
      }
      // Columna de total
      cell = row.createCell(columnHeaderNames.size() - 1);
      cell.setCellValue(fact.getTotalFactura().doubleValue());
      cell.setCellStyle(moneyStyle);
    }
    for (int i = 0; i < columnHeaderNames.size(); i++) {
      sheet.autoSizeColumn(i);
    }
    nroFila = nroFila + 3;
    row = sheet.createRow(nroFila++);
    cell = row.createCell(0);
    cell.setCellValue("Alicuota");
    cell.setCellStyle(styleHeader);
    cell = row.createCell(1);
    cell.setCellValue("Importe");
    cell.setCellStyle(styleHeader);

    // Arma encabezados de totales por alícuota
    for (ImportesAlicuotasIVA al : libro.getTotalesAlicuota()) {
      Row alicRow = sheet.createRow(nroFila++);
      Cell alicCell = alicRow.createCell(0);
      alicCell.setCellValue(al.getAlicuota().getNombreAlicuotaIva());

      alicCell = alicRow.createCell(1);
      alicCell.setCellValue(al.getImporteIva().doubleValue());
      alicCell.setCellStyle(moneyStyle);
    }

    cell = row.createCell(3);
    cell.setCellValue("Facturación total:");
    cell.setCellStyle(styleHeader);
    cell = row.createCell(4);
    cell.setCellValue(libro.getImporteTotal().doubleValue());
    cell.setCellStyle(moneyStyle);
  }

  private List<String> buildBaseColumnHeaderNames() {
    List<String> columnHeaderNames = new LinkedList<>();
    columnHeaderNames.add("Fecha");
    columnHeaderNames.add("Tipo");
    columnHeaderNames.add("Número");
    columnHeaderNames.add("Tipo Doc");
    columnHeaderNames.add("Documento");
    columnHeaderNames.add("Razón Social");
    columnHeaderNames.add("Cat. IVA");
    columnHeaderNames.add("Neto Gravado");
    columnHeaderNames.add("No Grav.");
    columnHeaderNames.add("IVA Total");
    columnHeaderNames.add("Perc. IVA");
    columnHeaderNames.add("Perc. Ing.Br.");

    return columnHeaderNames;
  }
}
