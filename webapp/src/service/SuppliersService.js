import axios from "axios"

export class SuppliersService {
  searchSuppliers(query, successCallback) {
    let searchData = transformSuppliersSearchData(query)
    let promise = axios.post(`/persons/search`, searchData)

    if (successCallback) {
      promise.then((response) => successCallback(response.data.data))
    }
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
