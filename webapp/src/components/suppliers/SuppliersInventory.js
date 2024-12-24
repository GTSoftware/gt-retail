import React, { useEffect, useState } from "react"
import { Button } from "primereact/button"
import { SuppliersService } from "../../service/SuppliersService"
import { SearchPersonsFilter } from "../core/SearchPersonsFilter"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"

const CUSTOMER_COLUMNS = [
  { field: "personId", header: "Id" },
  { field: "businessName", header: "Nombre" },
  { field: "identification", header: "Identificación" },
  { field: "fantasyName", header: "Nombre Fantasía" },
  { field: "email", header: "e-mail" },
  { field: "address", header: "Dirección" },
]

export const SuppliersInventory = () => {
  const [loading, setLoading] = useState(false)
  const [suppliers, setSuppliers] = useState([])
  const [totalRecords, setTotalRecords] = useState(0)
  const rowsPerPage = 20
  const [first, setFirst] = useState(0)
  const [searchFilter, setSearchFilter] = useState({
    activo: true,
    proveedor: true,
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
  const suppliersService = new SuppliersService()

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

    setLoading(true)
    setFirst(event.first)

    suppliersService
      .searchSuppliersPaginated(searchOptions)
      .then((response) => handleSuccess(response))
      .catch((err) => {
        setLoading(false)
      })
  }

  const handleSuccess = (data) => {
    setTotalRecords(data.totalResults)
    setSuppliers(data.data)
    setLoading(false)
  }

  const getLinkActions = (rowData) => {
    const { personId } = rowData

    return (
      <Button
        type="button"
        icon="fa fa-fw fa-edit"
        onClick={() => handleEditSupplier(personId)}
      />
    )
  }

  const handleEditSupplier = (personId) => {
    window.open(`#/supplier/${personId}`, "_blank")
  }

  const renderColumns = () => {
    let columns = CUSTOMER_COLUMNS.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          style={col.style}
          body={(rowData) => rowData[col.field]}
        />
      )
    })

    columns.push(<Column body={getLinkActions} />)

    return columns
  }

  const renderSearchResults = () => {
    return (
      <DataTable
        value={suppliers}
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
      <h1>Mayor de proveedores</h1>
      {renderFilterSection()}
      <Button
        type="button"
        label={"Nuevo"}
        icon="fa fa-fw fa-plus"
        onClick={() => window.open(`#/supplier/`, "_blank")}
      />
      {renderSearchResults()}
    </div>
  )
}
