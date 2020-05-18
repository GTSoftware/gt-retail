import React, {Component} from 'react';
import {ShopCartStore} from "../../stores/ShopCartStore";
import {Button} from "primereact/button";
import ShopCartService from "../../service/ShopCartService";
import {Dropdown} from "primereact/dropdown";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import _ from "lodash";
import {Panel} from "primereact/panel";
import {InputTextarea} from "primereact/inputtextarea";
import {InputText} from "primereact/inputtext";
import {Growl} from "primereact/growl";
import {LoadingButton} from "../core/LoadingButton";
import {Dialog} from "primereact/dialog";


const paymentColums = [
    {field: 'idFormaPago.nombreFormaPago', header: 'Forma de pago'},
    {field: 'montoPago', header: 'Importe'},
    {field: 'idPlan.nombre', header: 'Plan'},
    {field: 'idDetallePlan.detallePlan', header: 'Cuotas'}
];

export class ShopCartPayment extends Component {

    constructor(props) {
        super(props);

        this.shopCartStore = new ShopCartStore();

        this.state = {
            saleType: this.shopCartStore.getSaleType(),
            saleTypes: null,
            saleCondition: this.shopCartStore.getSaleCondition(),
            saleConditions: null,
            paymentMethods: null,
            payments: this.shopCartStore.getPayments(),
            products: this.shopCartStore.getProducts(),
            additionalInfoPanelCollapsed: true,
            observaciones: this.shopCartStore.getObservacaiones(),
            remitente: this.shopCartStore.getRemitente(),
            remito: this.shopCartStore.getRemito(),
            savingSale: false,
            showAddPaymentDialog: false,
            newPayment: {}
        };

        this.handleGoBack = this.handleGoBack.bind(this);
        this.handleSaleTypes = this.handleSaleTypes.bind(this);
        this.handleSaleConditions = this.handleSaleConditions.bind(this);
        this.handlePropertyChange = this.handlePropertyChange.bind(this);
        this.getCartTotal = this.getCartTotal.bind(this);
        this.handlePaymentMethods = this.handlePaymentMethods.bind(this);
        this.renderColumns = this.renderColumns.bind(this);
        this.getTableActions = this.getTableActions.bind(this);
        this.removePayment = this.removePayment.bind(this);
        this.getPaymentsTableProps = this.getPaymentsTableProps.bind(this);
        this.getTotalPayment = this.getTotalPayment.bind(this);
        this.getNextItemNumber = this.getNextItemNumber.bind(this);
        this.isAbleToSave = this.isAbleToSave.bind(this);
        this.renderAdditionalInfoPanel = this.renderAdditionalInfoPanel.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handleSaveSaleError = this.handleSaveSaleError.bind(this);
        this.renderAddPaymentDialog = this.renderAddPaymentDialog.bind(this);
        this.addPaymentPlanDetail = this.addPaymentPlanDetail.bind(this);
        this.sortPaymentPlanDetails = this.sortPaymentPlanDetails.bind(this);
        this.getRemainingAmount = this.getRemainingAmount.bind(this);
        this.handleAddNewPayment = this.handleAddNewPayment.bind(this);
        this.addSurchargeItem = this.addSurchargeItem.bind(this);
        this.handleSavedSale = this.handleSavedSale.bind(this);
    }

    componentDidMount() {
        const state = this.state;

        if (!state.saleType || !state.saleTypes) {
            ShopCartService.getSaleTypes((saleTypes) => this.handleSaleTypes(saleTypes));
        }
        if (!state.saleCondition || !state.saleConditions) {
            ShopCartService.getSaleConditions()
                .then(response => this.handleSaleConditions(response.data));
        }
        if (!state.payments || !state.paymentMethods) {
            ShopCartService.getPaymentMethods()
                .then(response => this.handlePaymentMethods(response.data));
        }
    }

