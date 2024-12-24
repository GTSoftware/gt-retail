import { get } from "../utils/HTTPService"

export class LocationService {
  getCountries(successCallback) {
    get(`/locations/countries`, successCallback)
  }

  getProvinces(countryId, successCallback) {
    get(`/locations/provinces?countryId=${countryId}`, successCallback)
  }

  getTowns(provinceId, query, successCallback) {
    get(`/locations/towns?provinceId=${provinceId}&query=${query}`, successCallback)
  }
}
