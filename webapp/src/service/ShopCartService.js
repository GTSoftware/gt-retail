import axios from 'axios';
import _ from 'lodash';

class ShopCartService {

    addProduct(searchData) {
        return axios.post(`/shop-cart/add-product`, searchData);
    }

    getDefaultCustomer(successCallback, errorCallback) {
        let promise = axios.get(`/shop-cart/default-customer`);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error));
        }
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

    saveSale(shopCart, successCallback, errorCallback) {
        let saleRequest = transformToSaleRequest(shopCart);
        let promise = axios.post(`/shop-cart/sale`, saleRequest);

        if (successCallback) {
            promise.then(response => successCallback(response.data));
        }
        if (errorCallback) {
            promise.catch(error => errorCallback(error))
        }
    }

    getPaymentPlans(paymentMethod) {
        return axios.get(`/shop-cart/payment-plans?paymentPlanId=${paymentMethod.id}`);
    }
}

function transformToSaleRequest(shopCart) {
    return {
        customerId: shopCart.customerInfo.customerId,
        saleTypeId: shopCart.saleType.id,
        saleConditionId: shopCart.saleCondition.id,
        observaciones: shopCart.observaciones,
        remito: shopCart.remito,
        remitente: shopCart.remitente,
        products: shopCart.products,
        payments: transformPayments(shopCart.payments)
    };
}

function transformPayments(payments) {
    return payments.map(payment => {
        return {
            paymentMethodId: payment.idFormaPago.id,
            paymentAmount: payment.montoPago,
            paymentPlanId: _.get(payment, 'idPlan.id'),
            paymentPlanDetailId: _.get(payment, 'idDetallePlan.id')
        }
    });
}

export default new ShopCartService()