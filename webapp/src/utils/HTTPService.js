import _ from "lodash"
import axios from "axios"
import { v4 as uuid } from "uuid"
import log from "../utils/Logger"
import FileSaver from "file-saver"

const USER_TOKEN_STORE = "userToken"
const NO_OK_CALLBACK_MSG = "No Callback provided when calling service"
const NO_ERROR_CALLBACK_MSG = "No error Callback provided when calling service"

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

const post = (url, data, cb, errorCb) => {
  axios
    .post(url, data, getRequestHeaders())
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

const postWithFileDownload = (url, data, cb, errorCb) => {
  const config = getRequestHeaders()
  config["responseType"] = "blob"

  axios
    .post(url, data, config)
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
    .put(url, data, getRequestHeaders())
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

const patch = (url, data, cb, errorCb) => {
  axios
    .patch(url, data, getRequestHeaders())
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

const get = (url, cb, errorCb) => {
  axios
    .get(url, getRequestHeaders())
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

const getWithFileDownload = (url, cb, errorCb) => {
  const config = getRequestHeaders()
  config["responseType"] = "blob"

  axios
    .get(url, config)
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

export { get, post, put, patch, postWithFileDownload, getWithFileDownload }
