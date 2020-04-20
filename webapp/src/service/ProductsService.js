import axios from 'axios';

export class ProductsService {

    searchProducts(searchOptions, successFunction, errorFunction) {
        let promise = axios.post(`/products/search`, searchOptions);

        if (successFunction) {
            promise.then(response => successFunction(response.data));
        }
        if (errorFunction) {
            promise.catch(error => errorFunction(error.response.data))
        }
    }

}