    render() {
        const state = this.state;

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                {this.renderAddPaymentDialog()}
                <div className="p-card-body p-fluid ">
                    <div className="p-grid">

                        <div className="p-col-12 p-md-4">
                            <label htmlFor="total">Total: </label>
                            <label id="total" className="shop-cart--cart-total">
                                $ {this.getCartTotal()}
                            </label>
                        </div>

                        <div className="p-col-12 p-md-4">
                            <label htmlFor="tipoComprobante">Tipo de comprobante: </label>
                            <Dropdown id="tipoComprobante"
                                      optionLabel="nombreComprobante"
                                      options={state.saleTypes}
                                      value={state.saleType}
                                      onChange={(event) => {
                                          this.handlePropertyChange('saleType', event.value)
                                      }}/>

                        </div>

                        <div className="p-col-12 p-md-4">
                            <label htmlFor="condicionVenta">Condición de venta: </label>
                            <Dropdown id="condicionVenta"
                                      optionLabel="nombreCondicion"
                                      options={state.saleConditions}
                                      value={state.saleCondition}
                                      onChange={(event) => {
                                          this.handlePropertyChange('saleCondition', event.value)
                                      }}
                            />

                        </div>
                    </div>

                    <div className="SeparatorFull"/>

                    <div className="p-grid">
                        <div className="p-col-12 p-md-4">
                            <Button label="Agregar pago"
                                    disabled={this.getRemainingAmount() <= 0}
                                    icon="fa fa-fw fa-plus"
                                    onClick={() => this.setState({
                                        showAddPaymentDialog: true,
                                        newPayment: {montoPago: this.getRemainingAmount()}
                                    })}/>
                        </div>
                    </div>
                    <DataTable {...this.getPaymentsTableProps()}>
                        {this.renderColumns()}
                    </DataTable>

                    <div className="SeparatorFull"/>
                    {this.renderAdditionalInfoPanel()}
                </div>

                <div className="p-card-footer">
                    <div className="p-col p-justify-end p-align-end p-grid">
                        <Button label="Anterior"
                                className="shop-cart--footer-actions-button p-button-secondary"
                                icon="fa fa-fw fa-arrow-left"
                                onClick={this.handleGoBack}/>
                        <LoadingButton label="Guardar"
                                       loading={this.state.savingSale}
                                       className="p-button-success shop-cart--footer-actions-button"
                                       disabled={!this.isAbleToSave()}
                                       onClick={this.handleSave}
                                       icon="fa fa-fw fa-save"/>
                    </div>
                </div>
            </div>
        );
    }

    renderAdditionalInfoPanel() {
        return (
            <Panel header="Información adicional"
                   toggleable={true}
                   collapsed={this.state.additionalInfoPanelCollapsed}
                   onToggle={(e) => this.setState({additionalInfoPanelCollapsed: e.value})}>
                <div className="p-grid">
                    <label htmlFor="observaciones">Observaciones:</label>
                    <InputTextarea id="observaciones"
                                   value={this.state.observaciones}
                                   onChange={(e) => this.handlePropertyChange('observaciones', e.target.value)}
                                   maxLength={1024}/>

                    <label htmlFor="remitente">Remitente:</label>
                    <InputText id="remitente"
                               maxLength={100}
                               value={this.state.remitente}
                               onChange={(e) => this.handlePropertyChange('remitente', e.target.value)}/>

                    <label htmlFor="remito">Remito:</label>
                    <InputText id="remito"
                               maxLength={100}
                               value={this.state.remito}
                               onChange={(e) => this.handlePropertyChange('remito', e.target.value)}/>
                </div>
            </Panel>
        );
    }

    renderColumns() {
        let columns = paymentColums.map((col, i) => {
            return <Column key={col.field} field={col.field} header={col.header}/>;
        });
        columns = columns.concat(
            <Column key="actions"
                    body={this.getTableActions}
                    style={{textAlign: 'center', width: '6em'}}/>
        );

        return columns;
    }

    renderAddPaymentDialog() {
        return <Dialog modal={true}
                       visible={this.state.showAddPaymentDialog}
                       header={"Agregar pago"}
                       onHide={() => this.setState({showAddPaymentDialog: false})}>

            <div className="p-card-body p-fluid ">

                <div className="p-grid">
                    <div className="p-col p-md-10">
                        <label htmlFor="saldo">Saldo: </label>
                        <label id="saldo"
                               style={{fontWeight: "bold"}}>{this.getRemainingAmount()}</label>
                    </div>
                    <div className="p-col p-md-10">
                        <label htmlFor="monto">Monto: </label>
                        <InputText id="monto"
                                   keyfilter="money"
                                   value={this.state.newPayment.montoPago}
                                   onChange={(e) => this.handleNewPaymentPropertyChange('montoPago', e.target.value)}
                        />
                    </div>

                    <div className="p-col p-md-10">
                        <label htmlFor="paymentMethod">Forma de pago: </label>
                        <Dropdown id="paymentMethod"
                                  optionLabel="nombreFormaPago"
                                  value={_.get(this.state.newPayment, 'idFormaPago')}
                                  options={this.state.paymentMethods}
                                  onChange={(e) => this.handleNewPaymentPropertyChange('idFormaPago', e.value)}
                        />
                    </div>

                    {this.renderPaymentPlans()}
                </div>

                <div className="p-card-footer">
                    <div className="p-col p-justify-end p-align-end p-grid">
                        <Button label="Agregar"
                                icon="fa fa-fw fa-plus"
                            //disabled={this.state.newPayment.montoPago > this.getRemainingAmount()}
                                onClick={this.handleAddNewPayment}
                        />
                    </div>
                </div>
            </div>

        </Dialog>
    }

    renderPaymentPlans() {
        let componentToRender = null;

        if (_.get(this.state.newPayment, 'idFormaPago.requierePlan')) {
            componentToRender = (
                <>
                    <div className="p-col p-md-4">
                        <label htmlFor="planesPago">Plan de pago: </label>
                        <Dropdown id="planesPago"
                                  optionLabel="nombre"
                                  value={_.get(this.state.newPayment, 'idPlan')}
                                  options={_.get(this.state.newPayment, 'paymentPlans')}
                                  onChange={(e) => this.handleNewPaymentPropertyChange('idPlan', e.value)}
                        />
                    </div>
                    <div className="p-col p-md-6">
                        <label htmlFor="detallesPlan">Cuotas: </label>
                        <Dropdown id="detallesPlan"
                                  optionLabel="detallePlan"
                                  value={_.get(this.state.newPayment, 'idDetallePlan')}
                                  options={_.get(this.state.newPayment, 'idPlan.negocioPlanesPagoDetalles')}
                                  onChange={(e) => this.handleNewPaymentPropertyChange('idDetallePlan', e.value)}
                        />
                    </div>
                </>
            )
        }
        return componentToRender;
    }

    getTableActions(rowData, column) {
        return (
            <div className="p-grid p-fluid">
                <Button type="button" icon="fa fa-fw fa-trash"
                        className="p-button-danger"
                        onClick={() => (this.removePayment(rowData))}
                        tooltip={'Quitar ítem'}/>

            </div>
        );
    }

    getPaymentsTableProps() {
        let header = <div className="p-clearfix">Pagos</div>;
        let footer = <div className="p-clearfix">
            <label>Suma de pagos: ${this.getTotalPayment()}</label>
        </div>;

        let tableProps = {
            value: this.state.payments,
            header: header,
            footer: footer,
            resizableColumns: true,
            responsive: true
        };

        return tableProps;
    }

    addPaymentPlanDetail(negocioPlanesPagoDetalles, montoPago) {

        if (negocioPlanesPagoDetalles) {
            negocioPlanesPagoDetalles.forEach((d) => {
                d.detallePlan = '' + d.cuotas + ' x $' + ((d.coeficienteInteres * montoPago) / d.cuotas).toFixed(2) + ' = $' + (d.coeficienteInteres * montoPago).toFixed(2);
            })
        }
    }

    sortPaymentPlanDetails(negocioPlanesPagoDetalles) {
        if (negocioPlanesPagoDetalles) {
            negocioPlanesPagoDetalles.sort((a, b) => {
                return (a.cuotas - b.cuotas)
            });
        }
    }

    handleNewPaymentPropertyChange(propertyName, value) {
        let newPayment = this.state.newPayment;

        newPayment[propertyName] = value;

        if (propertyName === 'paymentPlans') {
            value.forEach((plan) => {
                this.addPaymentPlanDetail(plan.negocioPlanesPagoDetalles, newPayment.montoPago);
                this.sortPaymentPlanDetails(plan.negocioPlanesPagoDetalles);
            });
        }

        if (propertyName === 'montoPago') {

            newPayment.idDetallePlan = null;
            newPayment.idPlan = null;
            newPayment.idFormaPago = null;
            newPayment.paymentPlans = null;
            newPayment.montoPago = value;
        }

        this.setState({
            newPayment: newPayment
        });

        if (propertyName === 'idFormaPago' && value.requierePlan) {
            ShopCartService.getPaymentPlans(this.state.newPayment.idFormaPago)
                .then(response => this.handleNewPaymentPropertyChange('paymentPlans', response.data));
        }

    }

    handleSaleTypes(saleTypes) {
        let defaultSaleType = saleTypes.find((type) => type.porDefecto === true);

        if (this.state.saleType) {
            this.setState({
                saleTypes: saleTypes
            });
        } else {
            this.setState({
                saleTypes: saleTypes,
                saleType: defaultSaleType
            });
            this.shopCartStore.setSaleType(defaultSaleType);
        }
    }

    handleSaleConditions(saleConditions) {
        let defaultSaleCondition = saleConditions.find((condition) => condition.porDefecto === true);

        if (this.state.saleCondition) {
            this.setState({
                saleConditions: saleConditions
            });
        } else {
            this.setState({
                saleConditions: saleConditions,
                saleCondition: defaultSaleCondition
            });
            this.shopCartStore.setSaleCondition(defaultSaleCondition);
        }

    }

    handlePaymentMethods(paymentMethods) {
        let defaultPaymentMethod = paymentMethods.find((method) => method.porDefecto === true);
        const state = this.state;

        if (!state.payments || state.payments.length === 0) {
            let defaultPayment = {
                idFormaPago: defaultPaymentMethod,
                montoPagado: 0,
                itemNumber: state.itemNumber,
                montoPago: this.getCartTotal()
            };
            this.setState({
                payments: [defaultPayment],
                paymentMethods: paymentMethods,
                itemNumber: state.itemNumber + 1
            });
        } else {
            this.setState({paymentMethods: paymentMethods});
        }
    }

    handlePropertyChange(propertyName, value) {
        let state = this.state;
        state[propertyName] = value;

        this.setState({
            state
        });
    }

    handleGoBack() {
        const state = this.state;

        this.shopCartStore.setSaleType(state.saleType);
        this.shopCartStore.setPayments(state.payments);
        this.shopCartStore.setSaleCondition(state.saleCondition);
        this.shopCartStore.setObservaciones(state.observaciones);
        this.shopCartStore.setRemitente(state.remitente);
        this.shopCartStore.setRemito(state.remito);
        this.shopCartStore.setProducts(state.products);
        this.props.goBackCallback();
    }

    getCartTotal() {
        let total = 0;
        const products = this.state.products;

        products.map((product) => total = total + product.subTotal);

        return +(total).toFixed(2);
    }

    getTotalPayment() {
        let total = 0;
        const payments = this.state.payments;

        if (payments && payments.length > 0) {
            payments.map((payment) => total = total + parseFloat(payment.montoPago));
        }

        return +(total).toFixed(2);
    }

    getRemainingAmount() {
        return +(this.getCartTotal() - this.getTotalPayment()).toFixed(2);
    }

    removePayment(rowData) {
        let payments = this.state.payments;
        let products = this.state.products;

        if (rowData.productoRecargoItem) {
            _.remove(products, (item) => {
                return (item.itemNumber === rowData.productoRecargoItem)
            });
        }

        _.remove(payments, (item) => {
            return (item.itemNumber === rowData.itemNumber)
        });

        this.setState({
            payments: payments,
            products: products
        });
    }

    getNextItemNumber() {
        const payments = this.state.payments;

        if (!payments || payments.length === 0) {
            return 1;
        } else {
            return Math.max.apply(Math, payments.map((p) => {
                return p.itemNumber;
            })) + 1;
        }
    }

    isAbleToSave() {
        const state = this.state;
        const pagoTotal = _.get(state, 'saleCondition.pagoTotal');
        const remainingAmount = this.getRemainingAmount();

        return (pagoTotal && (this.getTotalPayment() === this.getCartTotal()))
            || (!pagoTotal && remainingAmount > 0);
    }

    handleAddNewPayment() {
        let payments = this.state.payments;
        let itemNumber = this.getNextItemNumber();
        let newPayment = this.state.newPayment;
        let surchargeItemNumber;

        newPayment.itemNumber = itemNumber;
        newPayment.montoPago = parseFloat(newPayment.montoPago).toFixed(2);
        delete newPayment.paymentPlans;
        delete newPayment.paymentMethods;

        if (_.get(newPayment, 'idDetallePlan.coeficienteInteres', 0) > 1) {
            let surchargeAmount = +(newPayment.montoPago * newPayment.idDetallePlan.coeficienteInteres - newPayment.montoPago).toFixed(2);
            newPayment.montoPago = +(newPayment.montoPago * newPayment.idDetallePlan.coeficienteInteres).toFixed(2);

            surchargeItemNumber = this.addSurchargeItem(surchargeAmount,
                newPayment.idPlan.nombre,
                newPayment.idDetallePlan.cuotas);

            newPayment.productoRecargoItem = surchargeItemNumber;
        }

        payments = payments.concat(this.state.newPayment);

        this.setState({
            payments: payments,
            itemNumber: itemNumber + 1,
            newPayment: {},
            showAddPaymentDialog: false
        });
    }

    addSurchargeItem(surchargeAmount, nombrePlan, cuotas) {
        let products = this.state.products;
        let itemNumber = this.getProductsNextItemNumber();
        let surchargeItem = {
            itemNumber: itemNumber,
            cantidadEditable: false,
            deletable: false,
            descripcion: `COSTO FINANCIERO POR ${nombrePlan} EN: ${cuotas} CUOTAS`,
            codigoPropio: "X",
            precioVentaUnitario: surchargeAmount,
            cantidad: 1,
            unidadVenta: "UN.",
            subTotal: surchargeAmount,
        };

        products.splice(0, 0, surchargeItem);

        this.setState({products: products});

        return itemNumber;
    }

    getProductsNextItemNumber() {
        const products = this.state.products;

        if (!products || products.length === 0) {
            return 1;
        } else {
            return Math.max.apply(Math, products.map((p) => {
                return p.itemNumber;
            })) + 1;
        }
    }

    handleSave() {
        const state = this.state;

        this.shopCartStore.setSaleType(state.saleType);
        this.shopCartStore.setPayments(state.payments);
        this.shopCartStore.setSaleCondition(state.saleCondition);
        this.shopCartStore.setObservaciones(state.observaciones);
        this.shopCartStore.setRemitente(state.remitente);
        this.shopCartStore.setRemito(state.remito);
        this.shopCartStore.setProducts(state.products);
        this.setState({savingSale: true});

        ShopCartService.saveSale(this.shopCartStore.getData(), this.handleSavedSale, this.handleSaveSaleError);
    }

    handleSavedSale(savedSale) {
        this.shopCartStore.setData(savedSale);
        this.props.goNextCallback();
    }

    handleSaveSaleError(error) {
        this.setState({savingSale: false});
        this.growl.show({
            severity: 'error',
            summary: 'No se pudo guardar la venta',
            detail: error.response.data.message
        });
    }
}
