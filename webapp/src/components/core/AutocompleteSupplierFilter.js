import React, { useEffect, useState } from "react"
import { AutoComplete } from "primereact/autocomplete"
import { SuppliersService } from "../../service/SuppliersService"

export const AutocompleteSupplierFilter = ({
  onSupplierSelect,
  selectedSupplier,
}) => {
  const [currentSupplier, setCurrentSupplier] = useState(selectedSupplier)
  const [filteredSuppliers, setFilteredSuppliers] = useState([])
  const service = new SuppliersService()

  useEffect(() => setCurrentSupplier(selectedSupplier), [selectedSupplier])

  const handleSelectionChange = (value) => {
    setCurrentSupplier(value)

    if (onSupplierSelect) {
      onSupplierSelect(value)
    }
  }

  const filterSuppliers = (query) => {
    service.searchSuppliers(query).then((suppliers) => {
      setFilteredSuppliers(suppliers.data)
    })
  }

  return (
    <AutoComplete
      id="supplier"
      minLength={2}
      placeholder="Comience a escribir para buscar un proveedor"
      delay={500}
      completeMethod={(event) => filterSuppliers(event.query)}
      suggestions={filteredSuppliers}
      field="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
      value={currentSupplier || ""}
    />
  )
}
