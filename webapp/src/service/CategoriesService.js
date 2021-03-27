import axios from "axios"

export class CategoriesService {
  getCategories(successCallback) {
    let promise = axios.get(`/products/categories`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getSubCategories(categoryId, successCallback) {
    let promise = axios.get(`/products/categories/${categoryId}/sub-categories`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }
}
