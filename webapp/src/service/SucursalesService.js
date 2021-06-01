import { post } from "../utils/HTTPService"

class SucursalesService {
  retrieveActiveSucursales(successCallback) {
    const activeSucursalesFilter = {
      activa: true,
    }

    post(`/sucursales/search-all`, activeSucursalesFilter, successCallback)
  }
}

export default new SucursalesService()
