import _ from "lodash";
import React, {Component} from "react";
import {DeliveryNotesService} from "../../service/DeliveryNotesService";
import {ProductsService} from "../../service/ProductsService";
import {Growl} from "primereact/growl";
import {AutocompleteProductFilter} from "../core/AutocompleteProductFilter";
import {Column} from "primereact/column";
import {DataTable} from "primereact/datatable";
import {DEFAULT_DATA_TABLE_PROPS} from "../DefaultProps";
import {Button} from "primereact/button";
import FileOutputsService from "../../service/FileOutputsService";
import {LoadingButton} from "../core/LoadingButton";
import {formatDate, getBeginOfMonth, getEndOfMonth} from "../../utils/DateUtils";
import {Calendar} from "primereact/calendar";
import {Dropdown} from "primereact/dropdown";

export class SearchDeliveryNotes extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {

            deliveryTypes: [],
            deliveryNotes: [],
            loading: false,
            product: null,
            fromDate: getBeginOfMonth(),
            toDate: getEndOfMonth(),
            deliveryType: null,
            rows: 10,
            first: 0,
            totalRecords: 0

        }

        this.deliveryNotesService = new DeliveryNotesService();
        this.productsService = new ProductsService();
    }

    componentDidMount() {
        const {deliveryTypes} = this.state;

        if (deliveryTypes.length === 0) {
            this.deliveryNotesService.getDeliveryTypes((deliveryTypes) => this.setState({deliveryTypes: deliveryTypes}));
        }

    }

    render() {
        const {product, deliveryTypes, deliveryType} = this.state;

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                <h1>Búsqueda de remitos</h1>
                <div className="p-grid p-fluid">

                    <div className="p-col-8">
                        <label>Producto:</label>
                        <AutocompleteProductFilter selectedProduct={product}
                                                   onProductSelect={this.handleSelectedProduct}/>

                    </div>
                    <div className="p-col-4">
                        <label htmlFor="deliveryType">Tipo de movimiento:</label>

                        <Dropdown id="deliveryType"
                                  optionLabel="nombreTipo"
                                  placeholder="Seleccione el tipo de movimiento"
                                  options={deliveryTypes}
                                  value={deliveryType}
                                  showClear={true}
                                  onChange={(e) => this.setState({deliveryType: e.value})}/>
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

                    <div className="p-col-6">
                        <label htmlFor="toDate">Fecha hasta:</label>
                        <Calendar id="toDate"
                                  showTime={true}
                                  showIcon={true}
                                  hourFormat="24"
                                  dateFormat="dd/mm/yy"
                                  value={this.state.toDate}
                                  onChange={(e) => this.setState({toDate: e.value})}/>

                    </div>

                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-filter"
                                   label={"Buscar remitos"}
                                   className="p-button-success"
                                   onClick={() => this.filterDeliveryNotes(0)}
                                   loading={this.state.loading}
                                   tooltip={'Buscar remitos'}/>

                    <DataTable {...this.getItemsTableProps()}>

                        <Column header={"Fecha"} body={(rowData) => {
                            return formatDate(rowData.createdDate)
                        }}/>
                        <Column header={"Origen"} field={"origin"}/>
                        <Column header={"Destino"} field={"destination"}/>
                        <Column header={"Tipo"} field={"type"}/>
                        <Column header={"Usuario"} field={"user"}/>
                        <Column header={"Observaciones"} field={"observations"}/>
                        <Column header={"Remito"} field={"deliveryNoteId"} body={this.getLinkActions}/>

                    </DataTable>

                </div>

            </div>
        )
    }

    getItemsTableProps = () => {
        const {deliveryNotes, rows, first, totalRecords, loading} = this.state;
        const header = <div className="p-clearfix">Remitos</div>;
        const footer = (
            <div className="p-clearfix">
                <label>Remitos: {totalRecords}</label>
            </div>);

        return {
            ...DEFAULT_DATA_TABLE_PROPS,
            ...{
                value: deliveryNotes,
                loading: loading,
                header: header,
                footer: footer,
                rows: rows,
                totalRecords: totalRecords,
                lazy: true,
                first: first,
                onPage: this.onPageEvent,
                resizableColumns: true,
                emptyMessage: 'No hay remitos que coincidan con la búsqueda'
            }
        }
    }

    getLinkActions = (rowData) => {
        return (
            <Button type="button" icon="fa fa-fw fa-print" label={rowData.deliveryNoteId}
                    onClick={() => FileOutputsService.getDeliveryNote(rowData.deliveryNoteId)}/>
        )
    }

    handleSelectedProduct = (searchProduct) => {

        this.setState({product: searchProduct});

    }

    onPageEvent = (event) => {
        this.filterDeliveryNotes(event.first);
    }

    filterDeliveryNotes = (first) => {
        const {fromDate, toDate, product, rows, deliveryType} = this.state;
        let filter = {
            firstResult: first || 0,
            maxResults: rows,
            searchFilter: {
                idProducto: _.get(product, 'productId', null),
                fechaAltaDesde: fromDate,
                fechaAltaHasta: toDate,
                idTipoMovimiento: _.get(deliveryType, 'id', null)
            }
        }

        this.deliveryNotesService.searchDeliveryNotes(filter,
            (deliveryNotes) => this.setState({
                loading: false,
                deliveryNotes: deliveryNotes.data,
                first: filter.firstResult,
                totalRecords: deliveryNotes.totalResults
            }));

        this.setState({loading: true});
    }

}
