import { get, getV2, patchV2, post, postV2 } from "../utils/HTTPService"
import { serializeDate } from "../utils/DateUtils"
import { toLowerCaseTrim, toUpperCaseTrim } from "../utils/StringUtils"

export class SuppliersService {
  async searchSuppliers(query) {
    const searchData = transformSuppliersSearchData(query)

    return await postV2(`/persons/search`, searchData)
  }

  async searchSuppliersPaginated(searchOptions) {
    return await postV2(`/persons/search`, searchOptions)
  }

  async getSupplier(customerId) {
    return await getV2(`/suppliers/${customerId}`)
  }

  async addNewSupplier(formData) {
    const newCustomer = transformFormData(formData)

    return await postV2(`/suppliers`, newCustomer)
  }

  async updateSupplier(formData) {
    const customerUpdateRequest = transformFormData(formData)

    return await patchV2(`/suppliers/${formData.customerId}`, customerUpdateRequest)
  }

  async retrieveSupplier(searchData) {
    const identificationTypeId = searchData.idTipoDocumento
    const identificationNumber = searchData.documento
    return getV2(
      `/suppliers/supplier?identificationTypeId=${identificationTypeId}&identificationNumber=${identificationNumber}`
    )
  }

  //Supplier invoices

  searchInvoices(searchCriteria, successCallback) {
    const searchFilter = transformSuppliersInvoiceSearchData(searchCriteria)

    post(`/supplier-invoices/search`, searchFilter, successCallback)
  }

  storeInvoice(invoiceData, successCallback, errorCallback) {
    const createInvoiceRequest = transformInvoiceData(invoiceData)
    post(`/supplier-invoice/`, createInvoiceRequest, successCallback, errorCallback)
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

//Supplier invoice functions

function transformInvoiceData(invoiceData) {
  return {
    invoiceDate: serializeDate(invoiceData.invoiceDate),
    notes: invoiceData.notes,
    letter: invoiceData.letter,
    pointOfSale: invoiceData.pointOfSale,
    invoiceNumber: invoiceData.invoiceNumber,
    invoiceTypeId: invoiceData.invoiceType.id,
    supplierId: invoiceData.supplier.personId,
    fiscalPeriodId: invoiceData.fiscalPeriod.id,
    grossIncomePerceptionAmount: +invoiceData.grossIncomePerceptionAmount,
    taxPerceptionAmount: +invoiceData.taxPerceptionAmount,
    invoiceDetails: transformInvoiceDetails(invoiceData.invoiceDetails),
  }
}

function transformInvoiceDetails(invoiceDetails) {
  return invoiceDetails.map((line) => {
    return {
      taxRateId: line.taxRate.taxRateId,
      taxAmount: line.taxAmount,
      baseAmount: line.baseAmount,
      nonTaxableAmount: line.nonTaxableAmount,
    }
  })
}

function transformSuppliersSearchData(query) {
  return {
    firstResult: 0,
    maxResults: 15,
    searchFilter: {
      txt: query,
      activo: true,
      proveedor: true,
    },
  }
}

function transformSuppliersInvoiceSearchData(searchCriteria) {
  return {
    firstResult: searchCriteria.firstResult,
    maxResults: searchCriteria.maxResults,
    searchFilter: {
      fechaComprobanteDesde: serializeDate(searchCriteria.fromDate),
      fechaComprobanteHasta: serializeDate(searchCriteria.toDate),
      idProveedor: searchCriteria.selectedSupplier?.personId,
    },
  }
}
