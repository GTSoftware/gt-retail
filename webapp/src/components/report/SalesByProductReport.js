import React, {Component} from 'react';
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {ReportsService} from "../../service/ReportsService";
import {Button} from "primereact/button";
import {LoadingButton} from "../core/LoadingButton";
import {getBeginOfMonth, getEndOfMonth, serializeDate} from "../../utils/DateUtils";
import {Calendar} from "primereact/calendar";

export class SalesByProductReport extends Component {


    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: false,
            soldProducts: [],
            fromDate: getBeginOfMonth(),
            toDate: getEndOfMonth()
        }

        this.reportsService = new ReportsService();
    }

    render() {
        const {soldProducts, loading} = this.state;
        let header = (
            <div style={{textAlign: 'left'}}>
                <Button type="button" icon="fa fa-fw fa-download"
                        iconPos="left" label="CSV"
                        onClick={this.exportTable}/>
            </div>
        );

        return (
            <div className="card card-w-title">

                <h1 style={{fontSize: '16px'}}>Reporte de ventas por producto</h1>
                <div className="p-grid p-fluid">

                    {this.renderFilterSection()}

                    <DataTable value={soldProducts} style={{marginBottom: '20px'}} responsive={true}
                               loadingIcon="fa fa-spin fa-spinner"
                               loading={loading}
                               rows={10}
                               paginator={true}
                               header={header}
                               emptyMessage={"Presione filtrar para generar el reporte"}
                               ref={(el) => {
                                   this.reportTable = el;
                               }}>
                        <Column field="productCode" header="Código" sortable={true}/>
                        <Column field="productDescription" style={{width: "40%"}} header="Descripción"
                                sortable={true}/>
                        <Column field="supplierCode" header="Cód Fábrica" sortable={true}/>
                        <Column field="supplier" header="Proveedor" sortable={true}/>
                        <Column field="saleUnit" header="Unidad" sortable={true}/>
                        <Column field="minimumStock" header="Stock Mínimo" sortable={true}/>
                        <Column field="totalStock" header="Stock Total" sortable={true}/>
                        <Column field="soldQuantity" header="Cantidad vendida" sortable={true}/>
                        <Column field="totalSalesCost" header="Costo de ventas" sortable={true}/>
                        <Column field="salePriceTotal" header="Total precio de venta" sortable={true}/>
                        <Column field="totalSales" header="Cantidad de ventas" sortable={true}/>
                    </DataTable>
                </div>
            </div>
        );
    }

    renderFilterSection = () => {
        const {loading, fromDate, toDate} = this.state;

        return (
            <>
                <div className="p-col-4">
                    <label htmlFor="fromDate">Fecha desde:</label>
                    <Calendar id="fromDate"
                              showTime={false}
                              showIcon={true}
                              dateFormat="dd/mm/yy"
                              value={fromDate}
                              onChange={(e) => this.setState({fromDate: e.value})}/>

                </div>
                <div className="p-col-4">
                    <label htmlFor="fromDate">Fecha hasta:</label>
                    <Calendar id="fromDate"
                              showTime={false}
                              showIcon={true}
                              dateFormat="dd/mm/yy"
                              value={toDate}
                              onChange={(e) => this.setState({fromDate: e.value})}/>

                </div>
                <div className="p-col-12">
                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-filter"
                                   loading={loading}
                                   label={"Filtrar"}
                                   onClick={this.getReport}/>
                </div>
            </>
        )
    }

    getReport = () => {
        this.setState({loading: true});

        this.reportsService.getSalesByProductReport(
            this.getSearchFilter(),
            this.handleReport)
    }

    getSearchFilter = () => {
        const {fromDate, toDate} = this.state;

        return {
            fechaDesde: serializeDate(fromDate),
            fechaHasta: serializeDate(toDate)
        }
    }

    exportTable = () => {
        this.reportTable.exportCSV();
    }

    handleReport = (soldProducts) => {
        this.setState({
            loading: false,
            soldProducts: soldProducts
        })
    }
}
