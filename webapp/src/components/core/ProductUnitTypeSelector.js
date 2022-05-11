import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { ProductsService } from "../../service/ProductsService"

export const ProductUnitTypeSelector = ({ onSelectUnitType, selectedUnitType }) => {
  const [currentUnitType, setCurrentUnitType] = useState(selectedUnitType)
  const [unitTypes, setUnitTypes] = useState([])
  const service = new ProductsService()

  useEffect(() => service.getUnitTypes(handleUnitTypes), [])
  useEffect(() => setCurrentUnitType(selectedUnitType), [selectedUnitType])

  const handleUnitTypes = (values) => {
    setUnitTypes(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentUnitType(value)

    if (onSelectUnitType) {
      onSelectUnitType(value)
    }
  }
  return (
    <Dropdown
      id="unitType"
      placeholder={""}
      filter={true}
      dataKey="unitTypeId"
      options={unitTypes}
      showClear={true}
      value={currentUnitType}
      optionLabel="unitName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
