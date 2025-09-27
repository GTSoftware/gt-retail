import React, { useEffect, useRef, useState } from "react"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import { Toast } from "primereact/toast"
import { WarehousesService } from "../../service/WarehousesService"
import { AddEditWarehouseDialog } from "./AddEditWarehouseDialog"

const columns = [
  { field: "warehouseId", header: "Id" },
  { field: "warehouseName", header: "Nombre" },
  { field: "branchName", header: "Sucursal" },
]

export const WarehousesInventory = () => {
  const warehousesService = new WarehousesService()

  const [loading, setLoading] = useState(false)
  const [warehouses, setWarehouses] = useState([])
  const [selectedWarehouse, setSelectedWarehouse] = useState(null)
  const [showEditDialog, setShowEditDialog] = useState(false)
  const [showAddDialog, setShowAddDialog] = useState(false)
  const toast = useRef(null)

  useEffect(() => {
    loadWarehouses()
  }, [])

  const loadWarehouses = () => {
    setLoading(true)
    warehousesService.list(
      (items) => {
        setWarehouses(items)
        setLoading(false)
      },
      () => setLoading(false)
    )
  }

  const renderColumns = () => {
    let cols = columns.map((col) => (
      <Column key={col.field} field={col.field} header={col.header} />
    ))
    cols.push(<Column body={getActions} />)
    return cols
  }

  const getActions = (rowData) => {
    return (
      <div>
        <Button
          type="button"
          icon="fa fa-fw fa-edit"
          tooltip={"Editar depósito"}
          onClick={() => {
            setSelectedWarehouse(rowData)
            setShowEditDialog(true)
          }}
        />
        <Button
          type="button"
          icon="fa fa-fw fa-trash"
          className="p-button-danger"
          tooltip={"Eliminar depósito"}
          onClick={() => handleDelete(rowData)}
        />
      </div>
    )
  }

  const handleDelete = (rowData) => {
    if (window.confirm("¿Está seguro que desea eliminar el depósito?")) {
      warehousesService.delete(
        rowData.warehouseId,
        () => {
          toast.current.show({
            severity: "success",
            summary: "Depósito eliminado",
          })
          loadWarehouses()
        },
        () =>
          toast.current.show({
            severity: "error",
            summary: "Error eliminando depósito",
          })
      )
    }
  }

  const handleCreate = (data) => {
    warehousesService.create(
      data,
      () => {
        toast.current.show({ severity: "success", summary: "Depósito creado" })
        loadWarehouses()
      },
      () =>
        toast.current.show({
          severity: "error",
          summary: "Error creando depósito",
        })
    )
  }

  const handleUpdate = (data) => {
    warehousesService.update(
      selectedWarehouse.warehouseId,
      data,
      () => {
        toast.current.show({ severity: "success", summary: "Depósito actualizado" })
        setSelectedWarehouse(null)
        loadWarehouses()
      },
      () =>
        toast.current.show({
          severity: "error",
          summary: "Error actualizando depósito",
        })
    )
  }

  return (
    <div className="content-section implementation">
      <Toast ref={toast} />

      <div className="p-grid">
        <div className="p-col-6">
          <h3>Depósitos</h3>
        </div>
        <div className="p-col-6" style={{ textAlign: "right" }}>
          <Button
            label="Nuevo"
            icon="fa fa-fw fa-plus"
            onClick={() => setShowAddDialog(true)}
          />
        </div>
      </div>

      <DataTable value={warehouses} dataKey={"warehouseId"} loading={loading}>
        {renderColumns()}
      </DataTable>

      {showAddDialog && (
        <AddEditWarehouseDialog
          visible={showAddDialog}
          modal
          onHide={() => setShowAddDialog(false)}
          acceptCallback={handleCreate}
        />
      )}

      {showEditDialog && (
        <AddEditWarehouseDialog
          visible={showEditDialog}
          modal
          onHide={() => setShowEditDialog(false)}
          acceptCallback={handleUpdate}
          warehouse={selectedWarehouse}
        />
      )}
    </div>
  )
}
