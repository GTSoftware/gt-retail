import { get, patch, post } from "../utils/HTTPService"
import { toUpperCaseTrim, toLowerCaseTrim } from "../utils/StringUtils"

export class CustomersService {
  addNewCustomer(formData, successCallback, errorCallback) {
    const newCustomer = transformFormData(formData)

    post(`/customers`, newCustomer, successCallback, errorCallback)
  }

  updateCustomer(formData, successCallback, errorCallback) {
    const customerUpdateRequest = transformFormData(formData)

    patch(
      `/customers/${formData.customerId}`,
      customerUpdateRequest,
      successCallback,
      errorCallback
    )
  }

  searchCustomers(searchData, successCallback) {
    post(`/persons/search`, searchData, successCallback)
  }

  getCustomer(customerId, successCallback, errorCallback) {
    get(`/customers/${customerId}`, successCallback, errorCallback)
  }

  retrieveCustomer(searchData, successCallback, errorCallback) {
    const identificationTypeId = searchData.idTipoDocumento
    const identificationNumber = searchData.documento
    get(
      `/customers/customer?identificationTypeId=${identificationTypeId}&identificationNumber=${identificationNumber}`,
      successCallback,
      errorCallback
    )
  }

  getLegalStatusTypes(successCallback) {
    get(`/legal/status-types`, successCallback)
  }

  getGenders(legalStatusId, successCallback) {
    get(`/legal/genders?legalStatusId=${legalStatusId}`, successCallback)
  }

  getIdentificationTypes(legalStatusId, successCallback) {
    get(
      `/legal/identification-types?legalStatusId=${legalStatusId}`,
      successCallback
    )
  }

  getResponsabilidadesIva(successCallback) {
    get(`/fiscal/responsabilidades-iva`, successCallback)
  }

  //TODO these location-related methods should be moved to a proper service.
  getCountries(successCallback) {
    get(`/locations/countries`, successCallback)
  }

  getProvinces(countryId, successCallback) {
    get(`/locations/provinces?countryId=${countryId}`, successCallback)
  }

  getTowns(provinceId, query, successCallback) {
    get(`/locations/towns?provinceId=${provinceId}&query=${query}`, successCallback)
  }
}

function transformFormData(formData) {
  const legalPerson = formData.tipoPersoneria.id === 2

  return {
    email: toLowerCaseTrim(formData.email),
    razonSocial: legalPerson
      ? toUpperCaseTrim(formData.razonSocial)
      : toUpperCaseTrim(`${formData.apellidos}, ${formData.nombres}`),
    apellidos: legalPerson ? null : toUpperCaseTrim(formData.apellidos),
    nombres: legalPerson ? null : toUpperCaseTrim(formData.nombres),
    nombreFantasia: toUpperCaseTrim(formData.nombreFantasia),
    calle: toUpperCaseTrim(formData.calle),
    altura: toUpperCaseTrim(formData.altura),
    piso: toUpperCaseTrim(formData.piso),
    depto: toUpperCaseTrim(formData.depto),
    documento: formData.documento,
    provinciaId: formData.provincia.id,
    paisId: formData.pais.id,
    localidadId: formData.localidad.id,
    tipoPersoneriaId: formData.tipoPersoneria.id,
    tipoDocumentoId: formData.tipoDocumento.id,
    generoId: formData.genero.id,
    responsabilidadIvaId: formData.responsabilidadIva.id,
    telefonos: transformPhoneNumbers(formData.telefonos),
    version: formData.version,
    activo: formData.activo,
  }
}

function transformPhoneNumbers(telefonos) {
  if (telefonos) {
    return telefonos.map((telefono) => {
      return {
        numero: `${telefono.areaCode} ${telefono.number}`,
        referencia: toUpperCaseTrim(telefono.reference),
        version: telefono.version,
        id: telefono.id,
      }
    })
  }
}
