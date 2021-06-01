import { post } from "../utils/HTTPService"

export class PaymentPendingSalesService {
  searchSales(searchOptions, successCallback, errorCallback) {
    post(`/sales/payment-pending`, searchOptions, successCallback, errorCallback)
  }

  prepareToPay(selectedSales, successCallback, errorCallback) {
    const prepareToPayRequest = {
      salesIds: selectedSales.map((sale) => {
        return sale.saleId
      }),
    }

    post(
      `/sales/prepare-to-pay`,
      prepareToPayRequest,
      successCallback,
      errorCallback
    )
  }
}
