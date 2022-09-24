import { getWithFileDownload } from "../utils/HTTPService"

class FileOutputsService {
  getSaleBudget(saleId) {
    getWithFileDownload(`/downloads/budget?saleId=${saleId}`)
  }

  getSaleBudgetA4(saleId) {
    getWithFileDownload(`/downloads/budget?saleId=${saleId}&format=A4`)
  }

  getSaleBudgetTicket(saleId) {
    getWithFileDownload(`/downloads/budget?saleId=${saleId}&format=TICKET`)
  }

  getInvoice(saleId) {
    getWithFileDownload(`/downloads/invoice?saleId=${saleId}`)
  }

  getInvoiceA4(saleId) {
    getWithFileDownload(`/downloads/invoice?saleId=${saleId}&format=A4`)
  }

  getInvoiceTicket(saleId) {
    getWithFileDownload(`/downloads/invoice?saleId=${saleId}&format=TICKET`)
  }

  getDeliveryNote(deliveryNoteId) {
    getWithFileDownload(`/downloads/deliveryNote?deliveryNoteId=${deliveryNoteId}`)
  }
}

export default new FileOutputsService()
