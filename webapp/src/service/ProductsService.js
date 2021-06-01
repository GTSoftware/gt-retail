import _ from "lodash"
import { post, get, put, patch } from "../utils/HTTPService"
import { toUpperCaseTrim } from "../utils/StringUtils"

export class ProductsService {
  searchProducts(searchOptions, successCallback, errorCallback) {
    post(`/products/search`, searchOptions, successCallback, errorCallback)
  }

  getProduct(productId, successCallback, errorCallback) {
    get(`/products/${productId}`, successCallback, errorCallback)
  }

  getSupplyTypes(successCallback) {
    get(`/products/supply-types`, successCallback)
  }

  getPercentTypes(successCallback) {
    get(`/products/pricing/percent-types`, successCallback)
  }

  updateProductsPricing(updateOptions, successCallback, errorCallback) {
    const updateRequest = transformUpdatePrices(updateOptions)
    put(`/products/pricing`, updateRequest, successCallback, errorCallback)
  }

  getPriceLists(successCallback) {
    get(`/products/price-list`, successCallback)
  }

  updateProduct(product, successCallback, errorCallback) {
    patch(
      `/products/${product.productId}`,
      transformProduct(product),
      successCallback,
      errorCallback
    )
  }
}

function transformProduct(product) {
  let result = { ...product }

  result.salePrices = result.salePrices.map((salePrice) => {
    return {
      salePriceId: salePrice.salePriceId,
      priceListId: _.get(salePrice, "priceList.priceListId"),
      utility: salePrice.utility,
      netPrice: salePrice.netPrice,
      finalPrice: salePrice.finalPrice,
      version: salePrice.version,
    }
  })
  result.percentages = result.percentages.map((percent) => {
    return {
      productPricePercentId: _.get(percent, "productPricePercentId"),
      percentTypeId: _.get(percent, "percentType.percentTypeId"),
      rate: percent.rate,
      version: percent.version,
    }
  })
  result.saleUnitTypeId = _.get(result, "saleUnit.unitTypeId")
  result.purchaseUnitTypeId = _.get(result, "purchaseUnit.unitTypeId")
  result.supplyTypeId = _.get(result, "supplyType.supplyTypeId")
  result.categoryId = _.get(result, "category.categoryId")
  result.subCategoryId = _.get(result, "subCategory.subCategoryId")
  result.regularSupplierId = _.get(result, "regularSupplier.personId")
  result.fiscalTaxRateId = _.get(result, "fiscalTaxRate.taxRateId")
  result.brandId = _.get(result, "brand.brandId")
  result.description = toUpperCaseTrim(result.description)
  result.factoryCode = toUpperCaseTrim(result.factoryCode)
  result.code = toUpperCaseTrim(result.code)

  return result
}

function transformUpdatePrices(updateOptions) {
  return {
    searchFilter: updateOptions.searchFilter,
    costUpdatePercent: updateOptions.updateCost ? updateOptions.costPercent : null,
    percentsToDelete: updateOptions.updatePercents
      ? transformPercents(updateOptions.percentsToDelete)
      : null,
    percentsToAdd: updateOptions.updatePercents
      ? transformPercents(updateOptions.percentsToAdd)
      : null,
  }
}

function transformPercents(percents) {
  return percents.map((percent) => {
    return {
      percentTypeId: percent.percentType.percentTypeId,
      percentValue: percent.value,
    }
  })
}
