import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { ProductsService } from "../../service/ProductsService"

export const ProductSupplyTypeSelector = ({
  onSupplyTypeSelect,
  selectedSupplyType,
}) => {
  const [currentSupplyType, setCurrentSupplyType] = useState(selectedSupplyType)
  const [supplyTypes, setSupplyTypes] = useState([])
  const service = new ProductsService()

  useEffect(() => service.getSupplyTypes(handleGetSupplyTypes), [])
  useEffect(() => setCurrentSupplyType(selectedSupplyType), [selectedSupplyType])

  const handleGetSupplyTypes = (supplyTypes) => {
    setSupplyTypes(supplyTypes)
  }

  const handleSelectionChange = (value) => {
    setCurrentSupplyType(value)

    if (onSupplyTypeSelect) {
      onSupplyTypeSelect(value)
    }
  }

  return (
    <Dropdown
      id="supplyType"
      placeholder={"Tipo de proveeduria"}
      filter={true}
      dataKey="supplyTypeId"
      options={supplyTypes}
      showClear={true}
      value={currentSupplyType}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
