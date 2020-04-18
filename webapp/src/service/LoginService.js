import axios from 'axios';
import jwt from 'jwt-decode'

export const USER_TOKEN_STORE = 'userToken';

class LoginService {

    constructor() {
        if (!this.axiosInterceptorId && this.isUserLoggedIn()) {
            let userToken = sessionStorage.getItem(USER_TOKEN_STORE);

            this.userDetails = jwt(userToken).userDetails;

            this.setUpInterceptors(this.createTokenHeaderValue(userToken));
        }
    }

    performLogin(userCredentials) {
        return axios.post('authenticate', userCredentials);
    }

    initUserSession(token) {
        this.userDetails = jwt(token).userDetails;

        sessionStorage.setItem(USER_TOKEN_STORE, token);

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
        return this.userDetails;
    }

    hasUserRole(role) {
        let userDetails = this.userDetails;
        let hasRole = userDetails.userRoles.filter((r) => r === role);

        return hasRole.length !== 0;
    }

    performLogout() {
        axios.interceptors.request.eject(this.axiosInterceptorId);
        sessionStorage.removeItem(USER_TOKEN_STORE);
        delete this.userDetails;
    }
}

export default new LoginService();
