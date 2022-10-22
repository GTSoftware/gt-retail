import { post } from "../utils/HTTPService"
import jwt from "jwt-decode"

const USER_TOKEN_STORE = "userToken"

class LoginService {
  constructor() {
    if (this.isUserLoggedIn()) {
      let userToken = sessionStorage.getItem(USER_TOKEN_STORE)

      this.userDetails = jwt(userToken).userDetails
    }
  }

  performLogin(userCredentials, okCallback, errorCallback) {
    post(
      "/authenticate",
      userCredentials,
      (response) => {
        this.initUserSession(response.token)
        if (okCallback) {
          okCallback()
        }
      },
      errorCallback
    )
  }

  initUserSession(token) {
    this.userDetails = jwt(token).userDetails

    sessionStorage.setItem(USER_TOKEN_STORE, token)
  }

  isUserLoggedIn() {
    const userToken = sessionStorage.getItem(USER_TOKEN_STORE)

    return userToken || false
  }

  getUserDetails() {
    return this.userDetails
  }

  hasUserRole(role) {
    let userDetails = this.userDetails
    let hasRole = userDetails.userRoles.filter((r) => r === role)

    return hasRole.length !== 0
  }

  performLogout() {
    post(
      "/logoff",
      null,
      (response) => {
        //Ignore
      },
      (errorCallback) => {
        //Ignore
      }
    )
    sessionStorage.removeItem(USER_TOKEN_STORE)
    delete this.userDetails
  }
}

export default new LoginService()
