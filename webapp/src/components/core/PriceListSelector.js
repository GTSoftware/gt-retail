import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { ProductsService } from "../../service/ProductsService"

export const PriceListSelector = ({ onChange, selectedItem }) => {
  const [currentPriceList, setCurrentPriceList] = useState(selectedItem)
  const [priceLists, setPriceLists] = useState([])
  const service = new ProductsService()

  useEffect(() => service.getPriceLists(handlePriceLists), [])
  useEffect(() => setCurrentPriceList(selectedItem), [selectedItem])

  const handlePriceLists = (values) => {
    setPriceLists(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentPriceList(value)

    if (onChange) {
      onChange(value)
    }
  }

  return (
    <Dropdown
      id="priceList"
      placeholder={"Lista de precios"}
      filter={true}
      dataKey="priceListId"
      options={priceLists}
      showClear={true}
      value={currentPriceList}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
