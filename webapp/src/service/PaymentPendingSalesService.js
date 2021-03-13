import axios from "axios"

export class PaymentPendingSalesService {
  searchSales(searchOptions, successCallback, errorCallback) {
    let promise = axios.post(`/sales/payment-pending`, searchOptions)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error.response.data))
    }
  }

  prepareToPay(selectedSales, successCallback, errorCallback) {
    const prepareToPayRequest = {
      salesIds: selectedSales.map((sale) => {
        return sale.saleId
      }),
    }
    let promise = axios.post(`/sales/prepare-to-pay`, prepareToPayRequest)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error.response.data))
    }
  }
}
