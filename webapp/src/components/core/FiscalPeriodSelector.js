import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { FiscalBookService } from "../../service/FiscalBookService"

export const FiscalPeriodSelector = ({ onChange, value }) => {
  const [currentFiscalPeriod, setCurrentFiscalPeriod] = useState(value)
  const [fiscalPeriods, setFiscalPeriods] = useState([])
  const service = new FiscalBookService()

  useEffect(() => service.getFiscalPeriods(handleFiscalPeriods), [])
  useEffect(() => setCurrentFiscalPeriod(value), [value])

  const handleFiscalPeriods = (response) => {
    setFiscalPeriods(response?.data)
  }

  const handleSelectionChange = (event) => {
    setCurrentFiscalPeriod(event.value)
    if (onChange) {
      onChange(event.value)
    }
  }

  return (
    <Dropdown
      id="fiscalPeriod"
      optionLabel="nombrePeriodo"
      dataKey={"id"}
      filter={true}
      placeholder="Seleccione un periodo"
      options={fiscalPeriods}
      value={currentFiscalPeriod}
      onChange={handleSelectionChange}
    />
  )
}
