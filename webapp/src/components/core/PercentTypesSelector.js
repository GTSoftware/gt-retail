import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { ProductsService } from "../../service/ProductsService"

export const PercentTypesSelector = ({ onChange, selectedItem }) => {
  const [currentPercentType, setCurrentPercentType] = useState(selectedItem)
  const [percentTypes, setPercentTypes] = useState([])
  const service = new ProductsService()

  useEffect(() => service.getPercentTypes(handlePercentTypes), [])
  useEffect(() => setCurrentPercentType(selectedItem), [selectedItem])

  const handlePercentTypes = (values) => {
    setPercentTypes(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentPercentType(value)

    if (onChange) {
      onChange(value)
    }
  }

  return (
    <Dropdown
      id="percentType"
      placeholder={"Tipo de porcentaje"}
      filter={true}
      dataKey="percentTypeId"
      options={percentTypes}
      showClear={true}
      value={currentPercentType}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
