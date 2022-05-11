import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { FiscalTaxRateService } from "../../service/FiscalTaxRateService"

export const FiscalTaxRateSelector = ({ onTaxRateSelect, selectedTaxRate }) => {
  const [currentTaxRate, setCurrentTaxRate] = useState(selectedTaxRate)
  const [taxRates, setTaxRates] = useState([])
  const service = new FiscalTaxRateService()

  useEffect(() => service.getTaxRates(handleTaxRates), [])
  useEffect(() => setCurrentTaxRate(selectedTaxRate), [selectedTaxRate])

  const handleTaxRates = (brands) => {
    setTaxRates(brands)
  }

  const handleSelectionChange = (value) => {
    setCurrentTaxRate(value)

    if (onTaxRateSelect) {
      onTaxRateSelect(value)
    }
  }

  return (
    <Dropdown
      id="taxRate"
      placeholder={"Tasa IVA"}
      filter={true}
      dataKey="taxRateId"
      options={taxRates}
      showClear={true}
      value={currentTaxRate}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
