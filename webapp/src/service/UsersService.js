import { get, put } from "../utils/HTTPService"

class UsersService {
  retrieveUsers(cb) {
    get("users", cb)
  }

  retrieveUser(userId, cb) {
    get(`users/${userId}`, cb)
  }

  resetUserPassword(user, cb) {
    put(`users/${user.id}/reset-password`, cb)
  }
}

export default new UsersService()
