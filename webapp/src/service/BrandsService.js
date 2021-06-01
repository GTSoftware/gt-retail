import { get } from "../utils/HTTPService"

export class BrandsService {
  getBrands(successCallback) {
    get(`/products/brands`, successCallback)
  }
}
