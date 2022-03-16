import React , {useState} from "react"
import { Button } from "primereact/button"
import PropTypes from "prop-types"
import {CustomersService} from "../../service/CustomersService";
import {SearchPersonsFilter} from "../core/SearchPersonsFilter";

export const CustomersInventory = () => {

  const [loading, setLoading] = useState(false)
  const [customers, setCustomers] = useState([])
  const customersService = new CustomersService()

  const searchCustomers = () => {
    customersService.searchCustomers({},null)
  }

  const renderFilterSection = () => {
    return (
      <SearchPersonsFilter
        searchProductsCallback={searchCustomers}
        loading={this.state.loading}
      />
    )
  }

  const renderSearchResults = () => {

  }

  return (
    <div className="card card-w-title">
      <h1>Mayor de clientes</h1>
      {renderFilterSection()}
      {renderSearchResults()}
    </div>
  )
}