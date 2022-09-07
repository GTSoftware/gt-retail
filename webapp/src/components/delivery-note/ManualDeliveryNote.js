import React, { useEffect, useRef, useState } from "react"
import { SelectButton } from "primereact/selectbutton"
import { Dropdown } from "primereact/dropdown"
import { AutoComplete } from "primereact/autocomplete"
import { DeliveryNotesService } from "../../service/DeliveryNotesService"
import { Button } from "primereact/button"
import { Panel } from "primereact/panel"
import { InputText } from "primereact/inputtext"
import { LoadingButton } from "../core/LoadingButton"
import { Checkbox } from "primereact/checkbox"
import { Toast } from "primereact/toast"
import { DataTable } from "primereact/datatable"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { Column } from "primereact/column"
import _ from "lodash"
import { InputTextarea } from "primereact/inputtextarea"
import FileOutputsService from "../../service/FileOutputsService"
import { SearchProductsDialog } from "../core/SearchProductsDialog"
import { v4 as uuid } from "uuid"

const DELIVERY_DIRECTION = {
  INTERNAL: "Interno",
  EXTERNAL: "Externo",
}

const DELIVERY_DIRECTIONS = [
  DELIVERY_DIRECTION.INTERNAL,
  DELIVERY_DIRECTION.EXTERNAL,
]

