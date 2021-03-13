import axios from "axios"

class UsersService {
  retrieveUsers() {
    return axios.get("users")
  }

  retrieveUser(userId) {
    return axios.get(`users/${userId}`)
  }

  resetUserPassword(user) {
    return axios.put(`users/${user.id}/reset-password`)
  }
}

export default new UsersService()
