import { SessionStore } from "./SessionStore"
import _ from "lodash"

export class ShopCartStore extends SessionStore {
  getStoreName() {
    return "shopCart"
  }

  getInitialState() {
    return {
      products: [],
      customerInfo: null,
      saleStep: 0,
      saleType: null,
      saleCondition: null,
      payments: null,
      observaciones: null,
      remitente: null,
      remito: null,
    }
  }

  setProducts(products) {
    let storeData = { ...this.getData(), ...{ products: products } }

    this.setData(storeData)
  }

  getProducts() {
    return _.get(this.getData(), "products", [])
  }

  getSaleStep() {
    return _.get(this.getData(), "saleStep")
  }

  setSaleStep(saleStep) {
    let storeData = { ...this.getData(), ...{ saleStep: saleStep } }

    this.setData(storeData)
  }

  getCustomerInfo() {
    return _.get(this.getData(), "customerInfo", null)
  }

  setCustomerInfo(customerInfo) {
    let storeData = { ...this.getData(), ...{ customerInfo: customerInfo } }

    this.setData(storeData)
  }

  getSaleType() {
    return _.get(this.getData(), "saleType", null)
  }

  setSaleType(saleType) {
    let storeData = { ...this.getData(), ...{ saleType: saleType } }

    this.setData(storeData)
  }

  getSaleCondition() {
    return _.get(this.getData(), "saleCondition", null)
  }

  setSaleCondition(saleCondition) {
    let storeData = { ...this.getData(), ...{ saleCondition: saleCondition } }

    this.setData(storeData)
  }

  getPayments() {
    return _.get(this.getData(), "payments", null)
  }

  setPayments(payments) {
    let storeData = { ...this.getData(), ...{ payments: payments } }

    this.setData(storeData)
  }

  getObservacaiones() {
    return _.get(this.getData(), "observaciones", null)
  }

  getRemitente() {
    return _.get(this.getData(), "remitente", null)
  }

  getRemito() {
    return _.get(this.getData(), "remito", null)
  }

  setObservaciones(observaciones) {
    let storeData = { ...this.getData(), ...{ observaciones: observaciones } }

    this.setData(storeData)
  }

  setRemitente(remitente) {
    let storeData = { ...this.getData(), ...{ remitente: remitente } }

    this.setData(storeData)
  }

  setRemito(remito) {
    let storeData = { ...this.getData(), ...{ remito: remito } }

    this.setData(storeData)
  }
}
