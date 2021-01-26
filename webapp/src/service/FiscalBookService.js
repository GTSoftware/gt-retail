import axios from 'axios';
import FileSaver from "file-saver";

export class FiscalBookService {

    getFiscalPeriods(successCallback) {
        let promise = axios.post(`/fiscal/fiscal-periods`, getDefaultFiscalPeriodsSearch());

        if (successCallback) {
            promise.then(response => {
                successCallback(response.data.data);
            });
        }
    }

    getRegInfoFile(query, successCallback, errorCallback) {
        let searchData = transformFiscalBookSearch(query);
        let promise = axios.post(`/fiscal/digital-book`, searchData, {responseType: 'blob'})
            .then(response => {
                handleDownloadFile(response);
                if (successCallback) {
                    successCallback();
                }
            });
        if (errorCallback) {
            promise.catch(response => errorCallback(response.data));
        }

    }

    getFiscalBookSpreadsheetFile(query, successCallback, errorCallback) {
        let searchData = transformFiscalBookSearch(query);
        let promise = axios.post(`/fiscal/book`, searchData, {responseType: 'blob'})
            .then(response => {
                handleDownloadFile(response);
                if (successCallback) {
                    successCallback();
                }
            });

        if (errorCallback) {
            promise.catch(response => errorCallback(response.data));
        }
    }

}

function transformFiscalBookSearch(query) {
    return {
        fiscalPeriodId: query.fiscalPeriodId,
        kind: query.kind
    }
}

function getDefaultFiscalPeriodsSearch() {
    return {
        firstResult: 0,
        maxResults: 24,
        searchFilter: {}
    };
}

function handleDownloadFile(response) {
    let fileName = response.headers["content-disposition"].split("filename=")[1];

    FileSaver.saveAs(response.data, fileName);
}

