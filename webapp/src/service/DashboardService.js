import { get } from "../utils/HTTPService"

export class DashboardService {
  async getSalesQuantity(successCallback) {
    get(`/dashboard/sales-quantity`, successCallback)
  }

  async getNewCustomersQuantity(successCallback) {
    get(`/dashboard/new-customers`, successCallback)
  }

  async getYearSalesReport(successCallback) {
    get(`/dashboard/monthly-sales`, successCallback)
  }

  async getStockBreakReport(successCallback) {
    get(`/dashboard/stock-break`, successCallback)
  }
}
