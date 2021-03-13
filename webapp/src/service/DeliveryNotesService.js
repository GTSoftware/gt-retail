import axios from "axios"

export class DeliveryNotesService {
  getWarehouses(successCallback) {
    let promise = axios.get(`/delivery-notes/warehouses`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  getDeliveryTypes(successCallback) {
    let promise = axios.get(`/delivery-notes/delivery-types`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }

  searchPersons(query, successCallback) {
    let searchData = transformPersonSearchData(query)
    let promise = axios.post(`/persons/search`, searchData)

    if (successCallback) {
      promise.then((response) => successCallback(response.data.data))
    }
  }

  addProduct(searchCriteria, successCallback, errorCallback) {
    let promise = axios.post(`/delivery-notes/add-product`, searchCriteria)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error))
    }
  }

  saveDeliveryNote(deliveryNote, successCallback, errorCallback) {
    let addDeliveryNote = transformDeliveryNote(deliveryNote)
    let promise = axios.post(`/delivery-notes/delivery-note`, addDeliveryNote)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error))
    }
  }

  searchDeliveryNotes(searchOptions, successCallback, errorCallback) {
    let promise = axios.post(`/delivery-notes/search`, searchOptions)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
    if (errorCallback) {
      promise.catch((error) => errorCallback(error))
    }
  }

  getDeliveryNoteDetails(deliveryNoteId, successCallback) {
    let promise = axios.get(`/delivery-notes/${deliveryNoteId}/details`)

    if (successCallback) {
      promise.then((response) => successCallback(response.data))
    }
  }
}

function transformPersonSearchData(query) {
  return {
    firstResult: 0,
    maxResults: 15,
    searchFilter: {
      txt: query,
      activo: true,
    },
  }
}

function transformDeliveryItems(deliveryNoteItems) {
  return deliveryNoteItems.map((item) => {
    return {
      productId: item.productId,
      quantity: item.quantity,
      itemNumber: item.itemNumber,
    }
  })
}

function transformDeliveryNote(deliveryNote) {
  return {
    deliveryTypeId: deliveryNote.deliveryType.id,
    internalOriginId:
      deliveryNote.originDirection === "Interno"
        ? deliveryNote.origin.warehouseId
        : null,
    externalOriginId:
      deliveryNote.originDirection === "Externo"
        ? deliveryNote.origin.personId
        : null,
    internalDestinationId:
      deliveryNote.destinationDirection === "Interno"
        ? deliveryNote.destination.warehouseId
        : null,
    externalDestinationId:
      deliveryNote.destinationDirection === "Externo"
        ? deliveryNote.destination.personId
        : null,
    observations: deliveryNote.observations,
    deliveryNoteItems: transformDeliveryItems(deliveryNote.deliveryNoteItems),
  }
}
