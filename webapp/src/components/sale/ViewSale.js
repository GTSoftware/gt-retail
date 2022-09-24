import React, { useEffect, useRef, useState } from "react"
import { Toast } from "primereact/toast"
import { SalesService } from "../../service/SalesService"
import _ from "lodash"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../utils/DateUtils"
import { Button } from "primereact/button"
import { InvoiceDialog } from "./InvoiceDialog"
import FileOutputsService from "../../service/FileOutputsService"
import { InvoicePrintSplitButton } from "../core/InvoicePrintSplitButton"
import {BudgetPrintSplitButton} from "../core/BudgetPrintSplitButton";

export const ViewSale = (props) => {
  const salesService = new SalesService()

  const [saleId] = useState(props.match.params.saleId)
  const [sale, setSale] = useState(null)
  const [loading, setLoading] = useState(true)
  const [showInvoiceDialog, setShowInvoiceDialog] = useState(false)

  const toast = useRef(null)

  useEffect(
    () => salesService.getSale(saleId, handleGetSale, handleGetSaleError),
    [saleId]
  )

  const handleGetSale = (saleInfo) => {
    setSale(saleInfo)
    setLoading(false)
  }

  const renderSaleInformation = () => {
    return (
      <>
        <div className="p-col-12 p-lg-4">
          <label>Cliente:</label>
          <label style={{ fontWeight: "bold" }}>{sale.customer}</label>
        </div>
        <div className="p-col-12 p-lg-4">
          <label>Fecha:</label>
          <label style={{ fontWeight: "bold" }}>{formatDate(sale.saleDate)}</label>
        </div>
        <div className="p-col-12 p-lg-4">
          <label>Vendedor:</label>
          <label style={{ fontWeight: "bold" }}>{sale.user}</label>
        </div>
        <div className="p-col-12 p-lg-4">
          <label>Sucursal:</label>
          <label style={{ fontWeight: "bold" }}>{sale.branch}</label>
        </div>
        <div className="p-col-12 p-lg-4">
          <label>Tipo:</label>
          <label style={{ fontWeight: "bold" }}>{sale.saleType}</label>
        </div>

        <div className="p-col-12 p-lg-4">
          <label>Condición:</label>
          <label style={{ fontWeight: "bold" }}>{sale.saleCondition}</label>
        </div>

        <div className="p-col-12 p-lg-4">
          <label>Factura:</label>
          <label style={{ fontWeight: "bold" }}>{sale.invoiceNumber}</label>
        </div>

        <div className="p-col-12">{renderSaleItems()}</div>

        <div className="p-col-12 p-lg-6">
          <label>Total:</label>
          <label style={{ fontWeight: "bold" }}>
            $ {sale.total.toLocaleString()}
          </label>
        </div>
        <div className="p-col-12 p-lg-6">
          <label>Saldo:</label>
          <label style={{ fontWeight: "bold" }}>
            $ {sale.remainingAmount.toLocaleString()}
          </label>
        </div>

        <div className="p-col-12 p-lg-4">
          <label>Observaciones:</label>
          <label style={{ fontWeight: "bold" }}>{sale.observations}</label>
        </div>
        <div className="p-col-12 p-lg-2">
          <label>Remitente:</label>
          <label style={{ fontWeight: "bold" }}>{sale.remitente}</label>
        </div>
        <div className="p-col-12 p-lg-2">
          <label>Remito:</label>
          <label style={{ fontWeight: "bold" }}>{sale.remito}</label>
        </div>
      </>
    )
  }

  const renderSaleItems = () => {
    return (
      <DataTable value={sale.details} rows={8} paginator={true}>
        <Column field="productCode" header="Código" />
        <Column field="description" header="Descricpción" />
        <Column field="saleUnit" header="Un. Venta" />
        <Column field="quantity" header="Cantidad" />
        <Column field="subTotal" header="SubTotal" />
      </DataTable>
    )
  }

  const renderActions = () => {
    let buttonToRender

    if (sale.invoiceNumber) {
      buttonToRender = (
        <InvoicePrintSplitButton saleId={sale.saleId} />
      )
    } else {
      buttonToRender = (
        <Button
          type="button"
          icon="fa fa-fw fa-print"
          label={"Facturar"}
          className="p-button-success"
          onClick={() => setShowInvoiceDialog(true)}
          tooltip={"Abre el cuadro de diálogo para facturar el comprobante actual"}
        />
      )
    }

    return buttonToRender
  }

  const renderInvoiceDialog = () => {
    return (
      <InvoiceDialog
        visible={showInvoiceDialog}
        currentSale={sale}
        successCallback={handleInvoice}
        onHide={() => setShowInvoiceDialog(false)}
      />
    )
  }

  const getPlaceholder = () => {
    return <span className="fa fa-spinner fa-spin fa-3x" />
  }

  const handleGetSaleError = (error) => {
    toast.current.show({
      severity: "error",
      summary: "No se pudo encontrar el comprobante",
      detail: _.get(error, "response.data.message", ""),
    })
  }

  const handleInvoice = (createdInvoice) => {
    toast.current.show({
      severity: "info",
      summary: `Factura ${createdInvoice.invoiceNumber} generada exitosamente`,
    })

    setLoading(true)

    salesService.getSale(
      saleId,
      (saleInfo) => {
        setSale(saleInfo)
        setLoading(false)
        setShowInvoiceDialog(false)
      },
      handleGetSaleError
    )
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Vista de comprobante: {saleId}</h1>
      <div className="p-grid p-fluid">
        {loading && getPlaceholder()}
        {!loading && renderSaleInformation()}

        {!loading && renderActions()}
        {!loading && renderInvoiceDialog()}
      </div>
    </div>
  )
}
