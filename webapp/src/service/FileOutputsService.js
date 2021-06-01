import { getWithFileDownload } from "../utils/HTTPService"

class FileOutputsService {
  getSaleBudget(saleId) {
    getWithFileDownload(`/downloads/budget?saleId=${saleId}`)
  }

  getInvoice(saleId) {
    getWithFileDownload(`/downloads/invoice?saleId=${saleId}`)
  }

  getDeliveryNote(deliveryNoteId) {
    getWithFileDownload(`/downloads/deliveryNote?deliveryNoteId=${deliveryNoteId}`)
  }
}

export default new FileOutputsService()
