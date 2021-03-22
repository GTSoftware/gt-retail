import axios from "axios"

export class BrandsService {
  getBrands(successCallback) {
    let promise = axios.get(`/products/brands`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }
}
