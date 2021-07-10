import xlsx from "xlsx"
import FileSaver from "file-saver"
import { serializeDate } from "./DateUtils"

const EXCEL_TYPE =
  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"
const EXCEL_EXTENSION = ".xlsx"

const exportExcel = async (values, fileName) => {
  const worksheet = xlsx.utils.json_to_sheet(values)
  const workbook = { Sheets: { data: worksheet }, SheetNames: ["data"] }
  const excelBuffer = xlsx.write(workbook, { bookType: "xlsx", type: "array" })

  saveAsExcelFile(excelBuffer, fileName)
}

const saveAsExcelFile = (buffer, fileName) => {
  const data = new Blob([buffer], {
    type: EXCEL_TYPE,
  })
  const fullFileName =
    fileName + "_export_" + serializeDate(new Date()) + EXCEL_EXTENSION

  FileSaver.saveAs(data, fullFileName)
}

export { exportExcel }
