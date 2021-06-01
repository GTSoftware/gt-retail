import { post, postWithFileDownload } from "../utils/HTTPService"

export class FiscalBookService {
  getFiscalPeriods(successCallback) {
    post(`/fiscal/fiscal-periods`, getDefaultFiscalPeriodsSearch(), successCallback)
  }

  getRegInfoFile(query, successCallback, errorCallback) {
    const searchData = transformFiscalBookSearch(query)

    postWithFileDownload(
      `/fiscal/digital-book`,
      searchData,
      successCallback,
      errorCallback
    )
  }

  getFiscalBookSpreadsheetFile(query, successCallback, errorCallback) {
    const searchData = transformFiscalBookSearch(query)

    postWithFileDownload(`/fiscal/book`, searchData, successCallback, errorCallback)
  }
}

function transformFiscalBookSearch(query) {
  return {
    fiscalPeriodId: query.fiscalPeriodId,
    kind: query.kind,
  }
}

function getDefaultFiscalPeriodsSearch() {
  return {
    firstResult: 0,
    maxResults: 24,
    searchFilter: {},
  }
}
