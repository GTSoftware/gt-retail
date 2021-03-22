import React, { Component } from "react"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import PropTypes from "prop-types"
import { formatDate } from "../../utils/DateUtils"

const productColumns = [
  { field: "productId", header: "Id", index: 1 },
  { field: "codigoPropio", header: "Código", index: 2 },
  { field: "descripcion", header: "Descripción", style: { width: "30%" }, index: 4 },
  {
    field: "precioVenta",
    header: "Precio",
    style: { fontWeight: "bold" },
    index: 5,
  },
  { field: "saleUnit", header: "Unidad", index: 6 },
  { field: "brand.brandName", header: "Marca", index: 7 },
]

const productAdditionalColumns = [
  { field: "codigoFabricante", header: "Código fabricante", index: 3 },
  { field: "fechaUltimaModificacion", header: "Fecha Ult.Modif", index: 8 },
]

const stockColumns = [{ field: "stockActualEnSucursal", header: "Stock", index: 6 }]

export class SearchProductsTable extends Component {
  static propTypes = {
    products: PropTypes.array.isRequired,
    onPageEvent: PropTypes.func.isRequired,
    rows: PropTypes.number.isRequired,
    totalRecords: PropTypes.number.isRequired,
    emptyMessage: PropTypes.string,
    showAdditionalColumns: PropTypes.bool,
    showStockColumns: PropTypes.bool,
  }

  constructor(props, context) {
    super(props, context)

    this.state = {
      loading: false,
      first: 0,
      totalRecords: props.totalRecords || 0,
    }
  }

  render() {
    const props = this.props

    return (
      <DataTable
        value={props.products}
        paginator={true}
        rows={props.rows}
        totalRecords={props.totalRecords}
        lazy={true}
        first={this.state.first}
        onPage={this.onPageEvent}
        loading={this.state.loading}
        emptyMessage={props.emptyMessage}
        resizableColumns={true}
        loadingIcon="fa fa-fw fa-spin fa-spinner"
      >
        {this.renderColumns()}
      </DataTable>
    )
  }

  renderColumns = () => {
    let sortedColumnFields = this.getProductColumns()

    return sortedColumnFields.map((col, i) => {
      let body = null
      if (col.field === "fechaUltimaModificacion") {
        body = (rowData) => this.formatDate(rowData, col.field)
      }
      return (
        <Column
          key={col.field}
          field={col.field}
          body={body}
          header={col.header}
          style={col.style}
        />
      )
    })
  }

  getProductColumns = () => {
    let columns = productColumns.slice()

    if (this.props.showAdditionalColumns) {
      columns = columns.concat(productAdditionalColumns)
    }

    if (this.props.showStockColumns) {
      columns = columns.concat(stockColumns)
    }

    columns.sort((firstCol, SecondCol) => {
      return firstCol.index - SecondCol.index
    })

    return columns
  }

  onPageEvent = (event) => {
    let pageOptions = {
      firstResult: event.first,
      maxResults: this.state.rows,
    }

    this.props.onPageEvent(pageOptions)

    this.setState({
      //loading: true,
      first: event.first,
    })
  }

  formatDate = (rowData, fieldName) => {
    return formatDate(rowData[fieldName])
  }
}
