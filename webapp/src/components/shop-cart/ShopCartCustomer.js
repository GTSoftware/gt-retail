import React, {Component} from 'react';
import {Button} from "primereact/button";
import {AutoComplete} from "primereact/autocomplete";
import {InputText} from "primereact/inputtext";
import {ShopCartStore} from "../../stores/ShopCartStore";
import ShopCartService from "../../service/ShopCartService";
import _ from "lodash";
import {InputTextarea} from "primereact/inputtextarea";
import {AddNewCustomerDialog} from "./AddNewCustomerDialog";


export class ShopCartCustomer extends Component {

    constructor(props) {
        super(props);

        this.shopCartStore = new ShopCartStore();

        this.state = {
            customerInfo: null,
            filteredCustomers: [],
            showAddNewCustomerDialog: false
        };

        this.handleGoNext = this.handleGoNext.bind(this);
        this.handleGoBack = this.handleGoBack.bind(this);
        this.handleSetCustomer = this.handleSetCustomer.bind(this);
        this.filterCustomers = this.filterCustomers.bind(this);
    }


    componentDidMount() {
        const {customerInfo} = this.state;

        if (!customerInfo) {
            ShopCartService.getDefaultCustomer(this.handleSetCustomer);
        }
    }

    render() {
        const {customerInfo} = this.state;

        return (
            <div className="card card-w-title">
                {this.renderAddNewCustomerDialog()}
                <div className="p-card-body p-fluid ">
                    <div className="p-grid">
                        <div className="p-col">
                            <label htmlFor="razonSocialField">Razón social: </label>
                            <div className="p-inputgroup">
                                <AutoComplete minLength={2} placeholder="Comience a escribir para buscar un cliente"
                                              autoFocus={true}
                                              delay={500}
                                              id="razonSocialField" style={{width: "40%"}}
                                              completeMethod={(event) => this.filterCustomers(event.query)}
                                              suggestions={this.state.filteredCustomers}
                                              field="businessName"
                                              required={true}
                                              onChange={event => this.handleSetCustomer(event.value)}
                                              value={customerInfo || ''}

                                />
                                <Button icon="fa fa-fw fa-plus"
                                        tooltip="Cliente nuevo"
                                        className="p-button-success"
                                        onClick={() => this.setState({showAddNewCustomerDialog: true})}/>

                            </div>
                        </div>

                    </div>

                    <div className="SeparatorFull"/>

                    <div className="p-grid">
                        <div className="p-col-12 p-md-4">
                            <label htmlFor="documento">Documento: </label>
                            <InputText id="documento"
                                       readOnly={true}
                                       value={_.get(customerInfo, 'identification', '')}/>

                        </div>
                        <div className="p-col-12 p-md-4">
                            <label htmlFor="responsabilidadIVA">Categoría IVA: </label>
                            <InputText id="responsabilidadIVA"
                                       readOnly={true}
                                       value={_.get(customerInfo, 'responsabilidadIVA', '')}/>
                        </div>

                        <div className="p-col p-md-12">
                            <label htmlFor="direccion">Dirección: </label>
                            <InputText id="direccion"
                                       readOnly={true}
                                       value={_.get(customerInfo, 'address', '')}/>
                        </div>

                        <div className="p-col-12 p-md-4">
                            <label htmlFor="email">Email: </label>
                            <InputText id="email"
                                       readOnly={true}
                                       value={_.get(customerInfo, 'email', '')}/>
                        </div>
                        <div className="p-col-12 p-md-4">
                            <label htmlFor="telefono">Teléfonos: </label>
                            <InputTextarea id="telefono"
                                           readOnly={true}
                                           value={this.getFormattedTelefonos(customerInfo)}/>
                        </div>
                    </div>
                </div>

                <div className="p-card-footer">
                    <div className="p-col p-justify-end p-align-end p-grid">
                        <Button label="Anterior"
                                className="p-button-secondary shop-cart--footer-actions-button"
                                icon="fa fa-fw fa-arrow-left"
                                onClick={this.handleGoBack}/>
                        <Button label="Siguiente"
                                className="shop-cart--footer-actions-button"
                                disabled={_.get(this.state, 'customerInfo.customerId', null) === null}
                                icon="fa fa-fw fa-arrow-right"
                                iconPos="right"
                                onClick={this.handleGoNext}/>
                    </div>
                </div>
            </div>
        );
    }

    renderAddNewCustomerDialog = () => {
        const {showAddNewCustomerDialog} = this.state;

        return (
            showAddNewCustomerDialog &&
            <AddNewCustomerDialog visible={this.state.showAddNewCustomerDialog}
                                  modal={true}
                                  handleNewCustomer={this.handleSetCustomer}
                                  onHide={() => this.setState({showAddNewCustomerDialog: false})}/>
        )
    }

    handleSetCustomer(customer) {
        this.setState({customerInfo: customer});
    }

    filterCustomers(query) {
        ShopCartService.searchCustomers(query)
            .then(
                (response) => {
                    this.setState({filteredCustomers: response.data})
                }
            );
    }

    getFormattedTelefonos = (customerInfo) => {
        const telefonos = _.get(customerInfo, 'phones');
        let formattedTelefonos = '';

        if (telefonos) {
            formattedTelefonos = telefonos.map((telefono) => {
                return `${telefono.phoneNumber} (${telefono.reference})`
            }).join('\n');
        }

        return formattedTelefonos;
    }

    handleGoNext() {
        if (_.get(this.state.customerInfo, 'customerId', null) !== null) {
            this.shopCartStore.setCustomerInfo(this.state.customerInfo);
            this.props.goNextCallback();
        }
    }

    handleGoBack() {
        this.props.goBackCallback();
    }
}
