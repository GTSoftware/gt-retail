import axios from "axios"

export class ReportsService {
  getSalesByProductReport(searchOptions, successCallback) {
    let promise = axios.post(`/reports/sold-products`, searchOptions)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }
}
