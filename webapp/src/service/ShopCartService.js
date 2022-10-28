import { get, post } from "../utils/HTTPService"
import _ from "lodash"

class ShopCartService {
  addProduct(searchData, successCallback, errorCallback) {
    post(`/shop-cart/add-product`, searchData, successCallback, errorCallback)
  }

  getDefaultCustomer(successCallback, errorCallback) {
    get(`/shop-cart/default-customer`, successCallback, errorCallback)
  }

  searchCustomers(query, successCallback) {
    get(`/shop-cart/customers-search?query=${query}`, successCallback)
  }

  getSaleTypes(successCallback) {
    get(`/shop-cart/sale-types`, successCallback)
  }

  getSaleConditions(successCallback) {
    get(`/shop-cart/sale-conditions`, successCallback)
  }

  getPaymentMethods(successCallback) {
    get(`/shop-cart/payment-methods`, successCallback)
  }

  saveSale(shopCart, successCallback, errorCallback) {
    let saleRequest = transformToSaleRequest(shopCart)

    post(`/shop-cart/sale`, saleRequest, successCallback, errorCallback)
  }

  getPaymentPlans(paymentMethod, successCallback) {
    get(
      `/shop-cart/payment-plans?paymentPlanId=${paymentMethod.id}`,
      successCallback
    )
  }
}

function transformToSaleRequest(shopCart) {
  return {
    customerId: shopCart.customerInfo.customerId,
    saleTypeId: shopCart.saleType.id,
    saleConditionId: shopCart.saleCondition.id,
    observaciones: shopCart.observaciones,
    remito: shopCart.remito,
    remitente: shopCart.remitente,
    products: shopCart.products,
    payments: transformPayments(shopCart.payments),
    shopCartId: shopCart.shopCartId,
  }
}

function transformPayments(payments) {
  return payments.map((payment) => {
    return {
      paymentMethodId: payment.idFormaPago.id,
      paymentAmount: payment.montoPago,
      paymentPlanId: _.get(payment, "idPlan.id"),
      paymentPlanDetailId: _.get(payment, "idDetallePlan.id"),
    }
  })
}

export default new ShopCartService()
