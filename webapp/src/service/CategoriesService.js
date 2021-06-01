import { get } from "../utils/HTTPService"

export class CategoriesService {
  getCategories(successCallback) {
    get(`/products/categories`, successCallback)
  }

  getSubCategories(categoryId, successCallback) {
    get(`/products/categories/${categoryId}/sub-categories`, successCallback)
  }
}
