import React, {Component} from "react";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import PropTypes from 'prop-types';


const productColumns = [
    {field: 'productId', header: 'Id'},
    {field: 'codigoPropio', header: 'Código'},
    {field: 'descripcion', header: 'Descripción', style: {width: "30%"}},
    {field: 'precioVenta', header: 'Precio', style: {"fontWeight": "bold"}},
    {field: 'saleUnit', header: 'Unidad'},
    {field: 'stockActualEnSucursal', header: 'Stock'},
    {field: 'brand.brandName', header: 'Marca'}
];

export class SearchProductsTable extends Component {

    static propTypes = {
        products: PropTypes.array.isRequired,
        onPageEvent: PropTypes.func.isRequired,
        rows: PropTypes.number.isRequired,
        totalRecords: PropTypes.number.isRequired,
        emptyMessage: PropTypes.string
    }

    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: false,
            first: 0,
            totalRecords: props.totalRecords || 0
        }

    }

    render() {
        const props = this.props;

        return (
            <DataTable value={props.products}
                       paginator={true}
                       rows={props.rows}
                       totalRecords={props.totalRecords}
                       lazy={true}
                       first={this.state.first}
                       onPage={this.onPageEvent}
                       loading={this.state.loading}
                       emptyMessage={props.emptyMessage}
                       responsive={true}
                       loadingIcon="fa fa-fw fa-spin fa-spinner">

                {this.renderColumns()}

            </DataTable>
        )
    }

    renderColumns = () => {
        let columns = productColumns.map((col, i) => {
            return <Column key={col.field} field={col.field} header={col.header} style={col.style}/>;
        });

        return columns;
    }

    onPageEvent = (event) => {
        let pageOptions = {
            firstResult: event.first,
            maxResults: this.state.rows
        }

        this.props.onPageEvent(pageOptions);

        this.setState({
            //loading: true,
            first: event.first
        });
    }

}
