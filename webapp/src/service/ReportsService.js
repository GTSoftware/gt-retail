import { post } from "../utils/HTTPService"

export class ReportsService {
  getSalesByProductReport(searchOptions, successCallback) {
    post(`/reports/sold-products`, searchOptions, successCallback)
  }
}
