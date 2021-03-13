import axios from "axios"

export class CustomersService {
  addNewCustomer(formData, successCallback, errorCallback) {
    let newCustomer = transformFormData(formData)
    let promise = axios.post(`/customers/customer`, newCustomer)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error))
    }
  }

  retrieveCustomer(searchData, successCallback, errorCallback) {
    const identificationTypeId = searchData.idTipoDocumento
    const identificationNumber = searchData.documento
    let promise = axios.get(
      `/customers/customer?identificationTypeId=${identificationTypeId}&identificationNumber=${identificationNumber}`
    )

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error))
    }
  }

  getLegalStatusTypes(successCallback) {
    let promise = axios.get(`/legal/status-types`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getGenders(legalStatusId, successCallback) {
    let promise = axios.get(`/legal/genders?legalStatusId=${legalStatusId}`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getIdentificationTypes(legalStatusId, successCallback) {
    let promise = axios.get(
      `/legal/identification-types?legalStatusId=${legalStatusId}`
    )

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getResponsabilidadesIva(successCallback) {
    let promise = axios.get(`/fiscal/responsabilidades-iva`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  //TODO these location-related methods should be moved to a proper service.
  getCountries(successCallback) {
    let promise = axios.get(`/locations/countries`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getProvinces(countryId, successCallback) {
    let promise = axios.get(`/locations/provinces?countryId=${countryId}`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getTowns(provinceId, query, successCallback) {
    let promise = axios.get(
      `/locations/towns?provinceId=${provinceId}&query=${query}`
    )

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }
}

function transformFormData(formData) {
  const legalPerson = formData.tipoPersoneria.id === 2

  return {
    email: toLowerCase(formData.email),
    razonSocial: legalPerson
      ? toUpperCase(formData.razonSocial)
      : toUpperCase(`${formData.apellidos}, ${formData.nombres}`),
    apellidos: legalPerson ? null : toUpperCase(formData.apellidos),
    nombres: legalPerson ? null : toUpperCase(formData.nombres),
    nombreFantasia: toUpperCase(formData.nombreFantasia),
    calle: toUpperCase(formData.calle),
    altura: toUpperCase(formData.altura),
    piso: toUpperCase(formData.piso),
    depto: toUpperCase(formData.depto),
    documento: formData.documento,
    provinciaId: formData.provincia.id,
    paisId: formData.pais.id,
    localidadId: formData.localidad.id,
    tipoPersoneriaId: formData.tipoPersoneria.id,
    tipoDocumentoId: formData.tipoDocumento.id,
    generoId: formData.genero.id,
    responsabilidadIvaId: formData.responsabilidadIva.id,
    telefonos: transformPhoneNumbers(formData.telefonos),
  }
}

function transformPhoneNumbers(telefonos) {
  if (telefonos) {
    return telefonos.map((telefono) => {
      return {
        numero: `${telefono.areaCode} ${telefono.number}`,
        referencia: toUpperCase(telefono.reference),
      }
    })
  }
}

function toUpperCase(value) {
  if (value) {
    return `${value}`.toUpperCase().trim()
  }
  return null
}

function toLowerCase(value) {
  if (value) {
    return `${value}`.toLowerCase().trim()
  }
  return null
}
