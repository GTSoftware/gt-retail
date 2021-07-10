import { exportExcel } from "../../utils/ExcelExporter"

const exportToExcel = async (sales) => {
  await exportExcel(transform(sales), "ventas")
}

const transform = (sales) => {
  return sales.map((item) => {
    return {
      "Nro Venta": item.saleId,
      Cliente: item.customer,
      Fecha: item.saleDate,
      Factura: item.invoiceNumber,
      Vendedor: item.user,
      "Condicion de venta": item.saleCondition,
      "Tipo Comprobante": item.saleType,
      Total: item.total,
    }
  })
}

export { exportToExcel }
