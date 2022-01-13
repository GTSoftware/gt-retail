import _ from "lodash"
import { get, patch, post, put } from "../utils/HTTPService"
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

  getUnitTypes(successCallback) {
    get(`/products/unit-types`, successCallback)
  }

  updateProduct(product, successCallback, errorCallback) {
    patch(
      `/products/${product.productId}`,
      transformProduct(product),
      successCallback,
      errorCallback
    )
  }

  createProduct(product, successCallback, errorCallback) {
    post(`/products`, transformProduct(product), successCallback, errorCallback)
  }

  validateProductCode(product, errorCallback) {
    const searchParams = new URLSearchParams("")

    searchParams.set("productCode", product.code)

    if (product.productId) {
      searchParams.set("productId", product.productId)
    }

    get(`/products/valid-code?${searchParams}`, () => {}, errorCallback)
  }
}

function transformProduct(product) {
  let result = {}

  result.productId = product.productId
  result.version = product.version
  result.observations = product.observations
  result.active = product.active
  result.salePrices = product.salePrices.map((salePrice) => {
    return {
      salePriceId: salePrice.salePriceId,
      priceListId: _.get(salePrice, "priceList.priceListId"),
      utility: salePrice.utility,
      netPrice: salePrice.netPrice,
      finalPrice: salePrice.finalPrice,
      version: salePrice.version,
    }
  })
  result.percentages = product.percentages.map((percent) => {
    return {
      productPricePercentId: _.get(percent, "productPricePercentId"),
      percentTypeId: _.get(percent, "percentType.percentTypeId"),
      rate: percent.rate,
      version: percent.version,
    }
  })
  result.netCost = product.netCost
  result.grossCost = product.grossCost
  result.purchaseUnitsToSaleUnitEquivalence =
    product.purchaseUnitsToSaleUnitEquivalence
  result.minimumStock = product.minimumStock
  result.saleUnitTypeId = _.get(product, "saleUnit.unitTypeId")
  result.purchaseUnitTypeId = _.get(product, "purchaseUnit.unitTypeId")
  result.supplyTypeId = _.get(product, "supplyType.supplyTypeId")
  result.categoryId = _.get(product, "category.categoryId")
  result.subCategoryId = _.get(product, "subCategory.subCategoryId")
  result.regularSupplierId = _.get(product, "regularSupplier.personId")
  result.fiscalTaxRateId = _.get(product, "fiscalTaxRate.taxRateId")
  result.brandId = _.get(product, "brand.brandId")
  result.description = toUpperCaseTrim(product.description)
  result.factoryCode = toUpperCaseTrim(product.factoryCode)
  result.code = toUpperCaseTrim(product.code)

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
