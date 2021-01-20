import _ from "lodash";
import React, {Component} from "react";
import {Growl} from "primereact/growl";
import {Column} from "primereact/column";
import {DataTable} from "primereact/datatable";
import {DEFAULT_DATA_TABLE_PROPS} from "../DefaultProps";
import {Button} from "primereact/button";
import FileOutputsService from "../../service/FileOutputsService";
import {LoadingButton} from "../core/LoadingButton";
import {formatDate, getBeginOfToday, getEndOfToday, serializeDate} from "../../utils/DateUtils";
import {Calendar} from "primereact/calendar";
import {TabPanel, TabView} from "primereact/tabview";
import ShopCartService from "../../service/ShopCartService";
import {MultiSelect} from "primereact/multiselect";
import {AutoComplete} from "primereact/autocomplete";
import {TriStateCheckbox} from "primereact/tristatecheckbox";
import LoginService from "../../service/LoginService";
import {PaymentPendingSalesService} from "../../service/PaymentPendingSalesService";
import {PayDialog} from "./PayDialog";

export class SearchPendingSales extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {

            sales: [],
            invoicedTotal: 0,
            notInvoicedTotal: 0,

            saleTypes: [],
            filteredCustomers: [],
            loading: false,
            selectedSales: [],

            fromDate: getBeginOfToday(),
            toDate: getEndOfToday(),
            selectedSaleTypes: [],
            selectedCustomer: null,
            facturada: null,

            rows: 10,
            first: 0,
            totalRecords: 0,
            adminUser: LoginService.hasUserRole('ADMINISTRADORES'),

