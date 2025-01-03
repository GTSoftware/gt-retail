import _ from "lodash"
import axios from "axios"
import { v4 as uuid } from "uuid"
import log from "../utils/Logger"
import FileSaver from "file-saver"

const USER_TOKEN_STORE = "userToken"
const NO_OK_CALLBACK_MSG = "No Callback provided when calling service"
const NO_ERROR_CALLBACK_MSG = "No error Callback provided when calling service"
const BASE_URL = "/api"

const isUserTokenPresent = () => {
  const userToken = sessionStorage.getItem(USER_TOKEN_STORE)

  return userToken || false
}

const handleDownloadFile = (response) => {
  let fileName = response.headers["content-disposition"].split("filename=")[1]

  FileSaver.saveAs(response.data, fileName)
}

const getRequestHeaders = () => {
  let reqConfig = { headers: { "x-gt-request-id": uuid() } }

  if (isUserTokenPresent()) {
    reqConfig.headers["authorization"] =
      "Bearer " + sessionStorage.getItem(USER_TOKEN_STORE)
  }

  return reqConfig
}

function getErrorData(errorResp) {
  return _.get(errorResp, "response.data", {})
}

const getUrl = (url) => {
  return `${BASE_URL}${url}`
}

const post = (url, data, cb, errorCb) => {
  axios
    .post(getUrl(url), data, getRequestHeaders())
    .then((response) => {
      if (!cb) {
        log.error(NO_OK_CALLBACK_MSG, url)
      } else {
        cb(response.data)
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

const postV2 = async (url, data) => {
  try {
    const response = await axios.post(getUrl(url), data, getRequestHeaders())
    if (!response) {
      log.error("No response from server", url)
      return null
    } else {
      return response.data
    }
  } catch (error) {
    throw getErrorData(error)
  }
}

const postWithFileDownload = (url, data, cb, errorCb) => {
  const config = getRequestHeaders()
  config["responseType"] = "blob"

  axios
    .post(getUrl(url), data, config)
    .then((response) => {
      handleDownloadFile(response)
      if (cb) {
        cb()
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

const put = (url, data, cb, errorCb) => {
  axios
    .put(getUrl(url), data, getRequestHeaders())
    .then((response) => {
      if (!cb) {
        log.error(NO_OK_CALLBACK_MSG, url)
      } else {
        cb(response.data)
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

const putV2 = async (url, data) => {
  try {
    const response = await axios.put(getUrl(url), data, getRequestHeaders())
    if (!response) {
      log.error("No response from server", url)
      return null
    } else {
      return response.data
    }
  } catch (error) {
    throw getErrorData(error)
  }
}

const patch = (url, data, cb, errorCb) => {
  axios
    .patch(getUrl(url), data, getRequestHeaders())
    .then((response) => {
      if (!cb) {
        log.error(NO_OK_CALLBACK_MSG, url)
      } else {
        cb(response.data)
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

const patchV2 = async (url, data) => {
  try {
    const response = await axios.patch(getUrl(url), data, getRequestHeaders())
    if (!response) {
      log.error("No response from server", url)
      return null
    } else {
      return response.data
    }
  } catch (error) {
    throw getErrorData(error)
  }
}

const get = (url, cb, errorCb) => {
  axios
    .get(getUrl(url), getRequestHeaders())
    .then((response) => {
      if (!cb) {
        log.error(NO_OK_CALLBACK_MSG, url)
      } else {
        cb(response.data)
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

const getV2 = async (url) => {
  try {
    const response = await axios.get(getUrl(url), getRequestHeaders())
    if (!response) {
      log.error("No response from server", url)
      return null
    } else {
      return response.data
    }
  } catch (error) {
    throw getErrorData(error)
  }
}

const getWithFileDownload = (url, cb, errorCb) => {
  const config = getRequestHeaders()
  config["responseType"] = "blob"

  axios
    .get(getUrl(url), config)
    .then((response) => {
      handleDownloadFile(response)
      if (cb) {
        cb()
      }
    })
    .catch((errorResp) => {
      if (!errorCb) {
        log.error(NO_ERROR_CALLBACK_MSG, url)
      } else {
        const errorResponseData = getErrorData(errorResp)
        errorCb(errorResponseData)
      }
    })
}

export {
  get,
  getV2,
  post,
  postV2,
  put,
  putV2,
  patch,
  patchV2,
  postWithFileDownload,
  getWithFileDownload,
}
