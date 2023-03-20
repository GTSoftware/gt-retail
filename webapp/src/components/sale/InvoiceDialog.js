import React, { useEffect, useRef, useState } from "react"
import { Dialog } from "primereact/dialog"
import { SalesService } from "../../service/SalesService"
import { Dropdown } from "primereact/dropdown"
import { LoadingButton } from "../core/LoadingButton"
import _ from "lodash"
import { Toast } from "primereact/toast"
import { v4 as uuid } from "uuid"

export const InvoiceDialog = (props) => {
  const salesService = new SalesService()

  const [sale] = useState(props?.currentSale)
  const [loading, setLoading] = useState(false)
  const [pointsOfSale, setPointsOfSale] = useState([])
  const [selectedPointOfSale, setSelectedPointOfSale] = useState(null)
  const invoiceRequestId = uuid()

  useEffect(() => salesService.getPointsOfSale(handlePointsOfSale), [])
  const toast = useRef(null)

  const getDialogProps = () => {
    let defaultProps = {
      header: "Facturar comprobante",
      modal: true,
    }

    return { ...defaultProps, ...props }
  }

  const handlePointsOfSale = (pointsOfSale) => {
    const defaultPos = pointsOfSale.find((pos) => pos.byDefault === true)

    setPointsOfSale(pointsOfSale)
    setSelectedPointOfSale(defaultPos)
  }

  const handleRegisterInvoice = () => {
    setLoading(true)

    const invoiceToRegister = {
      saleId: sale.saleId,
      pointOfSale: selectedPointOfSale.posNumber,
      invoiceDate: null,
      invoiceNumber: null,
      invoiceRequestId: invoiceRequestId,
    }

    salesService.registerInvoice(invoiceToRegister, handleSuccess, handleError)
  }

  const handleSuccess = (createdInvoice) => {
    props.successCallback(createdInvoice)
  }

  const handleError = (error) => {
    setLoading(false)

    toast.current.show({
      severity: "error",
      summary: "No se pudo facturar el comprobante",
      detail: _.get(error, "message", ""),
    })
  }

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <div className="p-col-2">
            <label htmlFor="invoiceCharacter">Letra:</label>
            <label id="invoiceCharacter" style={{ fontWeight: "bold" }}>
              {sale.invoiceChar}
            </label>
          </div>
          <div className="p-col-10">
            <label htmlFor="pointOfSale">Punto de venta:</label>
            <Dropdown
              id="pointOfSale"
              value={selectedPointOfSale}
              options={pointsOfSale}
              optionLabel="displayName"
              onChange={(event) => setSelectedPointOfSale(event.value)}
            />
          </div>

          <LoadingButton
            loading={loading}
            label="Facturar"
            disabled={selectedPointOfSale === null}
            type="button"
            onClick={handleRegisterInvoice}
            icon="fa fa-fw fa-print"
          />
        </div>
      </div>
    )
  }

  return (
    <Dialog {...getDialogProps()}>
      <Toast ref={toast} />
      {renderContent()}
    </Dialog>
  )
}
