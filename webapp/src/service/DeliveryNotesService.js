import { post, get } from "../utils/HTTPService"

export class DeliveryNotesService {
  getWarehouses(successCallback) {
    get(`/delivery-notes/warehouses`, successCallback)
  }

  getDeliveryTypes(successCallback) {
    get(`/delivery-notes/delivery-types`, successCallback)
  }

  searchPersons(query, successCallback) {
    let searchData = transformPersonSearchData(query)
    post(`/persons/search`, searchData, successCallback)
  }

  addProduct(searchCriteria, successCallback, errorCallback) {
    post(
      `/delivery-notes/add-product`,
      searchCriteria,
      successCallback,
      errorCallback
    )
  }

  saveDeliveryNote(deliveryNote, successCallback, errorCallback) {
    let addDeliveryNote = transformDeliveryNote(deliveryNote)
    post(
      `/delivery-notes/delivery-note`,
      addDeliveryNote,
      successCallback,
      errorCallback
    )
  }

  searchDeliveryNotes(searchOptions, successCallback, errorCallback) {
    post(`/delivery-notes/search`, searchOptions, successCallback, errorCallback)
  }

  getDeliveryNoteDetails(deliveryNoteId, successCallback) {
    get(`/delivery-notes/${deliveryNoteId}/details`, successCallback)
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
