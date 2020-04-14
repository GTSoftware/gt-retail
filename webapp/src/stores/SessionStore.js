export class SessionStore {

    getStoreName;

    getInitialState;

    constructor() {
        if (!this.getData()) {
            this.clearStore();
        }
    }

    getData() {
        let stringStoreData = sessionStorage.getItem(this.getStoreName());

        return JSON.parse(stringStoreData);
    }

    setData(data) {
        let stringData = JSON.stringify(data);

        sessionStorage.setItem(this.getStoreName(), stringData);
    }

    clearStore() {
        this.setData(this.getInitialState());
    }
}
