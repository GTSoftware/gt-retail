import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { BrandsService } from "../../service/BrandsService"

export const BrandsSelector = ({ selectedBrand, onBrandSelect }) => {
  const [currentBrand, setCurrentBrand] = useState(selectedBrand)
  const [brands, setBrands] = useState([])
  const service = new BrandsService()

  useEffect(() => service.getBrands(handleGetBrands), [])
  useEffect(() => setCurrentBrand(selectedBrand), [selectedBrand])

  const handleGetBrands = (brands) => {
    setBrands(brands)
  }

  const handleSelectionChange = (value) => {
    setCurrentBrand(value)
    if (onBrandSelect) {
      onBrandSelect(value)
    }
  }

  return (
    <Dropdown
      id="brand"
      placeholder={"Marca"}
      filter={true}
      dataKey="brandId"
      options={brands}
      showClear={true}
      value={currentBrand}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
