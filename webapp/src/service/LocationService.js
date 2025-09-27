import { get, getV2, postV2 } from "../utils/HTTPService"
import { toUpperCaseTrim } from "../utils/StringUtils"

export class LocationService {
  getCountries(successCallback) {
    get(`/locations/countries`, successCallback)
  }

  getProvinces(countryId, successCallback) {
    get(`/locations/provinces?countryId=${countryId}`, successCallback)
  }

  async getTowns(provinceId, query) {
    return getV2(`/locations/towns?provinceId=${provinceId}&query=${query}`)
  }

  async addTown(data) {
    return await postV2(`/locations/towns`, transformTown(data))
  }
}

const transformTown = (data) => {
  return {
    name: toUpperCaseTrim(data.name),
    postalCode: toUpperCaseTrim(data.postalCode),
    provinceId: data.provinceId,
  }
}