export const ManualDeliveryNote = () => {
  const deliveryNotesService = new DeliveryNotesService()

  const [originDirection, setOriginDirection] = useState(DELIVERY_DIRECTION.EXTERNAL)
  const [destinationDirection, setDestinationDirection] = useState(
    DELIVERY_DIRECTION.INTERNAL
  )
  const [deliveryItems, setDeliveryItems] = useState([])
  const [deliveryTypes, setDeliveryTypes] = useState([])
  const [warehouses, setWarehouses] = useState([])
  const [filteredPersons, setFilteredPersons] = useState([])
  const [origin, setOrigin] = useState(null)
  const [deliveryType, setDeliveryType] = useState(null)
  const [destination, setDestination] = useState(null)
  const [firstStepDone, setFirstStepDone] = useState(false)
  const [panelCollapsed, setPanelCollapsed] = useState(false)
  const [productToSearch, setProductToSearch] = useState({
    productId: "",
    productCode: "",
    supplierCode: "",
    quantity: 1,
    usePurchaseUnits: false,
  })
  const [loadingAddProduct, setLoadingAddProduct] = useState(false)
  const [observations, setObservations] = useState("")
  const [savingDeliveryNote, setSavingDeliveryNote] = useState(false)
  const [savedDeliveryNoteId, setSavedDeliveryNoteId] = useState(null)
  const [showSearchProductsDialog, setShowSearchProductsDialog] = useState(false)
  const [onlyForSupplier, setOnlyForSupplier] = useState(true)

  useEffect(() => {
    deliveryNotesService.getDeliveryTypes((deliveryTypes) =>
      setDeliveryTypes(deliveryTypes)
    )
    deliveryNotesService.getWarehouses((warehouses) => setWarehouses(warehouses))
  }, [])

  const toast = useRef(null)

  const getPanelHeader = () => {
    let header = "Origen y destino"

    if (firstStepDone) {
      header = `Origen: ${origin.displayName} Destino: ${destination.displayName} - ${deliveryType.nombreTipo}`
    }

    return header
  }

  const renderOriginSection = () => {
    return (
      <div className="p-col-6">
        <h3>Origen:</h3>
        <SelectButton
          disabled={firstStepDone}
          value={originDirection}
          options={DELIVERY_DIRECTIONS}
          onChange={(e) => handleDeliveryDirectionChange("origin", e.value)}
        />
        {renderOriginSelector()}
      </div>
    )
  }

  const renderOriginSelector = () => {
    let originSelector = (
      <Dropdown
        id="internalOrigin"
        optionLabel="displayName"
        placeholder="Seleccione un depósito"
        options={warehouses}
        value={origin}
        disabled={firstStepDone}
        onChange={(e) => setOrigin(e.value)}
      />
    )
    if (DELIVERY_DIRECTION.EXTERNAL === originDirection) {
      originSelector = (
        <AutoComplete
          minLength={2}
          placeholder="Comience a escribir para buscar una persona"
          delay={500}
          id="originPersonField"
          completeMethod={(event) => filterPersons(event.query)}
          suggestions={filteredPersons}
          field="displayName"
          required={true}
          disabled={firstStepDone}
          onChange={(e) => setOrigin(e.value)}
          value={origin || ""}
        />
      )
    }

    return originSelector
  }

  const renderDestinationSection = () => {
    return (
      <div className="p-col-6">
        <h3>Destino:</h3>
        <SelectButton
          disabled={firstStepDone}
          value={destinationDirection}
          options={DELIVERY_DIRECTIONS}
          onChange={(e) => handleDeliveryDirectionChange("destination", e.value)}
        />
        {renderDestinationSelector()}
      </div>
    )
  }

  const renderDestinationSelector = () => {
    let destinationSelector = (
      <Dropdown
        id="internalDestination"
        disabled={firstStepDone}
        optionLabel="displayName"
        placeholder="Seleccione un depósito"
        options={warehouses}
        value={destination}
        onChange={(e) => setDestination(e.value)}
      />
    )
    if (DELIVERY_DIRECTION.EXTERNAL === destinationDirection) {
      destinationSelector = (
        <AutoComplete
          minLength={2}
          placeholder="Comience a escribir para buscar una persona"
          delay={500}
          disabled={firstStepDone}
          id="destinationPersonField"
          completeMethod={(event) => filterPersons(event.query)}
          suggestions={filteredPersons}
          field="displayName"
          required={true}
          onChange={(e) => setDestination(e.value)}
          value={destination || ""}
        />
      )
    }

    return destinationSelector
  }

  const renderDeliveryTypeSection = () => {
    return (
      <div className="p-col-12">
        <h3>Tipo de movimiento:</h3>
        <Dropdown
          id="deliveryType"
          optionLabel="nombreTipo"
          placeholder="Seleccione el tipo de movimiento"
          options={deliveryTypes}
          value={deliveryType}
          disabled={firstStepDone}
          onChange={(e) => setDeliveryType(e.value)}
        />
      </div>
    )
  }

  const renderConfirmFirstStepButton = () => {
    let nextStepButton = null

    if (
      !firstStepDone &&
      origin &&
      destination &&
      deliveryType &&
      !(
        DELIVERY_DIRECTION.EXTERNAL === originDirection &&
        DELIVERY_DIRECTION.EXTERNAL === destinationDirection
      )
    ) {
      nextStepButton = (
        <Button
          label="Confirmar"
          className="p-button-success"
          onClick={() => {
            setFirstStepDone(true)
            setPanelCollapsed(true)
          }}
          icon="fa fa-fw fa-arrow-down"
        />
      )
    }

    return nextStepButton
  }

  const renderAddItemsSection = () => {
    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-1 p-lg-1">
          <InputText
            id="id"
            autoFocus
            onChange={(e) => {
              handleSearchProductPropertyChange("productId", e.target.value)
            }}
            value={productToSearch.productId}
            keyfilter="int"
            placeholder="Id"
            onKeyPress={handleEnterKeyPress}
          />
        </div>
        <div className="p-col-2 p-lg-2">
          <InputText
            id="supplierCode"
            onChange={(e) => {
              handleSearchProductPropertyChange("supplierCode", e.target.value)
            }}
            value={productToSearch.supplierCode}
            placeholder="Código de fábrica"
            onKeyPress={handleEnterKeyPress}
          />
        </div>
        <div className="p-col-2 p-lg-2">
          <InputText
            id="codigo"
            onChange={(e) => {
              handleSearchProductPropertyChange("productCode", e.target.value)
            }}
            value={productToSearch.productCode}
            placeholder="Código propio"
            onKeyPress={handleEnterKeyPress}
          />
        </div>
        <div className="p-col-2 p-lg-2">
          <div className="p-inputgroup">
            <span className="p-inputgroup-addon">
              <Checkbox
                id="usePurchaseUnits"
                onChange={(e) => {
                  handleSearchProductPropertyChange("usePurchaseUnits", e.checked)
                }}
                tooltip={"Usar unidades de compra"}
                checked={productToSearch.usePurchaseUnits}
              />
            </span>
            <InputText
              id="cantidad"
              keyfilter="num"
              onChange={(e) => {
                handleSearchProductPropertyChange("quantity", e.target.value)
              }}
              value={productToSearch.quantity}
              placeholder="Cantidad"
              onKeyPress={handleEnterKeyPress}
            />
          </div>
        </div>

        <div className="p-col-2 p-lg-2">
          <label htmlFor="limitToSupplier">Limitar al proveedor:</label>
          <Checkbox
            id="limitToSupplier"
            onChange={(e) => setOnlyForSupplier(e.checked)}
            checked={onlyForSupplier}
          />
        </div>

        <div className="p-col-2 p-lg-2">
          <LoadingButton
            type="button"
            icon="fa fa-fw fa-plus"
            className="p-button-success shop-cart--add-product-button"
            onClick={tryAddProduct}
            loading={loadingAddProduct}
            tooltip={"Agregar producto"}
          />

          <Button
            type="button"
            className="shop-cart--search-product-button"
            icon="fa fa-fw fa-search"
            tooltip={"Buscar productos"}
            onClick={() => setShowSearchProductsDialog(true)}
          />
        </div>
      </div>
    )
  }

  const renderDeliveryItemsSection = () => {
    const internalOrigin = DELIVERY_DIRECTION.INTERNAL === originDirection
    const internalDestination = DELIVERY_DIRECTION.INTERNAL === destinationDirection

    return (
      <DataTable {...getItemsTableProps()}>
        <Column
          key="productId"
          field="productId"
          header="Id"
          style={{ width: "5%" }}
        />
        <Column
          key="productCode"
          field="productCode"
          header="Código"
          style={{ width: "8%" }}
        />
        <Column
          key="supplierCode"
          field="supplierCode"
          header="Cod. Fábrica"
          style={{ width: "8%" }}
        />
        <Column
          key="description"
          field="description"
          header="Descripción"
          style={{ width: "20%" }}
        />
        <Column key="totalStock" field="totalStock" header="Stock total" />
        <Column key="quantity" field="quantity" header="Cantidad" />

        {internalOrigin && (
          <Column
            key="originWarehouseNewStock"
            field="originWarehouseNewStock"
            header="Remanente en origen"
          />
        )}

        {internalDestination && (
          <Column
            key="destinationWarehouseNewStock"
            field="destinationWarehouseNewStock"
            header="Stock en destino"
          />
        )}

        <Column key="purchaseUnits" field="purchaseUnits" header="Un. Venta" />
        <Column key="saleUnits" field="saleUnits" header="Un. Compra" />
        <Column
          key="actions"
          body={getTableActions}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  const renderFooterSection = () => {
    return (
      <div className="p-fluid p-grid">
        <div className="p-col-12">
          <label htmlFor="observaciones">Observaciones:</label>
          <InputTextarea
            value={observations}
            onChange={(e) => setObservations(e.target.value)}
          />
        </div>
        <div className="p-col-6">
          {!savedDeliveryNoteId && (
            <LoadingButton
              loading={savingDeliveryNote}
              className="p-button-success"
              icon="fa fa-fw fa-save"
              label="Guardar"
              disabled={deliveryItems.length === 0}
              onClick={saveDeliveryNote}
            />
          )}
          {savedDeliveryNoteId && (
            <Button
              label="Imprimir Remito"
              icon="fa fa-fw fa-print"
              onClick={() => {
                FileOutputsService.getDeliveryNote(savedDeliveryNoteId)
              }}
            />
          )}
        </div>
      </div>
    )
  }

  const renderSearchProductsDialog = () => {
    return (
      <SearchProductsDialog
        visible={showSearchProductsDialog}
        modal={true}
        acceptCallback={handleSelectedProduct}
        onHide={() => setShowSearchProductsDialog(false)}
      />
    )
  }

  const getItemsTableProps = () => {
    let header = <div className="p-clearfix">Productos</div>
    let footer = (
      <div className="p-clearfix">
        <label>Renglones: {deliveryItems.length}</label>
      </div>
    )

    return {
      ...DEFAULT_DATA_TABLE_PROPS,
      ...{
        value: deliveryItems,
        header: header,
        footer: footer,
        resizableColumns: true,
        emptyMessage: "Todavía no se han agregado productos al remito",
      },
    }
  }

  const getTableActions = (rowData) => {
    return (
      <Button
        type="button"
        icon="fa fa-fw fa-trash"
        className="p-button-danger"
        onClick={() => removeItem(rowData)}
        tooltip={"Quitar ítem"}
      />
    )
  }

  const handleDeliveryDirectionChange = (direction, value) => {
    if (value) {
      if (direction === "origin") {
        setOriginDirection(value)
        setOrigin(null)
      } else {
        setDestinationDirection(value)
        setDestination(null)
      }
    }
  }

  const filterPersons = (query) => {
    deliveryNotesService.searchPersons(query, (data) =>
      setFilteredPersons(data.data)
    )
  }

  const handleSearchProductPropertyChange = (property, value) => {
    let searchFields = { ...productToSearch }

    searchFields[property] = value

    setProductToSearch(searchFields)
  }

  const tryAddProduct = () => {
    let searchCriteria = { ...productToSearch }

    if (shouldSearch(searchCriteria)) {
      setLoadingAddProduct(true)

      if (DELIVERY_DIRECTION.INTERNAL === originDirection) {
        searchCriteria.originWarehouseId = origin.warehouseId
      }

      if (DELIVERY_DIRECTION.INTERNAL === destinationDirection) {
        searchCriteria.destinationWarehouseId = destination.warehouseId
      }

      if (onlyForSupplier && DELIVERY_DIRECTION.EXTERNAL === originDirection) {
        searchCriteria.supplierId = origin.personId
      }

      deliveryNotesService.addProduct(searchCriteria, handleAddItem, handleError)
    }
  }

  const shouldSearch = (searchCriteria) => {
    return (
      searchCriteria.supplierCode ||
      searchCriteria.productCode ||
      searchCriteria.productId
    )
  }

  const handleAddItem = (deliveryItem) => {
    deliveryItem.itemNumber = uuid()
    setLoadingAddProduct(false)

    const newDeliveryItems = [...deliveryItems]

    newDeliveryItems.splice(0, 0, deliveryItem)

    setDeliveryItems(newDeliveryItems)
    clearProductSearch()
  }

  const handleError = (error) => {
    setLoadingAddProduct(false)

    toast.current.show({
      severity: "error",
      summary: "El producto no existe o no corresponde con el proveedor",
      detail: error.message,
    })
  }

  const handleSaveError = (error) => {
    setSavingDeliveryNote(false)

    toast.current.show({
      severity: "error",
      summary: "No se pudo guardar el remito",
      detail: _.get(error, "response.data.message", ""),
    })
  }

  const clearProductSearch = () => {
    productToSearch.quantity = 1
    productToSearch.productId = ""
    productToSearch.productCode = ""
    productToSearch.supplierCode = ""

    setProductToSearch(productToSearch)
  }

  const removeItem = (rowData) => {
    _.remove(deliveryItems, (item) => {
      return item.itemNumber === rowData.itemNumber
    })

    setDeliveryItems(deliveryItems)
  }

  const handleEnterKeyPress = (event) => {
    if (event.key === "Enter" && !loadingAddProduct) {
      tryAddProduct()
    }
  }

  const saveDeliveryNote = () => {
    let deliveryNote = {
      originDirection: originDirection,
      destinationDirection: destinationDirection,
      origin: origin,
      destination: destination,
      deliveryNoteItems: deliveryItems,
      observations: observations,
      deliveryType: deliveryType,
    }

    setSavingDeliveryNote(true)

    deliveryNotesService.saveDeliveryNote(
      deliveryNote,
      handleSaveSuccess,
      handleSaveError
    )
  }

  const handleSaveSuccess = (savedDeliveryNoteId) => {
    setSavingDeliveryNote(false)
    setSavedDeliveryNoteId(savedDeliveryNoteId)

    toast.current.show({
      severity: "info",
      summary: "Remito generado exitosamente",
      detail: `Número: ${savedDeliveryNoteId}`,
    })
  }

  const handleSelectedProduct = (searchProduct) => {
    productToSearch.productId = searchProduct.productId

    setProductToSearch(productToSearch)
    tryAddProduct()
  }

  const renderSecondStep = () => {
    return (
      <>
        <div className="SeparatorFull" />
        {renderAddItemsSection()}
        {renderDeliveryItemsSection()}
        <div className="SeparatorFull" />
        {renderFooterSection()}
      </>
    )
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />

      {renderSearchProductsDialog()}
      <h1>Nuevo remito manual</h1>
      <Panel
        header={getPanelHeader()}
        toggleable={true}
        collapsed={panelCollapsed}
        onToggle={(e) => setPanelCollapsed(e.value)}
      >
        <div className="p-grid p-fluid">
          {renderOriginSection()}
          {renderDestinationSection()}
          {renderDeliveryTypeSection()}
        </div>
      </Panel>
      {renderConfirmFirstStepButton()}

      {firstStepDone && renderSecondStep()}
    </div>
  )
}
