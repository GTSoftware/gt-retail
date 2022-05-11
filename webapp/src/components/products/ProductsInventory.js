import _ from "lodash"
import React, { Component, useEffect, useState } from "react"
import { ProductsService } from "../../service/ProductsService"
import { SearchProductsFilter } from "../core/SearchProductsFilter"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../utils/DateUtils"
import { Button } from "primereact/button"

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

export const ProductsInventory = () => {
  const productsService = new ProductsService()

  const [loading, setLoading] = useState(false)
  const [rows, setRows] = useState(20)
  const [first, setFirst] = useState(0)
  const [totalRecords, setTotalRecords] = useState(0)
  const [selectedIds, setSelectedIds] = useState(new Map())
  const [products, setProducts] = useState([])
  const [searchFilter, setSearchFilter] = useState(null)

  const renderFilterSection = () => {
    return (
      <SearchProductsFilter
        searchProductsCallback={searchProducts}
        loading={loading}
      />
    )
  }

  const renderSearchResults = () => {
    return (
      <DataTable
        value={products}
        dataKey={"productId"}
        paginator={true}
        rows={rows}
        totalRecords={totalRecords}
        lazy={true}
        first={first}
        rowClassName={getRowClass}
        onPage={onPageEvent}
        loading={loading}
        loadingIcon="fa fa-fw fa-spin fa-spinner"
        resizableColumns
      >
        {renderColumns()}
      </DataTable>
    )
  }

  const getRowClass = (product) => {
    return { "p-highlight": selectedIds.get(product.productId) }
  }

  const renderColumns = () => {
    let columns = productColumns.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          style={col.style}
          body={(rowData) => getColumnBody(col, rowData)}
        />
      )
    })

    columns.push(<Column body={getLinkActions} />)

    return columns
  }

  const searchProducts = (searchFilter) => {
    if (!loading) {
      let searchOptions = {
        firstResult: 0,
        maxResults: rows,
        searchFilter: searchFilter,
      }

      productsService.searchProducts(searchOptions, handleSuccess, handleError)

      setLoading(true)
      setFirst(0)
      setSearchFilter(searchFilter)
    }
  }

  const onPageEvent = (event) => {
    let searchOptions = {
      firstResult: event.first,
      maxResults: rows,
      searchFilter: searchFilter,
    }

    productsService.searchProducts(searchOptions, handleSuccess, handleError)

    setLoading(true)
    setFirst(event.first)
  }

  const handleSuccess = (data) => {
    const foundProducts = data.data
    const totalRows = data.totalResults

    setProducts(foundProducts)
    setTotalRecords(totalRows)
    setLoading(false)
  }

  const handleError = (errorData) => {
    setProducts([])
    setTotalRecords(0)
    setLoading(false)
  }

  const getColumnBody = (col, rowData) => {
    const field = _.get(rowData, col.field)

    if (col.format) {
      return col.format(field)
    }

    return field
  }

  const getLinkActions = (rowData) => {
    const { productId } = rowData

    return (
      <Button
        type="button"
        icon="fa fa-fw fa-edit"
        onClick={() => handleEditProduct(productId)}
      />
    )
  }

  const handleEditProduct = (productId) => {
    let newSelectedIds = new Map(selectedIds)

    newSelectedIds.set(productId, true)

    setSelectedIds(newSelectedIds)

    window.open(`#/product/${productId}`, "_blank")
  }

  return (
    <div className="card card-w-title">
      <h1>Mayor de productos</h1>
      {renderFilterSection()}
      <Button
        type="button"
        label={"Nuevo"}
        icon="fa fa-fw fa-plus"
        onClick={() => window.open(`#/product/`, "_blank")}
      />
      {renderSearchResults()}
    </div>
  )
}
