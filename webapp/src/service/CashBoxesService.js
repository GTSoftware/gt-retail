import { get, post } from "../utils/HTTPService"

export class CashBoxesService {
  getCurrentUserCashBox(successCallback, errorCallback) {
    get(`/cashboxes/my-box`, successCallback, errorCallback)
  }

  searchMovements(searchOptions, successCallback, errorCallback) {
    post(`/cashboxes/movements/search`, searchOptions, successCallback, errorCallback)
  }

  openBox(successCallback, errorCallback) {
    post(`/cashboxes/my-box/open`, {}, successCallback, errorCallback)
  }

  closeBox(closeBoxRequest, successCallback, errorCallback) {
    post(`/cashboxes/my-box/close`, closeBoxRequest, successCallback, errorCallback)
  }
}
