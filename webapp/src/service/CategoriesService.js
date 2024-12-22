import { get, post, put } from "../utils/HTTPService"
import { toUpperCaseTrim } from "../utils/StringUtils"

export class CategoriesService {
  getCategories(successCallback) {
    get(`/products/categories`, successCallback)
  }

  createCategory(categoryData, successCallback, errorCallback) {
    post(
      `/products/categories`,
      transformCategory(categoryData),
      successCallback,
      errorCallback
    )
  }

  updateCategory(categoryId, categoryData, successCallback, errorCallback) {
    put(
      `/products/category/${categoryId}`,
      transformCategory(categoryData),
      successCallback,
      errorCallback
    )
  }

  getSubCategories(categoryId, successCallback) {
    get(`/products/category/${categoryId}/sub-categories`, successCallback)
  }
}

const transformCategory = (categoryData) => {
  let result = {}
  result.categoryName = toUpperCaseTrim(categoryData.categoryName)
  result.version = categoryData.version

  return result
}
