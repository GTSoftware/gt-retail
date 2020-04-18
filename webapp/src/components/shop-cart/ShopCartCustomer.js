import React, {Component} from 'react';
import {Button} from "primereact/button";
import {AutoComplete} from "primereact/autocomplete";
import {InputText} from "primereact/inputtext";
import {ShopCartStore} from "../../stores/ShopCartStore";
import ShopCartService from "../../service/ShopCartService";
import _ from "lodash";
import {InputTextarea} from "primereact/inputtextarea";


export class ShopCartCustomer extends Component {

    constructor(props) {
        super(props);

        this.shopCartStore = new ShopCartStore();

        this.state = {
            customerInfo: this.shopCartStore.getCustomerInfo(),
            filteredCustomers: []
        };

        this.handleGoNext = this.handleGoNext.bind(this);
        this.handleGoBack = this.handleGoBack.bind(this);
        this.handleSetCustomer = this.handleSetCustomer.bind(this);
        this.getFormattedAddress = this.getFormattedAddress.bind(this);
        this.getFormattedDocument = this.getFormattedDocument.bind(this);
        this.filterCustomers = this.filterCustomers.bind(this);
        this.getFormattedTelefonos = this.getFormattedTelefonos.bind(this);
    }


    componentDidMount() {
        if (!this.shopCartStore.getCustomerInfo()) {
            ShopCartService.getDefaultCustomer()
                .then(response => this.handleSetCustomer(response.data));
        }
    }

    render() {
        let customerInfo = this.state.customerInfo;

        return (
            <div className="card card-w-title">
                <div className="p-card-body p-fluid ">
                    <div className="p-grid">
                        <div className="p-col">
                            <label htmlFor="razonSocialField">Razón social: </label>
                            <AutoComplete minLength={2} placeholder="Comience a escribir para buscar un cliente"
                                          autoFocus={true}
                                          delay={500}
                                          id="razonSocialField" style={{width: "40%"}}
                                          completeMethod={(event) => this.filterCustomers(event.query)}
                                          suggestions={this.state.filteredCustomers}
                                          field="razonSocial"
                                          required={true}
                                          onChange={event => this.handleSetCustomer(event.value)}
                                          value={this.state.customerInfo}


                            />
                            <Button icon="fa fa-fw fa-plus"
                                    tooltip="Cliente nuevo"
                                    className="p-button-success"/>
                        </div>

                    </div>

                    <div className="SeparatorFull"/>

                    <div className="p-grid">
                        <div className="p-col-12 p-md-4">
                            <label htmlFor="documento">Documento: </label>
                            <InputText id="documento"
                                       readOnly={true}
                                       value={this.getFormattedDocument(customerInfo)}/>

                        </div>
                        <div className="p-col-12 p-md-4">
                            <label htmlFor="responsabilidadIVA">Categoría IVA: </label>
                            <InputText id="responsabilidadIVA"
                                       readOnly={true}
                                       value={_.get(customerInfo, 'idResponsabilidadIva.nombreResponsabildiad', '')}/>
                        </div>

                        <div className="p-col p-md-12">
                            <label htmlFor="direccion">Dirección: </label>
                            <InputText id="direccion"
                                       readOnly={true}
                                       value={this.getFormattedAddress(customerInfo)}/>
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
                                disabled={_.get(this.state, 'customerInfo.id', null) === null}
                                icon="fa fa-fw fa-arrow-right"
                                iconPos="right"
                                onClick={this.handleGoNext}/>
                    </div>
                </div>
            </div>
        );
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

    getFormattedAddress(customerInfo) {
        const pais = _.get(customerInfo, 'idPais.nombrePais', '');
        const provincia = _.get(customerInfo, 'idProvincia.nombreProvincia', '');
        const localidad = _.get(customerInfo, 'idLocalidad.nombreLocalidad', '');
        const postalCode = _.get(customerInfo, 'idLocalidad.codigoPostal', '');
        const calle = _.get(customerInfo, 'calle', '');
        const altura = _.get(customerInfo, 'altura', '');
        const piso = _.get(customerInfo, 'piso', '');
        const depto = _.get(customerInfo, 'depto', '');

        return `${calle} ${altura} Piso: ${piso} Depto: ${depto} - (${postalCode}) ${localidad} - ${provincia} - ${pais}`
    }

    getFormattedDocument(customerInfo) {
        const tipoDoc = _.get(customerInfo, 'idTipoDocumento.nombreTipoDocumento', '');
        const documento = _.get(customerInfo, 'documento', '');

        return `${tipoDoc} ${documento}`
    }

    getFormattedTelefonos(customerInfo) {
        const telefonos = _.get(customerInfo, 'personasTelefonosList');
        let formattedTelefonos = '';

        if (telefonos) {
            formattedTelefonos = telefonos.map((telefono) => {
                return `${telefono.numero} (${telefono.referencia})`
            }).join('\n');
        }

        return formattedTelefonos;


    }

    handleGoNext() {
        if (_.get(this.state.customerInfo, 'id', null) !== null) {
            this.shopCartStore.setCustomerInfo(this.state.customerInfo);
            this.props.goNextCallback();
        }
    }

    handleGoBack() {
        this.props.goBackCallback();
    }
}
