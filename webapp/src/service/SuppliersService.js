import { post } from "../utils/HTTPService"
import { serializeDate } from "../utils/DateUtils"

export class SuppliersService {
  searchSuppliers(query, successCallback) {
    const searchData = transformSuppliersSearchData(query)

    post(`/persons/search`, searchData, successCallback)
  }

  searchInvoices(searchCriteria, successCallback) {
    const searchFilter = transformSuppliersInvoiceSearchData(searchCriteria)

    post(`/supplier-invoices/search`, searchFilter, successCallback)
  }
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
