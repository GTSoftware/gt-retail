import moment from "moment"

export const formatDate = function (stringDate) {
  return new Date(stringDate).toLocaleString()
}

export const getBeginOfMonth = function () {
  return moment().startOf("month").toDate()
}

export const getEndOfMonth = function () {
  return moment().endOf("month").toDate()
}

export const getBeginOfToday = function () {
  return moment().startOf("day").toDate()
}

export const getEndOfToday = function () {
  return moment().endOf("day").toDate()
}

export const serializeDate = function (date) {
  return moment(date).format("YYYY-MM-DDTHH:mm:ss")
}
