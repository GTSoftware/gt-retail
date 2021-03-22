import React, { Component } from "react"
import { Dialog } from "primereact/dialog"
import PropTypes from "prop-types"
import { LoadingButton } from "../core/LoadingButton"
import _ from "lodash"
import { Toast } from "primereact/toast"
import { PaymentPendingSalesService } from "../../service/PaymentPendingSalesService"
import { CashSaleToPay } from "./CashSaleToPay"

export class PayDialog extends Component {
  static propTypes = {
    payingCustomer: PropTypes.string.isRequired,
    salesToPay: PropTypes.array.isRequired,
    banks: PropTypes.array.isRequired,
    noExtraCostPaymentMethods: PropTypes.array.isRequired,
    successCallback: PropTypes.func.isRequired,
  }

  constructor(props, context) {
    super(props, context)

    this.salesService = new PaymentPendingSalesService()

    this.state = {
      salesToPay: props.salesToPay,
      payingCustomer: props.payingCustomer,
      banks: props.banks,
      noExtraCostPaymentMethods: props.noExtraCostPaymentMethods,
      loading: false,
    }
  }

  componentDidMount() {}

  render() {
    return (
      <Dialog {...this.getDialogProps()}>
        <Toast ref={(el) => (this.toast = el)} />
        {this.renderContent()}
      </Dialog>
    )
  }

  renderContent = () => {
    const { loading, payingCustomer } = this.state

    return (
      <div>
        {/*Aca tiene que venir una tabla y mostrar los pagos según el tipo de pago*/}
        <div className="p-grid p-fluid">
          <div className="p-col-12">
            <label htmlFor="customer">Cliente:</label>
            <label id="customer" style={{ fontWeight: "bold" }}>
              {payingCustomer}
            </label>
          </div>
          {this.renderSalesToPay()}

          <LoadingButton
            loading={loading}
            label="Cobrar"
            disabled={true}
            type="button"
            onClick={this.handleRegisterInvoice}
            icon="fa fa-fw fa-cash"
          />
        </div>
      </div>
    )
  }

  getDialogProps = () => {
    let props = this.props
    let defaultProps = {
      header: "Cobrar comprobantes",
      modal: true,
    }

    return { ...defaultProps, ...props }
  }

  handleSuccess = (createdInvoice) => {
    this.props.successCallback(createdInvoice)
  }

  handleError = (error) => {
    this.setState({ loading: false })

    this.toast.show({
      severity: "error",
      summary: "No se pudieron cobrar los comprobantes",
      detail: _.get(error, "message", ""),
    })
  }

  renderSalesToPay = () => {
    const salesToPay = this.state.salesToPay

    return salesToPay.map((saleToPay) => {
      return this.renderPaymentComponent(saleToPay)
    })
  }

  renderPaymentComponent = (saleToPay) => {
    let paymentComponent
    if (saleToPay.paymentMethodId === 1) {
      paymentComponent = (
        <CashSaleToPay
          saleId={saleToPay.saleId}
          totalPayment={saleToPay.totalPayment}
          editableAmount={saleToPay.editableAmount}
          minPayment={saleToPay.minPayment}
          maxPayment={saleToPay.maxPayment}
          successCallback={(saleId, amount) => {
            this.handleConfirmPayment(saleId, amount)
          }}
        />
      )
    }

    return paymentComponent
  }

  handleConfirmPayment = (saleId, amount) => {
    console.log("SaleId:" + saleId + " Amount: " + amount)
  }
}
