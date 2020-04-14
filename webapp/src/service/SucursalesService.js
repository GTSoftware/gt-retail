import axios from 'axios';

class SucursalesService {

    retrieveActiveSucursales() {
        let activeSucursalesFilter = {
            "activa": true,

        };

        return axios.post(`/sucursales/search-all`, activeSucursalesFilter);
    }

}

export default new SucursalesService()