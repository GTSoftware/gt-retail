import React, { useEffect, useState } from "react"
import { Button } from "primereact/button"
import { CustomersService } from "../../service/CustomersService"
import { SearchPersonsFilter } from "../core/SearchPersonsFilter"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import _ from "lodash"

const CUSTOMER_COLUMNS = [
  { field: "personId", header: "Id" },
  { field: "businessName", header: "Nombre" },
  { field: "identification", header: "Identificación" },
  { field: "fantasyName", header: "Nombre Fantasía" },
  { field: "email", header: "e-mail" },
  { field: "address", header: "Dirección" },
]

export const CustomersInventory = () => {
  const [loading, setLoading] = useState(false)
  const [customers, setCustomers] = useState([])
  const [totalRecords, setTotalRecords] = useState(0)
  const rowsPerPage = 20
  const [first, setFirst] = useState(0)
  const [searchFilter, setSearchFilter] = useState({
    activo: true,
    cliente: true,
    sortFields: [
      {
        fieldName: "razonSocial",
        ascending: true,
      },
    ],
  })
  useEffect(() => {
    onPageEvent({ first: 0 })
  }, [searchFilter])
  const customersService = new CustomersService()

  const fillSearchFilter = (searchCriteria) => {
    setSearchFilter({ ...searchFilter, ...searchCriteria })
  }

  const renderFilterSection = () => {
    return (
      <SearchPersonsFilter
        searchCustomersCallback={fillSearchFilter}
        loading={loading}
      />
    )
  }

  const onPageEvent = (event) => {
    const searchOptions = {
      firstResult: event.first,
      maxResults: rowsPerPage,
      searchFilter: searchFilter,
    }

    customersService.searchCustomers(searchOptions, handleSuccess)

    setLoading(true)
    setFirst(event.first)
  }

  const handleSuccess = (data) => {
    setTotalRecords(data.totalResults)
    setCustomers(data.data)
    setLoading(false)
  }

  const getLinkActions = (rowData) => {
    const { personId } = rowData

    return (
      <Button
        type="button"
        icon="fa fa-fw fa-edit"
        onClick={() => handleEditCustomer(personId)}
      />
    )
  }

  const handleEditCustomer = (customerId) => {
    window.open(`#/customer/${customerId}`, "_blank")
  }

  const renderColumns = () => {
    let columns = CUSTOMER_COLUMNS.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          style={col.style}
          body={(rowData) => _.get(rowData, col.field)}
        />
      )
    })

    columns.push(<Column body={getLinkActions} />)

    return columns
  }

  const renderSearchResults = () => {
    return (
      <DataTable
        value={customers}
        dataKey={"personId"}
        paginator={true}
        rows={rowsPerPage}
        totalRecords={totalRecords}
        lazy={true}
        first={first}
        onPage={onPageEvent}
        loading={loading}
        loadingIcon="fa fa-fw fa-spin fa-spinner"
        resizableColumns
      >
        {renderColumns()}
      </DataTable>
    )
  }

  return (
    <div className="card card-w-title">
      <h1>Mayor de clientes</h1>
      {renderFilterSection()}
      <Button
        type="button"
        label={"Nuevo"}
        icon="fa fa-fw fa-plus"
        onClick={() => window.open(`#/customer/`, "_blank")}
      />
      {renderSearchResults()}
    </div>
  )
}
