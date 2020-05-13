import React, {Component} from "react";
import {DeliveryNotesService} from "../../service/DeliveryNotesService";
import {Growl} from "primereact/growl";
import {StockService} from "../../service/StockService";
import {Dropdown} from "primereact/dropdown";
import {Button} from "primereact/button";
import {LoadingButton} from "../core/LoadingButton";
import {Calendar} from "primereact/calendar";
import moment from "moment";
import {DEFAULT_DATA_TABLE_PROPS} from "../DefaultProps";
import {SearchProductsDialog} from "../core/SearchProductsDialog";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import FileOutputsService from "../../service/FileOutputsService";
import {AutoComplete} from "primereact/autocomplete";
import {ProductsService} from "../../service/ProductsService";


export class ProductHistory extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            warehouses: [],
            warehouse: null,
            selectedProduct: null,
            productMovements: [],
            showSearchProductsDialog: false,
            filteredProducts: [],
            loading: false,
            fromDate: moment().subtract(1, 'months').toDate()
        }

        this.deliveryNotesService = new DeliveryNotesService();
        this.stockService = new StockService();
        this.productsService = new ProductsService();
    }

    componentDidMount() {
        const {warehouses} = this.state;

        if (warehouses.length === 0) {
            this.deliveryNotesService.getWarehouses((warehouses) => this.setState({
                warehouses: warehouses,
                warehouse: warehouses[0]
            }));
        }
    }

    render() {
        const {warehouses, warehouse, selectedProduct, filteredProducts} = this.state;

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                {this.renderSearchProductsDialog()}
                <h1>Historia de un producto</h1>

                <div className="p-grid p-fluid">
                    <div className="p-col-8">
                        <label>Producto:</label>
                        <div className="p-inputgroup">
                            <AutoComplete minLength={2} placeholder="Comience a escribir para buscar un producto"
                                          autoFocus={true}
                                          delay={500}
                                          id="producto"
                                          completeMethod={(event) => this.filterProducts(event.query)}
                                          suggestions={filteredProducts}
                                          field="descripcion"
                                          required={true}
                                          onChange={(event) => this.setState({selectedProduct: event.value})}
                                          value={selectedProduct || ''}

                            />

                            <Button type="button"
                                    className="shop-cart--search-product-button"
                                    icon="fa fa-fw fa-search"
                                    tooltip={'Buscar productos'}
                                    onClick={() => this.setState({showSearchProductsDialog: true})}
                            />
                        </div>
                    </div>
                    <div className="p-col-6">
                        <label htmlFor="warehouse">Depósito:</label>
                        <Dropdown id="warehouse"
                                  optionLabel="displayName"
                                  placeholder="Seleccione un depósito"
                                  options={warehouses}
                                  value={warehouse}
                                  onChange={(e) => this.setState({warehouse: e.value})}/>

                    </div>

                    <div className="p-col-6">
                        <label htmlFor="fromDate">Fecha desde:</label>
                        <Calendar id="fromDate"
                                  showTime={true}
                                  showIcon={true}
                                  hourFormat="24"
                                  dateFormat="dd/mm/yy"
                                  value={this.state.fromDate}
                                  onChange={(e) => this.setState({fromDate: e.value})}/>

                    </div>

                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-filter"
                                   label={"Buscar movimientos"}
                                   className="p-button-success"
                                   onClick={this.filterMovements}
                                   loading={this.state.loading}
                                   tooltip={'Buscar movimientos'}
                                   disabled={!this.canPerformSearch()}/>


                    <DataTable {...this.getItemsTableProps()}>

                        <Column header={"Fecha"} field={"movementDate"}/>
                        <Column header={"Código"} field={"productCode"}/>
                        <Column header={"Descripción"} field={"productDescription"}/>
                        <Column header={"Unidad"} field={"saleUnit"}/>
                        <Column header={"Cantidad"} field={"movementQuantity"}/>
                        <Column header={"Stock Parcial"} field={"partialStock"}/>
                        <Column header={"Origen"} field={"deliveryNoteOrigin"}/>
                        <Column header={"Destino"} field={"deliveryNoteDestination"}/>
                        <Column header={"Tipo"} field={"movementType"}/>
                        <Column header={"Remito"} field={"deliveryNoteId"} body={this.getLinkActions}/>

                    </DataTable>
                </div>
            </div>
        )
    }

    renderSearchProductsDialog = () => {
        return (
            <SearchProductsDialog visible={this.state.showSearchProductsDialog}
                                  modal={true}
                                  acceptCallback={(product) => this.setState({selectedProduct: product})}
                                  onHide={() => this.setState({showSearchProductsDialog: false})}/>
        )
    }

    getLinkActions = (rowData) => {
        return (
            <Button type="button" icon="fa fa-fw fa-print" label={rowData.deliveryNoteId}
                    onClick={() => FileOutputsService.getDeliveryNote(rowData.deliveryNoteId)}/>
        )
    }

    canPerformSearch = () => {
        const {fromDate, selectedProduct, warehouse} = this.state;

        return fromDate && selectedProduct && warehouse;
    }

    getItemsTableProps() {
        let header = <div className="p-clearfix">Movimientos</div>;
        let footer = (
            <div className="p-clearfix">
                <label>Movimientos: {this.state.productMovements.length}</label>
            </div>);

        return {
            ...DEFAULT_DATA_TABLE_PROPS,
            ...{
                value: this.state.productMovements,
                loading: this.state.loading,
                header: header,
                footer: footer,
                resizableColumns: true,
                emptyMessage: 'No hay movimientos para el producto solicitado'
            }
        }
    }

    filterMovements = () => {
        let filter = {
            fromDate: this.state.fromDate,
            productId: this.state.selectedProduct.productId,
            warehouseId: this.state.warehouse.warehouseId
        }

        this.stockService.getProductMovementsHistory(filter, (movements) => this.setState({
            loading: false,
            productMovements: movements
        }))
        this.setState({loading: true});
    }

    handleError = (error) => {
        this.setState({loadingAddProduct: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudo encontrar el producto',
            detail: error.response.data.message
        });
    }

    filterProducts = (query) => {
        let searchOptions = {
            searchFilter: {
                activo: true,
                puedeVenderse: true,
                buscarEnTodosLados: true,
                txt: query,
                sortFields: [{
                    fieldName: "descripcion",
                    ascending: true
                }],
            },
            firstResult: 0,
            maxResults: 5
        }

        this.productsService.searchProducts(searchOptions,
            (response) => this.setState({filteredProducts: response.data}));
    }

}
