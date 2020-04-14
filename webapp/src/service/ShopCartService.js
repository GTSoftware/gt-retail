import axios from 'axios';

class ShopCartService {

    addProduct(searchData) {
        return axios.post(`/shop-cart/add-product`, searchData);
    }

    getDefaultCustomer() {
        return axios.get(`/shop-cart/default-customer`);
    }

    searchCustomers(query) {
        return axios.get(`/shop-cart/customers-search?query=${query}`);
    }

    getSaleTypes() {
        return axios.get(`/shop-cart/sale-types`);
    }

    getSaleConditions() {
        return axios.get(`/shop-cart/sale-conditions`);
    }

    getPaymentMethods() {
        return axios.get(`/shop-cart/payment-methods`);
    }

    saveSale(shopCart) {
        return axios.post(`/shop-cart/sale`, shopCart);
    }

    getPaymentPlans(paymentMethod) {
        return axios.get(`/shop-cart/payment-plans?paymentPlanId=${paymentMethod.id}`);
    }
}

export default new ShopCartService()