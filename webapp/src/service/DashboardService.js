import { get } from "../utils/HTTPService"

export class DashboardService {
  getSalesQuantity(successCallback) {
    get(`/dashboard/sales-quantity`, successCallback)
  }

  getNewCustomersQuantity(successCallback) {
    get(`/dashboard/new-customers`, successCallback)
  }

  getYearSalesReport(successCallback) {
    get(`/dashboard/monthly-sales`, successCallback)
  }

  getStockBreakReport(successCallback) {
    get(`/dashboard/stock-break`, successCallback)
  }
}
