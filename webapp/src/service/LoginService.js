import axios from 'axios';
import jwt from 'jwt-decode'

export const USER_TOKEN_STORE = 'userToken';
export const USER_DETAILS_STORE = 'userDetails';

class LoginService {

    constructor() {
        if (!this.axiosInterceptorId && this.isUserLoggedIn()) {
            let userToken = sessionStorage.getItem(USER_TOKEN_STORE);

            this.setUpInterceptors(this.createTokenHeaderValue(userToken));
        }
    }

    performLogin(userCredentials) {
        return axios.post('authenticate', userCredentials);
    }

    initUserSession(token) {
        let userDetails = jwt(token).userDetails;

        sessionStorage.setItem(USER_TOKEN_STORE, token);
        sessionStorage.setItem(USER_DETAILS_STORE, JSON.stringify(userDetails));

        this.setUpInterceptors(this.createTokenHeaderValue(token));
    }

    setUpInterceptors(tokenHeaderValue) {
        if (this.axiosInterceptorId) {
            axios.interceptors.request.eject(this.axiosInterceptorId);
        }

        this.axiosInterceptorId = axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = tokenHeaderValue
                }

                return config;
            }
        )
    }

    createTokenHeaderValue(token) {
        return 'Bearer ' + token;
    }

    isUserLoggedIn() {
        let userToken = sessionStorage.getItem(USER_TOKEN_STORE);

        return userToken || false;
    }

    getUserDetails() {
        return JSON.parse(sessionStorage.getItem(USER_DETAILS_STORE));
    }

    performLogout() {
        axios.interceptors.request.eject(this.axiosInterceptorId);
        sessionStorage.removeItem(USER_TOKEN_STORE);
        sessionStorage.removeItem(USER_DETAILS_STORE);
    }
}

export default new LoginService()