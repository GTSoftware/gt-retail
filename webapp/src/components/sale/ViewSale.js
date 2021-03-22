import React, { Component } from "react"
import { Toast } from "primereact/toast"
import { SalesService } from "../../service/SalesService"
import PropTypes from "prop-types"
import _ from "lodash"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../utils/DateUtils"
import { Button } from "primereact/button"
import { InvoiceDialog } from "./InvoiceDialog"
import FileOutputsService from "../../service/FileOutputsService"

export class ViewSale extends Component {
  static propTypes = {
    match: PropTypes.shape({
      params: PropTypes.shape({
        saleId: PropTypes.string.isRequired,
      }),
    }),
  }

  constructor(props, context) {
    super(props, context)

    this.state = {
      saleId: props.match.params.saleId,
      sale: null,
      loading: true,
      showInvoiceDialog: false,
    }

    this.salesService = new SalesService()
  }

  componentDidMount() {
    const { sale, saleId } = this.state
    if (!sale) {
      this.salesService.getSale(
        saleId,
        (saleInfo) => this.setState({ sale: saleInfo, loading: false }),
        this.handleGetSaleError
      )
    }
  }

  render() {
    const { saleId, loading } = this.state

    return (
      <div className="card card-w-title">
        <Toast ref={(el) => (this.toast = el)} />
        <h1>Vista de comprobante: {saleId}</h1>
        <div className="p-grid p-fluid">
          {loading && this.getPlaceholder()}
          {!loading && this.renderSaleInformation()}

          {!loading && this.renderActions()}
          {!loading && this.renderInvoiceDialog()}
        </div>
      </div>
    )
  }

  renderSaleInformation = () => {
    const { sale } = this.state

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

        <div className="p-col-12">{this.renderSaleItems()}</div>

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

  renderSaleItems = () => {
    const { sale } = this.state

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

  renderActions = () => {
    const { sale } = this.state
    let buttonToRender

    if (sale.invoiceNumber) {
      buttonToRender = (
        <Button
          type="button"
          icon="fa fa-fw fa-print"
          label={"Imprimir factura"}
          tooltip={"Imprimir factura"}
          onClick={() => FileOutputsService.getInvoice(sale.saleId)}
        />
      )
    } else {
      buttonToRender = (
        <Button
          type="button"
          icon="fa fa-fw fa-print"
          label={"Facturar"}
          className="p-button-success"
          onClick={() => this.setState({ showInvoiceDialog: true })}
          tooltip={"Abre el cuadro de diálogo para facturar el comprobante actual"}
        />
      )
    }

    return buttonToRender
  }

  renderInvoiceDialog = () => {
    const { showInvoiceDialog, sale } = this.state

    return (
      <InvoiceDialog
        visible={showInvoiceDialog}
        sale={sale}
        successCallback={this.handleInvoice}
        onHide={() => this.setState({ showInvoiceDialog: false })}
      />
    )
  }

  getPlaceholder = () => {
    return <span className="fa fa-spinner fa-spin fa-3x" />
  }

  handleGetSaleError = (error) => {
    this.toast.show({
      severity: "error",
      summary: "No se pudo encontrar el comprobante",
      detail: _.get(error, "response.data.message", ""),
    })
  }

  handleInvoice = (createdInvoice) => {
    const { saleId } = this.state

    this.toast.show({
      severity: "info",
      summary: `Factura ${createdInvoice.invoiceNumber} generada exitosamente`,
    })

    this.setState({ loading: true })

    this.salesService.getSale(
      saleId,
      (saleInfo) =>
        this.setState({
          sale: saleInfo,
          loading: false,
          showInvoiceDialog: false,
        }),
      this.handleGetSaleError
    )
  }
}
