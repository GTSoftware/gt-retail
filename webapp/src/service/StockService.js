import axios from 'axios';

export class StockService {

    getProductMovementsHistory(movementsFilter, successCallback, errorCallback) {
        let promise = axios.post(`/stock/product-movements`, movementsFilter);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }

        if (errorCallback) {
            promise.catch(error => errorCallback(error));
        }
    }

}
