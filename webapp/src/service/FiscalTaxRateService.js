import { get } from "../utils/HTTPService"

export class FiscalTaxRateService {
  getTaxRates(successCallback) {
    get(`/fiscal/tax-rates`, successCallback)
  }
}
