import React, { Component } from "react"
import { AutoComplete } from "primereact/autocomplete"
import PropTypes from "prop-types"
import { SuppliersService } from "../../service/SuppliersService"

export class AutocompleteSupplierFilter extends Component {
  static propTypes = {
    onSupplierSelect: PropTypes.func.isRequired,
  }

  constructor(props, context) {
    super(props, context)

    this.state = {
      filteredSuppliers: [],
      selectedSupplier: null,
    }

    this.service = new SuppliersService()
  }

  render() {
    const { selectedSupplier, filteredSuppliers } = this.state

    return (
      <AutoComplete
        id="supplier"
        minLength={2}
        placeholder="Comience a escribir para buscar un proveedor"
        delay={500}
        completeMethod={(event) => this.filterSuppliers(event.query)}
        suggestions={filteredSuppliers}
        field="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
        value={selectedSupplier || ""}
      />
    )
  }

  filterSuppliers = (query) => {
    this.service.searchSuppliers(query, (suppliers) =>
      this.setState({ filteredSuppliers: suppliers })
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedSupplier: value,
    })

    this.props.onSupplierSelect(value)
  }
}
