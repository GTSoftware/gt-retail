import { get, post } from "../utils/HTTPService"

export class SalesService {
  searchSales(searchOptions, successCallback, errorCallback) {
    post(`/sales/search`, searchOptions, successCallback, errorCallback)
  }

  getSalesTotals(searchOptions, successCallback) {
    post(`/sales/totals`, searchOptions, successCallback)
  }

  getSale(saleId, successCallback, errorCallback) {
    get(`/sale/${saleId}`, successCallback, errorCallback)
  }

  getPointsOfSale(successCallback, errorCallback) {
    get(`/points-of-sale`, successCallback, errorCallback)
  }

  registerInvoice(invoiceToRegister, successCallback, errorCallback) {
    post(`/invoice`, invoiceToRegister, successCallback, errorCallback)
  }
}
