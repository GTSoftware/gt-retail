import { post } from "../utils/HTTPService"
import { serializeDate } from "../utils/DateUtils"
import _ from "lodash"

export class SuppliersService {
  searchSuppliers(query, successCallback) {
    const searchData = transformSuppliersSearchData(query)

    post(`/persons/search`, searchData, successCallback)
  }

  searchInvoices(searchCriteria, successCallback) {
    const searchFilter = transformSuppliersInvoiceSearchData(searchCriteria)

    post(`/supplier-invoices/search`, searchFilter, successCallback)
  }

  storeInvoice(invoiceData, successCallback, errorCallback) {
    const createInvoiceRequest = transformInvoiceData(invoiceData)
    post(`/supplier-invoice/`, createInvoiceRequest, successCallback, errorCallback)
  }
}

function transformInvoiceData(invoiceData) {
  return {
    invoiceDate: serializeDate(invoiceData.invoiceDate),
    notes: invoiceData.notes,
    pointOfSale: invoiceData.pointOfSale,
    invoiceNumber: invoiceData.invoiceNumber,
    invoiceTypeId: invoiceData.invoiceType.id,
    supplierId: invoiceData.supplier.personId,
    fiscalPeriodId: invoiceData.fiscalPeriod.id,
    grossIncomePerceptionAmount: +invoiceData.grossIncomePerceptionAmount,
    taxPerceptionAmount: +invoiceData.taxPerceptionAmount,
    invoiceDetails: transformInvoiceDetails(invoiceData.invoiceDetails),
  }
}

function transformInvoiceDetails(invoiceDetails) {
  return invoiceDetails.map((line) => {
    return {
      taxRateId: line.taxRate.taxRateId,
      taxAmount: line.taxAmount,
      baseAmount: line.baseAmount,
      nonTaxableAmount: line.nonTaxableAmount,
    }
  })
}

function transformSuppliersSearchData(query) {
  return {
    firstResult: 0,
    maxResults: 15,
    searchFilter: {
      txt: query,
      activo: true,
      proveedor: true,
    },
  }
}

function transformSuppliersInvoiceSearchData(searchCriteria) {
  return {
    firstResult: searchCriteria.firstResult,
    maxResults: searchCriteria.maxResults,
    searchFilter: {
      fechaComprobanteDesde: serializeDate(searchCriteria.fromDate),
      fechaComprobanteHasta: serializeDate(searchCriteria.toDate),
      idProveedor: searchCriteria.selectedSupplier?.personId,
    },
  }
}
