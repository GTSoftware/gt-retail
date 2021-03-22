const isBlank = (str) => {
  return str == null || str.trim().length === 0
}

const isNotBlank = (str) => {
  return !isBlank(str)
}

const isEmpty = (str) => {
  return str == null || str.length === 0
}

const isNotEmpty = (str) => {
  return !isEmpty(str)
}

const getLength = (str) => {
  if (isEmpty(str)) {
    return 0
  }
  return str.length
}

const toUpperCaseTrim = (str) => {
  if (str) {
    return str.trim().toUpperCase()
  }
  return null
}

const toLowerCaseTrim = (str) => {
  if (str) {
    return str.trim().toLowerCase()
  }
  return null
}

const isString = (str) => {
  return typeof str === "string"
}

export {
  isEmpty,
  toLowerCaseTrim,
  toUpperCaseTrim,
  isBlank,
  getLength,
  isNotBlank,
  isNotEmpty,
  isString,
}
