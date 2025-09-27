import { get, post, put, del } from "../utils/HTTPService"

export class WarehousesService {
  list(successCallback, errorCallback) {
    get(`/warehouses`, successCallback, errorCallback)
  }

  getById(warehouseId, successCallback, errorCallback) {
    get(`/warehouses/${warehouseId}`, successCallback, errorCallback)
  }

  create(data, successCallback, errorCallback) {
    post(`/warehouses`, data, successCallback, errorCallback)
  }

  update(warehouseId, data, successCallback, errorCallback) {
    put(`/warehouses/${warehouseId}`, data, successCallback, errorCallback)
  }

  delete(warehouseId, successCallback, errorCallback) {
    del(`/warehouses/${warehouseId}`, successCallback, errorCallback)
  }
}
