import React, { useState, useEffect, useRef, useMemo } from "react"
import { DeliveryNotesService } from "../../service/DeliveryNotesService"
import { Toast } from "primereact/toast"
import { StockService } from "../../service/StockService"
import { Dropdown } from "primereact/dropdown"
import { Button } from "primereact/button"
import { LoadingButton } from "../core/LoadingButton"
import { Calendar } from "primereact/calendar"
import moment from "moment"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import FileOutputsService from "../../service/FileOutputsService"
import { AutocompleteProductFilter } from "../core/AutocompleteProductFilter"
import { formatDate, serializeDate } from "../../utils/DateUtils"

export const ProductHistory = () => {
  const [warehouses, setWarehouses] = useState([])
  const [warehouse, setWarehouse] = useState(null)
  const [selectedProduct, setSelectedProduct] = useState(null)
  const [productMovements, setProductMovements] = useState([])
  const [loading, setLoading] = useState(false)
  const [fromDate, setFromDate] = useState(moment().subtract(1, "months").toDate())

  const toast = useRef(null)

  const deliveryNotesService = useMemo(() => new DeliveryNotesService(), [])
  const stockService = useMemo(() => new StockService(), [])

  useEffect(() => {
    deliveryNotesService.getWarehouses((data) => {
      setWarehouses(data)
      setWarehouse(data[0])
    })
  }, [deliveryNotesService])

  const handleError = (error) => {
    setLoading(false)

    toast.current.show({
      severity: "error",
      summary: "No se pudo encontrar el producto",
      detail: error?.message || "Error desconocido",
    })
  }

  const filterMovements = () => {
    const filter = {
      fromDate: serializeDate(fromDate),
      productId: selectedProduct.productId,
      warehouseId: warehouse.warehouseId,
    }

    setLoading(true)
    stockService.getProductMovementsHistory(
      filter,
      (movements) => {
        setLoading(false)
        setProductMovements(movements)
      },
      handleError
    )
  }

  const canPerformSearch = () => {
    return fromDate && selectedProduct && warehouse
  }

  const getLinkActions = (rowData) => {
    return (
      <Button
        type="button"
        icon="fa fa-fw fa-print"
        label={rowData.deliveryNoteId}
        onClick={() => FileOutputsService.getDeliveryNote(rowData.deliveryNoteId)}
      />
    )
  }

  const getItemsTableProps = () => {
    const header = <div className="p-clearfix">Movimientos</div>
    const footer = (
      <div className="p-clearfix">
        <label>Movimientos: {productMovements.length}</label>
      </div>
    )

    return {
      ...DEFAULT_DATA_TABLE_PROPS,
      value: productMovements,
      loading: loading,
      header: header,
      footer: footer,
      resizableColumns: true,
      emptyMessage: "No hay movimientos para el producto solicitado",
    }
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />

      <h1>Historia de un producto</h1>

      <div className="p-grid p-fluid">
        <div className="p-col-8">
          <label>Producto:</label>
          <AutocompleteProductFilter
            selectedProduct={selectedProduct}
            onProductSelect={(product) => setSelectedProduct(product)}
          />
        </div>
        <div className="p-col-6">
          <label htmlFor="warehouse">Depósito:</label>
          <Dropdown
            id="warehouse"
            optionLabel="displayName"
            placeholder="Seleccione un depósito"
            options={warehouses}
            value={warehouse}
            onChange={(e) => setWarehouse(e.value)}
          />
        </div>

        <div className="p-col-6">
          <label htmlFor="fromDate">Fecha desde:</label>
          <Calendar
            id="fromDate"
            showTime={true}
            showIcon={true}
            hourFormat="24"
            dateFormat="dd/mm/yy"
            value={fromDate}
            onChange={(e) => setFromDate(e.value)}
          />
        </div>

        <LoadingButton
          type="button"
          icon="fa fa-fw fa-filter"
          label={"Buscar movimientos"}
          className="p-button-success"
          onClick={filterMovements}
          loading={loading}
          tooltip={"Buscar movimientos"}
          disabled={!canPerformSearch()}
        />

        <DataTable {...getItemsTableProps()}>
          <Column
            header={"Fecha"}
            body={(rowData) => {
              return formatDate(rowData.movementDate)
            }}
          />
          <Column header={"Código"} field={"productCode"} />
          <Column header={"Descripción"} field={"productDescription"} />
          <Column header={"Unidad"} field={"saleUnit"} />
          <Column header={"Cantidad"} field={"movementQuantity"} />
          <Column header={"Stock Parcial"} field={"partialStock"} />
          <Column header={"Origen"} field={"deliveryNoteOrigin"} />
          <Column header={"Destino"} field={"deliveryNoteDestination"} />
          <Column header={"Tipo"} field={"movementType"} />
          <Column header={"Remito"} field={"deliveryNoteId"} body={getLinkActions} />
        </DataTable>
      </div>
    </div>
  )
}
