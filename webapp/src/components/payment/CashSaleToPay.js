import React, {Component} from "react";
import PropTypes from 'prop-types';
import {InputText} from "primereact/inputtext";
import {ToggleButton} from "primereact/togglebutton";

export class CashSaleToPay extends Component {

    static propTypes = {
        saleId: PropTypes.number.isRequired,
        totalPayment: PropTypes.number.isRequired,
        minPayment: PropTypes.number.isRequired,
        maxPayment: PropTypes.number.isRequired,
        editableAmount: PropTypes.bool.isRequired,
        successCallback: PropTypes.func.isRequired
    }

    constructor(props, context) {
        super(props, context);

        this.state = {
            paymentAmount: props.totalPayment,
            paymentConfirmed: false
        }
    }

    render() {
        const {saleId, editableAmount} = this.props;
        const {paymentAmount, paymentConfirmed} = this.state;

        return (
            <div className="p-col-6">
                <label>Venta: {saleId}</label>
                <InputText value={paymentAmount}
                           required={true}
                           keyfilter="money"
                           readOnly={!editableAmount}
                           onChange={(e) => {
                               this.setState({paymentAmount: e.target.value})
                           }}
                />
                <ToggleButton onIcon="fa fa-fw fa-check"
                              tooltip="Confirmar Pago"
                              onLabel="Confirmado"
                              offLabel="No Confirmado"
                              checked={paymentConfirmed}
                              onChange={() => this.handleConfirmToggleChange()}/>
            </div>
        );
    }

    isPaymentAmountValid = () => {
        const {maxPayment, minPayment, editableAmount} = this.props;
        const {paymentAmount} = this.state;
        let isValid = true;

        if (editableAmount) {
            let numericPayment = Number(paymentAmount);
            if ((numericPayment > maxPayment) || (numericPayment < minPayment)) {
                isValid = false;
            }
        }

        return isValid;
    }

    handleConfirmToggleChange = () => {
        const {successCallback, saleId} = this.props;

        if (this.isPaymentAmountValid()) {
            successCallback(saleId, Number(this.state.paymentAmount));
            this.setState({paymentConfirmed: true});
        }
    }
}
