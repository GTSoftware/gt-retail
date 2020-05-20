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

}
