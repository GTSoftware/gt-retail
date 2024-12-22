import { get, post, put } from "../utils/HTTPService"
import { toUpperCaseTrim } from "../utils/StringUtils"

export class CategoriesService {
  getCategories(successCallback) {
    get(`/products/categories`, successCallback)
  }

  getCategory(categoryId, successCallback) {
    get(`/products/category/${categoryId}`, successCallback)
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

  createSubCategory(categoryId, subCategoryData, successCallback, errorCallback) {
    post(
      `/products/category/${categoryId}/sub-categories`,
      transformSubCategory(subCategoryData),
      successCallback,
      errorCallback
    )
  }

  updateSubCategory(subCategoryData, successCallback, errorCallback) {
    put(
      `/products/category/${subCategoryData.categoryId}/sub-category/${subCategoryData.subCategoryId}`,
      transformSubCategory(subCategoryData),
      successCallback,
      errorCallback
    )
  }
}

const transformCategory = (categoryData) => {
  let result = {}
  result.categoryName = toUpperCaseTrim(categoryData.categoryName)
  result.version = categoryData.version

  return result
}

const transformSubCategory = (subCategoryData) => {
  let result = {}
  result.subCategoryName = toUpperCaseTrim(subCategoryData.subCategoryName)
  result.version = subCategoryData.version
  result.categoryId = subCategoryData.categoryId
  result.subCategoryId = subCategoryData.subCategoryId

  return result
}
