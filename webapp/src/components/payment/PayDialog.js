import React, {Component} from "react";
import {Dialog} from "primereact/dialog";
import PropTypes from 'prop-types';
import {LoadingButton} from "../core/LoadingButton";
import _ from "lodash";
import {Growl} from "primereact/growl";
import {PaymentPendingSalesService} from "../../service/PaymentPendingSalesService";


export class PayDialog extends Component {

    static propTypes = {
        payingCustomer: PropTypes.string.isRequired,
        salesToPay: PropTypes.object.isRequired,
        banks: PropTypes.array.isRequired,
        successCallback: PropTypes.func.isRequired
    }

    constructor(props, context) {
        super(props, context);

        this.salesService = new PaymentPendingSalesService();

        this.state = {
            salesToPay: this.props.salesToPay,
            payingCustomer: this.props.payingCustomer,
            banks: this.props.banks,
            loading: false

        }

    }

    componentDidMount() {

    }

    render() {
        return (
            <Dialog {...this.getDialogProps()} >
                <Growl ref={(el) => this.growl = el}/>
                {this.renderContent()}
            </Dialog>
        )
    }

    renderContent = () => {
        const {loading, payingCustomer} = this.state;

        return (
            <div>
                {/*Aca tiene que venir una tabla y mostrar los pagos según el tipo de pago*/}
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        <label htmlFor="customer">Cliente:</label>
                        <label id="customer" style={{fontWeight: "bold"}}>{payingCustomer}</label>
                    </div>


                    <LoadingButton loading={loading}
                                   label="Cobrar"
                                   disabled={true}
                                   type="button"
                                   onClick={this.handleRegisterInvoice}
                                   icon="fa fa-fw fa-cash"
                    />
                </div>
            </div>

        );
    }

    getDialogProps = () => {
        let props = this.props;
        let defaultProps = {
            header: "Cobrar comprobantes",
            modal: true
        }

        return {...defaultProps, ...props}
    }

    handleSuccess = (createdInvoice) => {
        this.props.successCallback(createdInvoice);
    }

    handleError = (error) => {
        this.setState({loading: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudieron cobrar los comprobantes',
            detail: _.get(error, 'message', '')
        });
    }
}
