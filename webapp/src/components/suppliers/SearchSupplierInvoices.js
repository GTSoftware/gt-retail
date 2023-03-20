import _ from "lodash"
import React, { useRef, useState } from "react"
import { Toast } from "primereact/toast"
import { Column } from "primereact/column"
import { DataTable } from "primereact/datatable"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { Button } from "primereact/button"
import FileOutputsService from "../../service/FileOutputsService"
import { LoadingButton } from "../core/LoadingButton"
import {
  formatDate,
  getBeginOfToday,
  getEndOfToday,
  serializeDate,
} from "../../utils/DateUtils"
import { Calendar } from "primereact/calendar"
import { SalesService } from "../../service/SalesService"
import { TabPanel, TabView } from "primereact/tabview"
import ShopCartService from "../../service/ShopCartService"
import { MultiSelect } from "primereact/multiselect"
import { AutoComplete } from "primereact/autocomplete"
import { TriStateCheckbox } from "primereact/tristatecheckbox"
import LoginService from "../../service/LoginService"

import { InvoicePrintSplitButton } from "../core/InvoicePrintSplitButton"
import { BudgetPrintSplitButton } from "../core/BudgetPrintSplitButton"
import { AutocompleteSupplierFilter } from "../core/AutocompleteSupplierFilter"
import { SuppliersService } from "../../service/SuppliersService"
import { exportExcel } from "../../utils/ExcelExporter"

export const SearchSupplierInvoices = () => {
  const [fromDate, setFromDate] = useState(getBeginOfToday())
  const [toDate, setToDate] = useState(getEndOfToday())
  const [invoices, setInvoices] = useState([])
  const rows = 15
  const [first, setFirst] = useState(0)
  const [totalRecords, setTotalRecords] = useState(0)
  const [loading, setLoading] = useState(false)
  const [selectedSupplier, setSelectedSupplier] = useState(null)
  const toast = useRef()
  const suppliersService = new SuppliersService()

  const getItemsTableProps = () => {
    const header = <div className="p-clearfix">Comprobantes</div>
    const footer = (
      <div className="p-clearfix">
        <Button
          type="button"
          icon="fa fa-fw fa-file-excel"
          onClick={exportToExcel}
        />
        <label>Comprobantes: {totalRecords}</label>
      </div>
    )

    return {
      ...DEFAULT_DATA_TABLE_PROPS,
      ...{
        value: invoices,
        loading: loading,
        header: header,
        footer: footer,
        rows: rows,
        totalRecords: totalRecords,
        lazy: true,
        first: first,
        onPage: onPageEvent,
        resizableColumns: true,
        emptyMessage: "No hay comprobantes que coincidan con la búsqueda",
      },
    }
  }

  // const getLinkActions = (rowData) => {
  //   let viewSaleButton = (
  //     <Button
  //       type="button"
  //       icon="fa fa-fw fa-search"
  //       label={`${rowData.saleId}`}
  //       tooltip={"Ver comprobante"}
  //       onClick={() => (window.location = `#/sale/${rowData.saleId}`)}
  //     />
  //   )
  //   let buttonToRender = <BudgetPrintSplitButton saleId={rowData.saleId} />
  //
  //   if (rowData.invoiceNumber) {
  //     buttonToRender = <InvoicePrintSplitButton saleId={rowData.saleId} />
  //   }
  //
  //   return (
  //     <div>
  //       {viewSaleButton}
  //       {buttonToRender}
  //     </div>
  //   )
  // }

  const onPageEvent = (event) => {
    filterInvoices(event.first)
  }

  const handleSearchInvoices = (firstResult, invoices) => {
    setLoading(false)
    setInvoices(invoices.data)
    setTotalRecords(invoices.totalResults)
    setFirst(firstResult)
  }

  const filterInvoices = (firstResult) => {
    const searchOptions = getPaginatedSearchOptions(firstResult)

    suppliersService.searchInvoices(
      searchOptions,
      (invoices) => handleSearchInvoices(firstResult, invoices),
      handleSearchError
    )
  }

  const getPaginatedSearchOptions = (first) => {
    return {
      firstResult: first,
      maxResults: rows,
      fromDate,
      toDate,
      selectedSupplier,
    }
  }

  const exportToExcel = () => {
    if (totalRecords) {
      let searchOptions = getPaginatedSearchOptions(0)
      searchOptions.maxResults = totalRecords

      suppliersService.searchInvoices(
        searchOptions,
        (sales) => exportExcel(sales.data, "comprobantes_proveedor"),
        handleSearchError
      )
    }
  }

  const handleSearchError = (error) => {
    toast.current.show({
      severity: "error",
      summary: "No se pudo realizar la búsqueda",
      detail: _.get(error, "response.data.message", ""),
    })
    setLoading(false)
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Búsqueda de comprobantes de proveedor</h1>
      <div className="p-grid p-fluid">
        <div className="p-col-12">
          <div className="p-grid p-fluid">
            <div className="p-col-4">
              <label htmlFor="fromDate">Fecha desde:</label>
              <Calendar
                id="fromDate"
                showTime={true}
                showIcon={true}
                hourFormat="24"
                dateFormat="dd/mm/yy"
                value={fromDate}
                onChange={(e) => setFromDate(e.value)}
              />
            </div>

            <div className="p-col-4">
              <label htmlFor="toDate">Fecha hasta:</label>
              <Calendar
                id="toDate"
                showTime={true}
                showIcon={true}
                hourFormat="24"
                dateFormat="dd/mm/yy"
                value={toDate}
                onChange={(e) => setToDate(e.value)}
              />
            </div>
            <div className="p-col-4">
              <label htmlFor="supplierFilter">Proveedor:</label>
              <AutocompleteSupplierFilter
                selectedSupplier={selectedSupplier}
                onSupplierSelect={setSelectedSupplier}
              />
            </div>
          </div>
        </div>

        <LoadingButton
          type="button"
          icon="fa fa-fw fa-filter"
          label={"Buscar comprobantes"}
          className="p-button-success"
          onClick={() => filterInvoices(0)}
          loading={loading}
          tooltip={"Buscar comprobantes"}
        />

        <DataTable {...getItemsTableProps()}>
          <Column
            header={"Fecha"}
            body={(rowData) => {
              return formatDate(rowData.invoiceDate)
            }}
          />
          <Column header={"Proveedor"} field={"supplier"} />
          <Column header={"Tipo"} field={"invoiceType"} />
          <Column header={"Número"} field={"number"} />
          <Column header={"Total"} field={"total"} />
          <Column header={"Id"} field={"invoiceId"} />
        </DataTable>
      </div>
    </div>
  )
}
