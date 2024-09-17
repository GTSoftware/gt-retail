import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import ShopCartService from "../../service/ShopCartService"

export const SaleTypeSelector = ({ onSaleTypeSelect, selectedSaleType }) => {
  const [currentSaleType, setCurrentSaleType] = useState(selectedSaleType)
  const [saleTypes, setSaleTypes] = useState([])
  const service = ShopCartService

  useEffect(() => service.getSaleTypes(handleSaleTypes), [])
  useEffect(() => setCurrentSaleType(selectedSaleType), [selectedSaleType])

  const handleSaleTypes = (values) => {
    setSaleTypes(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentSaleType(value)

    if (onSaleTypeSelect) {
      onSaleTypeSelect(value)
    }
  }

  return (
    <Dropdown
      id="category"
      placeholder={"Tipo de comprobante"}
      filter={true}
      dataKey="id"
      options={saleTypes}
      showClear={true}
      value={currentSaleType}
      optionLabel="nombreComprobante"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
