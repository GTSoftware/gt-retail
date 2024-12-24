import { get, put } from "../utils/HTTPService"

export class UsersService {
  retrieveUsers(cb) {
    get("/users", cb)
  }

  retrieveUser(userId, cb) {
    get(`/users/${userId}`, cb)
  }

  resetUserPassword(userId, cb, ec) {
    put(`/users/${userId}/reset-password`, null, cb, ec)
  }

  changePassword(userId, newPassword, cb, ec) {
    put(`/users/${userId}/change-password`, { newPassword }, cb, ec)
  }
}
