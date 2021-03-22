import moment from "moment"

const MONTH = "month"
const DAY = "day"
const SERIALIZED_DATE_FORMAT = "YYYY-MM-DDTHH:mm:ss"

const formatDate = (stringDate) => {
  return new Date(stringDate).toLocaleString()
}

const getBeginOfMonth = () => {
  return moment().startOf(MONTH).toDate()
}

const getEndOfMonth = () => {
  return moment().endOf(MONTH).toDate()
}

const getBeginOfToday = () => {
  return moment().startOf(DAY).toDate()
}

const getEndOfToday = () => {
  return moment().endOf(DAY).toDate()
}

const serializeDate = (date) => {
  return moment(date).format(SERIALIZED_DATE_FORMAT)
}

export {
  formatDate,
  getBeginOfMonth,
  serializeDate,
  getBeginOfToday,
  getEndOfMonth,
  getEndOfToday,
}
