import axios from 'axios';

export class DashboardService {

    getSalesQuantity(successCallback) {
        let promise = axios.get(`/dashboard/sales-quantity`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getNewCustomersQuantity(successCallback) {
        let promise = axios.get(`/dashboard/new-customers`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getYearSalesReport(successCallback) {
        let promise = axios.get(`/dashboard/monthly-sales`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getStockBreakReport(successCallback) {
        let promise = axios.get(`/dashboard/stock-break`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

}
