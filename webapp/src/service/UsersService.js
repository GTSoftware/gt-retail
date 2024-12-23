import { get, put } from "../utils/HTTPService"

export class UsersService {
  retrieveUsers(cb) {
    get("/users", cb)
  }

  retrieveUser(userId, cb) {
    get(`/users/${userId}`, cb)
  }

  resetUserPassword(passwordData, cb) {
    put(`/users/${user.id}/reset-password`, passwordData, cb)
  }
}
