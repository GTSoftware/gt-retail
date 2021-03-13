import React, { Component } from "react"
import { Dialog } from "primereact/dialog"
import PropTypes from "prop-types"
import { SalesService } from "../../service/SalesService"
import { Dropdown } from "primereact/dropdown"
import { LoadingButton } from "../core/LoadingButton"
import _ from "lodash"
import { Growl } from "primereact/growl"

export class InvoiceDialog extends Component {
  static propTypes = {
    sale: PropTypes.object.isRequired,
    successCallback: PropTypes.func.isRequired,
  }

  constructor(props, context) {
    super(props, context)

    this.salesService = new SalesService()

    this.state = {
      sale: this.props.sale,
      loading: false,
      pointsOfSale: [],
      pointOfSale: null,
    }
  }

  componentDidMount() {
    const { pointsOfSale } = this.state

    if (pointsOfSale.length === 0) {
      this.salesService.getPointsOfSale((pos) => this.handlePointsOfSale(pos))
    }
  }

  render() {
    return (
      <Dialog {...this.getDialogProps()}>
        <Growl ref={(el) => (this.growl = el)} />
        {this.renderContent()}
      </Dialog>
    )
  }

  renderContent = () => {
    const { pointsOfSale, pointOfSale, loading, sale } = this.state

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
              value={pointOfSale}
              options={pointsOfSale}
              optionLabel="displayName"
              onChange={(event) => this.setState({ pointOfSale: event.value })}
            />
          </div>

          <LoadingButton
            loading={loading}
            label="Facturar"
            disabled={pointOfSale === null}
            type="button"
            onClick={this.handleRegisterInvoice}
            icon="fa fa-fw fa-print"
          />
        </div>
      </div>
    )
  }

  getDialogProps = () => {
    let props = this.props
    let defaultProps = {
      header: "Facturar comprobante",
      modal: true,
    }

    return { ...defaultProps, ...props }
  }

  handlePointsOfSale = (pointsOfSale) => {
    let defaultPos = pointsOfSale.find((pos) => pos.byDefault === true)

    this.setState({
      pointsOfSale: pointsOfSale,
      pointOfSale: defaultPos,
    })
  }

  handleRegisterInvoice = () => {
    const { sale, pointOfSale } = this.state
    let invoiceToRegister = {
      saleId: sale.saleId,
      pointOfSale: pointOfSale.posNumber,
      invoiceDate: null,
      invoiceNumber: null,
    }
    this.setState({ loading: true })

    this.salesService.registerInvoice(
      invoiceToRegister,
      this.handleSuccess,
      this.handleError
    )
  }

  handleSuccess = (createdInvoice) => {
    this.props.successCallback(createdInvoice)
  }

  handleError = (error) => {
    this.setState({ loading: false })

    this.growl.show({
      severity: "error",
      summary: "No se pudo facturar el comprobante",
      detail: _.get(error, "message", ""),
    })
  }
}
