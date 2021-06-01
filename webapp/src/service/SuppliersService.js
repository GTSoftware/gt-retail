import { post } from "../utils/HTTPService"

export class SuppliersService {
  searchSuppliers(query, successCallback) {
    const searchData = transformSuppliersSearchData(query)

    post(`/persons/search`, searchData, successCallback)
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
