import axios from "axios"
import FileSaver from "file-saver"

class FileOutputsService {
  getSaleBudget(saleId) {
    axios
      .get(`/downloads/budget?saleId=${saleId}`, { responseType: "blob" })
      .then((response) => {
        let fileName = response.headers["content-disposition"].split("filename=")[1]

        FileSaver.saveAs(response.data, fileName)
      })
  }

  getInvoice(saleId) {
    axios
      .get(`/downloads/invoice?saleId=${saleId}`, { responseType: "blob" })
      .then((response) => {
        let fileName = response.headers["content-disposition"].split("filename=")[1]

        FileSaver.saveAs(response.data, fileName)
      })
  }

  getDeliveryNote(deliveryNoteId) {
    axios
      .get(`/downloads/deliveryNote?deliveryNoteId=${deliveryNoteId}`, {
        responseType: "blob",
      })
      .then((response) => {
        let fileName = response.headers["content-disposition"].split("filename=")[1]

        FileSaver.saveAs(response.data, fileName)
      })
  }
}

export default new FileOutputsService()
