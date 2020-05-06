import axios from 'axios';

export class ProductsService {

    searchProducts(searchOptions, successCallback, errorCallback) {
        let promise = axios.post(`/products/search`, searchOptions);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

    getCategories(successCallback) {
        let promise = axios.get(`/products/categories`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getSubCategories(categoryId, successCallback) {
        let promise = axios.get(`/products/sub-categories?categoryId=${categoryId}`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getBrands(successCallback) {
        let promise = axios.get(`/products/brands`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getSupplyTypes(successCallback) {
        let promise = axios.get(`/products/supply-types`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    getPercentTypes(successCallback) {
        let promise = axios.get(`/products/pricing/percent-types`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
    }

    searchSuppliers(query, successCallback) {
        let searchData = transformSuppliersSearchData(query);
        let promise = axios.post(`/persons/search`, searchData);

        if (successCallback) {
            promise.then(response => successCallback(response.data.data));
        }
    }

    updateProductsPricing(updateOptions, successCallback, errorCallback) {
        let updateRequest = transformUpdatePrices(updateOptions);
        let promise = axios.put(`/products/pricing`, updateRequest);

        if (successCallback) {
            promise.then(response => successCallback()); //This is a no-content response
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error.response.data));
        }
    }

}

function transformSuppliersSearchData(query) {
    return {
        firstResult: 0,
        maxResults: 15,
        searchFilter: {
            txt: query,
            activo: true,
            proveedor: true
        }
    }
}

function transformUpdatePrices(updateOptions) {
    return {
        searchFilter: updateOptions.searchFilter,
        costUpdatePercent: updateOptions.updateCost ? updateOptions.costPercent : null,
        percentsToDelete: updateOptions.updatePercents ? transformPercents(updateOptions.percentsToDelete) : null,
        percentsToAdd: updateOptions.updatePercents ? transformPercents(updateOptions.percentsToAdd) : null
    };
}

function transformPercents(percents) {
    return percents.map((percent) => {
        return {
            percentTypeId: percent.percentType.id,
            percentValue: percent.value
        }
    });
}
