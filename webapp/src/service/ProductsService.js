import axios from 'axios';

export class ProductsService {

    searchProducts(searchOptions, successCallback, errorCallback) {
        let promise = axios.post(`/products/search`, searchOptions);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data))
        }
    }

}
