import React, { useState, useEffect, useRef } from "react"
import { CashBoxesService } from "../../service/CashBoxesService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import { Toast } from "primereact/toast"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { formatDate } from "../../utils/DateUtils"

export const MyCashBox = () => {
  const [cashBox, setCashBox] = useState(null)
  const [movements, setMovements] = useState([])
  const [loading, setLoading] = useState(false)
  const [totalRecords, setTotalRecords] = useState(0)
  const rows= 100
  const [first, setFirst] = useState(0)

  const toast = useRef(null)
  const cashBoxesService = new CashBoxesService()

  const loadMovements = (firstResult, currentBox = cashBox) => {
    const box = currentBox || cashBox
    if (!box) return

    setLoading(true)
    const searchOptions = {
      firstResult: firstResult,
      maxResults: rows,
      searchFilter: {
        idCaja: box.cashBoxId,
        sortFields: [{ fieldName: "fechaMovimiento", ascending: false }],
      },
    }

    cashBoxesService.searchMovements(
      searchOptions,
      (response) => {
        setMovements(response.data)
        setTotalRecords(response.totalResults)
        setFirst(firstResult)
        setLoading(false)
      },
      (error) => {
        setLoading(false)
        toast.current.show({
          severity: "error",
          summary: "Error",
          detail: error.message || "Error al cargar movimientos",
        })
      }
    )
  }

  const loadCurrentBox = () => {
    setLoading(true)
    cashBoxesService.getCurrentUserCashBox(
      (box) => {
        setCashBox(box)
        setLoading(false)
        loadMovements(0, box)
      },
      (error) => {
        setCashBox(null)
        setMovements([])
        setLoading(false)
        if (error.errorCode !== "404009") {
          toast.current.show({
            severity: "error",
            summary: "Error",
            detail: error.message || "Error al cargar la caja",
          })
        }
      }
    )
  }

  useEffect(() => {
    loadCurrentBox()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const onPage = (event) => {
    loadMovements(event.first)
  }

  const openBox = () => {
    setLoading(true)
    cashBoxesService.openBox(
      (box) => {
        toast.current.show({
          severity: "success",
          summary: "Éxito",
          detail: "Caja abierta correctamente",
        })
        setCashBox(box)
        setLoading(false)
        loadMovements(0, box)
      },
      (error) => {
        setLoading(false)
        toast.current.show({
          severity: "error",
          summary: "Error",
          detail: error.message || "Error al abrir la caja",
        })
      }
    )
  }

  return (
    <div className="card card-w-title dashboard">
      <Toast ref={toast} />
      <h1>Mi Caja {cashBox?.cashBoxId}</h1>

      {!cashBox && !loading && (
        <div className="p-grid p-fluid">
          <div className="p-col-12" style={{ textAlign: "center" }}>
            <p>No tienes una caja abierta actualmente.</p>
            <Button
              label="Abrir nueva caja"
              icon="fa fa-fw fa-plus"
              onClick={openBox}
            />
          </div>
        </div>
      )}

      {cashBox && (
        <div className="p-grid p-fluid">
          <div className="p-col-12 p-md-4">
            <div className="card summary">
              <span className="title">Saldo Inicial</span>

              <span className="count revenue">
                $ {cashBox.initialBalance.toLocaleString()}
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-4">
            <div className="card summary">
              <span className="title">Saldo Actual</span>
              <span className="count revenue">
                $ {cashBox.currentBalance.toLocaleString()}
              </span>
            </div>
          </div>
          <div className="p-col-12 p-md-4">
            <div className="card summary">
              <span className="title">Fecha apertura: </span>
              <span className="initials">
                {formatDate(cashBox.openDate)}
              </span>
            </div>
          </div>

          <div className="p-col-12">
            <DataTable
              {...DEFAULT_DATA_TABLE_PROPS}
              value={movements}
              lazy={true}
              first={first}
              rows={rows}
              totalRecords={totalRecords}
              onPage={onPage}
              loading={loading}
              header="Movimientos de caja"
            >
              <Column field="movementId" header="ID" sortable />
              <Column
                field="movementDate"
                header="Fecha"
                body={(rowData) => formatDate(rowData.movementDate)}
                sortable
              />
              <Column field="description" header="Descripción" />
              <Column
                field="amount"
                header="Monto"
                body={(rowData) => `$ ${rowData.amount.toLocaleString()}`}
                style={{ textAlign: "right" }}
              />
            </DataTable>
          </div>
        </div>
      )}
    </div>
  )
}