            prepareToPay: null,
            showPayDialog: false

        }

        this.salesService = new PaymentPendingSalesService();
    }

    componentDidMount() {
        const {saleTypes} = this.state;

        if (saleTypes.length === 0) {
            ShopCartService.getSaleTypes((saleTypes) => this.setState({saleTypes: saleTypes}));
        }

    }

    render() {

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                <h1>Comprobantes pendientes de cobro</h1>
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        <TabView>
                            <TabPanel header="Búsqueda estándar" leftIcon="fa fa-fw fa-search">
                                <div className="p-grid p-fluid">
                                    <div className="p-col-4">
                                        <label htmlFor="fromDate">Fecha desde:</label>
                                        <Calendar id="fromDate"
                                                  showTime={true}
                                                  showIcon={true}
                                                  hourFormat="24"
                                                  dateFormat="dd/mm/yy"
                                                  value={this.state.fromDate}
                                                  onChange={(e) => this.setState({fromDate: e.value})}/>

                                    </div>

                                    <div className="p-col-4">
                                        <label htmlFor="toDate">Fecha hasta:</label>
                                        <Calendar id="toDate"
                                                  showTime={true}
                                                  showIcon={true}
                                                  hourFormat="24"
                                                  dateFormat="dd/mm/yy"
                                                  value={this.state.toDate}
                                                  onChange={(e) => this.setState({toDate: e.value})}/>

                                    </div>

                                    <div className="p-col-4">
                                        <label htmlFor="saleTypes">Tipos de comprobante: </label>
                                        <MultiSelect id="saleTypes"
                                                     optionLabel="nombreComprobante"
                                                     options={this.state.saleTypes}
                                                     value={this.state.selectedSaleTypes}
                                                     onChange={(event) => {
                                                         this.setState({selectedSaleTypes: event.value})
                                                     }}/>

                                    </div>
                                </div>
                            </TabPanel>
                            <TabPanel header="Búsqueda avanzada" leftIcon="fa fa-fw fa-cog">
                                <div className="p-col-4">
                                    <label htmlFor="clienteFilter">Cliente:</label>
                                    <AutoComplete minLength={2} placeholder="Comience a escribir para buscar un cliente"
                                                  delay={500}
                                                  id="clienteFilter"
                                                  completeMethod={(event) => this.filterCustomers(event.query)}
                                                  suggestions={this.state.filteredCustomers}
                                                  field="businessName"
                                                  onChange={event => this.setState({selectedCustomer: event.value})}
                                                  value={this.state.selectedCustomer || ''}

                                    />
                                </div>
                                <div className="p-col-4">
                                    <label htmlFor="invoiceStatusFilter">Facturada:</label>
                                    <TriStateCheckbox id="invoiceStatusFilter"
                                                      tooltip={this.getInvoiceFilterStatus()}
                                                      value={this.state.facturada}
                                                      onChange={(e) => this.setState({facturada: e.value})}/>
                                </div>
                            </TabPanel>

                        </TabView>
                    </div>

                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-filter"
                                   label={"Buscar comprobantes"}
                                   className="p-button-success"
                                   onClick={() => this.filterSales(0)}
                                   loading={this.state.loading}
                                   tooltip={'Buscar comprobantes'}/>

                    <DataTable {...this.getItemsTableProps()}>
                        <Column selectionMode="multiple" style={{width: '3em'}}/>
                        <Column header={"Fecha"} body={(rowData) => {
                            return formatDate(rowData.saleDate)
                        }}/>
                        <Column header={"Tipo"} field={"saleType"}/>
                        <Column header={"Cliente"} field={"customer"}/>
                        <Column header={"Factura"} field={"invoiceNumber"}/>
                        <Column header={"Vendedor"} field={"user"}/>
                        <Column header={"Sucursal"} field={"branch"}/>
                        <Column header={"Total"} field={"total"}/>
                        <Column header={"Saldo"} field={"remainingAmount"}/>
                        <Column header={"Condición"} field={"saleCondition"}/>
                        <Column header={"Imprimir"} body={this.getLinkActions}/>

                    </DataTable>

                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-money"
                                   disabled={this.state.selectedSales.length === 0}
                                   label={this.getCobrarComprobantesLabel()}
                                   onClick={() => this.prepareToPay()}
                                   loading={this.state.loading}
                                   tooltip={'Cobrar comprobantes'}/>

                </div>
                {!this.state.loading && this.renderInvoiceDialog()}

            </div>
        )
    }

    renderInvoiceDialog = () => {
        const {showPayDialog, prepareToPay} = this.state;

        return (
            showPayDialog && <PayDialog visible={showPayDialog}
                                        payingCustomer={prepareToPay.customer}
                                        banks={prepareToPay.banks}
                                        successCallback={() => this.setState({showPayDialog: false})}
                                        salesToPay={prepareToPay.salesToPay}
                                        noExtraCostPaymentMethods={prepareToPay.noExtraCostPaymentMethods}
                                        onHide={() => this.setState({showPayDialog: false})}/>
        );
    }

    getItemsTableProps = () => {
        const {sales, rows, first, totalRecords, loading, selectedSales} = this.state;
        const header = <div className="p-clearfix">Comprobantes</div>;
        const footer = (
            <div className="p-clearfix">
                <label>Comprobantes: {totalRecords}</label>
            </div>);

        return {
            ...DEFAULT_DATA_TABLE_PROPS,
            ...{
                value: sales,
                loading: loading,
                header: header,
                footer: footer,
                rows: rows,
                rowKey: 'saleId',
                totalRecords: totalRecords,
                lazy: true,
                selection: selectedSales,
                onSelectionChange: (e) => this.handleSelectionChange(e.value),
                first: first,
                onPage: this.onPageEvent,
                resizableColumns: true,
                emptyMessage: 'No hay comprobantes que coincidan con la búsqueda'
            }
        }
    }

    handleSelectionChange = (newSelectedSales) => {
        if (this.validateSelectedSales(newSelectedSales)) {
            this.setState({selectedSales: newSelectedSales});
        }
    }

    validateSelectedSales = (selectedSales) => {
        let valid = true;
        if (selectedSales.length > 0) {
            let customerId = selectedSales[0].customerId;
            let sale = selectedSales.filter((sale) => sale.customerId !== customerId);
            if (sale.length > 0) {
                valid = false;
            }
        }

        return valid;
    }

    getCobrarComprobantesLabel = () => {
        const {selectedSales} = this.state;
        let label = 'Cobrar comprobantes'

        if (selectedSales.length > 0) {
            let amount = 0;
            selectedSales.forEach((sale) => {
                amount += sale.remainingAmount
            });
            label = label + ' por: $' + amount.toFixed(2);
        }

        return label;
    }

    getInvoiceFilterStatus = () => {
        let status = '';

        if (this.state.facturada === true) {
            status = 'Sí';
        } else if (this.state.facturada === false) {
            status = 'No';
        }

        return status;
    }

    getLinkActions = (rowData) => {
        let viewSaleButton = (
            <Button type="button" icon="fa fa-fw fa-search"
                    label={`${rowData.saleId}`}
                    tooltip={"Ver comprobante"}
                    onClick={() => window.location = `#/sale/${rowData.saleId}`}/>
        )
        let buttonToRender = (
            <Button type="button" icon="fa fa-fw fa-print"
                    label={`${rowData.saleId}`}
                    tooltip={"Imprimir presupuesto"}
                    onClick={() => FileOutputsService.getSaleBudget(rowData.saleId)}/>
        );

        if (rowData.invoiceNumber) {
            buttonToRender = (
                <Button type="button" icon="fa fa-fw fa-print"
                        label={`${rowData.saleId}`}
                        tooltip={"Imprimir factura"}
                        onClick={() => FileOutputsService.getInvoice(rowData.saleId)}/>
            );
        }

        return (
            <div>
                {viewSaleButton}
                {buttonToRender}
            </div>);
    }

    onPageEvent = (event) => {
        this.filterSales(event.first);
    }

    filterSales = (first) => {
        let searchOptions = this.getPaginatedSearchOptions(first);

        this.salesService.searchSales(searchOptions,
            (sales) => this.setState({
                loading: false,
                sales: sales.data,
                first: searchOptions.firstResult,
                totalRecords: sales.totalResults
            }));

        this.setState({loading: true});
    }

    getPaginatedSearchOptions = (first) => {
        const {fromDate, toDate, rows, selectedSaleTypes, selectedCustomer, facturada} = this.state;

        return {
            firstResult: first || 0,
            maxResults: rows,
            searchFilter: {
                fechaVentaDesde: serializeDate(fromDate),
                fechaVentaHasta: serializeDate(toDate),
                idTiposComprobanteList: selectedSaleTypes.map((saleType) => {
                    return saleType.id
                }),
                idPersona: _.get(selectedCustomer, 'customerId', null),
                facturada: facturada
            }
        }
    }

    filterCustomers = (query) => {
        ShopCartService.searchCustomers(query, (customers) => {
            this.setState({filteredCustomers: customers})
        });
    }

    prepareToPay = () => {
        const {selectedSales} = this.state;

        this.salesService.prepareToPay(selectedSales,
            (prepareToPay) => this.setState({
                prepareToPay: prepareToPay,
                showPayDialog: true
            }),
            (error) => this.handlePrepareToPayError(error));
    }

    handlePrepareToPayError = (error) => {
        this.setState({
            prepareToPay: null,
            showPayDialog: false
        });
        this.growl.show({
            severity: 'error',
            summary: 'No se pueden cobrar los comprobantes seleccionados',
            detail: _.get(error, 'response.data.message', '')
        });
    }
}
