import { post } from "../utils/HTTPService"

export class StockService {
  getProductMovementsHistory(movementsFilter, successCallback, errorCallback) {
    post(`/stock/product-movements`, movementsFilter, successCallback, errorCallback)
  }
}
