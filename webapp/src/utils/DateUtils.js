import moment from "moment";

export const formatDate = function (stringDate) {
    return new Date(stringDate).toLocaleString();
}

export const getBeginOfMonth = function () {
    return moment().startOf('month').toDate();
}

export const getEndOfMonth = function () {
    return moment().endOf('month').toDate();
}

