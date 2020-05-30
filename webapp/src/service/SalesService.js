import axios from 'axios';

export class SalesService {

    searchSales(searchOptions, successCallback, errorCallback) {
        let promise = axios.post(`/sales/search`, searchOptions);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

    getSalesTotals(searchOptions, successCallback) {
        let promise = axios.post(`/sales/totals`, searchOptions);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getSale(saleId, successCallback, errorCallback) {
        let promise = axios.get(`/sale/${saleId}`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

    getPointsOfSale(successCallback, errorCallback) {
        let promise = axios.get(`/points-of-sale`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

    registerInvoice(invoiceToRegister, successCallback, errorCallback) {
        let promise = axios.post(`/invoice`, invoiceToRegister);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

}
