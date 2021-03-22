import _ from "lodash"
import React, { Component } from "react"
import { ProductsService } from "../../service/ProductsService"
import { SearchProductsFilter } from "../core/SearchProductsFilter"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../utils/DateUtils"

const productColumns = [
  { field: "codigoPropio", header: "Código" },
  { field: "codigoFabricante", header: "Cód. Fab." },
  { field: "descripcion", header: "Descripción", style: { width: "30%" } },
  { field: "brand.brandName", header: "Marca" },
  { field: "costoAdquisicion", header: "Costo Bruto" },
  { field: "costoFinal", header: "Costo Neto" },
  { field: "precioVenta", header: "Precio", style: { fontWeight: "bold" } },
  { field: "saleUnit", header: "Unidad" },
  { field: "stockActual", header: "Stock Total" },
  {
    field: "fechaUltimaModificacion",
    header: "Fecha Ult. Modif.",
    format: function (rowData) {
      return formatDate(rowData)
    },
  },
]

export class ProductsInventory extends Component {
  constructor(props, context) {
    super(props, context)
    this.productsService = new ProductsService()

    this.state = {
      loading: false,
      products: [],
      rows: 20,
    }

    this.productsService = new ProductsService()
  }

  componentDidMount() {}

  render() {
    return (
      <div className="card card-w-title">
        <h1>Mayor de productos</h1>
        {this.renderFilterSection()}
        {this.renderSearchResults()}
      </div>
    )
  }

  renderFilterSection = () => {
    return (
      <SearchProductsFilter
        searchProductsCallback={this.searchProducts}
        loading={this.state.loading}
      />
    )
  }

  renderSearchResults = () => {
    const {
      products,
      rows,
      totalRecords,
      first,
      loading,
      selectedProduct,
    } = this.state

    return (
      <DataTable
        value={products}
        paginator={true}
        rows={rows}
        totalRecords={totalRecords}
        lazy={true}
        first={first}
        onPage={this.onPageEvent}
        loading={loading}
        selectionMode="single"
        selection={selectedProduct}
        onSelectionChange={(e) => this.setState({ selectedProduct: e.value })}
        loadingIcon="fa fa-fw fa-spin fa-spinner"
        resizableColumns
      >
        {this.renderColumns()}
      </DataTable>
    )
  }

  renderColumns = () => {
    let columns = productColumns.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          style={col.style}
          body={(rowData) => this.getColumnBody(col, rowData)}
        />
      )
    })

    return columns
  }

  searchProducts = (searchFilter) => {
    const { loading, rows } = this.state

    if (!loading) {
      let searchOptions = {
        firstResult: 0,
        maxResults: rows,
        searchFilter: searchFilter,
      }

      this.productsService.searchProducts(
        searchOptions,
        this.handleSuccess,
        this.handleError
      )

      this.setState({
        loading: true,
        first: 0,
        searchFilter: searchFilter,
      })
    }
  }

  onPageEvent = (event) => {
    const { rows, searchFilter } = this.state

    let searchOptions = {
      firstResult: event.first,
      maxResults: rows,
      searchFilter: searchFilter,
    }

    this.productsService.searchProducts(
      searchOptions,
      this.handleSuccess,
      this.handleError
    )

    this.setState({
      loading: true,
      first: event.first,
    })
  }

  handleSuccess = (data) => {
    const foundProducts = data.data
    const totalRows = data.totalResults

    this.setState({
      products: foundProducts,
      totalRecords: totalRows,
      loading: false,
    })
  }

  handleError = (errorData) => {
    this.setState({
      products: [],
      totalRecords: 0,
      loading: false,
    })
  }

  getColumnBody = (col, rowData) => {
    const field = _.get(rowData, col.field)

    if (col.format) {
      return col.format(field)
    }

    return field
  }
}
