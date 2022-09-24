import React from "react"
import { SplitButton } from "primereact/splitbutton"
import PropTypes from "prop-types"
import FileOutputsService from "../../service/FileOutputsService"

export const BudgetPrintSplitButton = (props) => {
  const printDefaultFormat = () => {
    FileOutputsService.getSaleBudget(props.saleId)
  }

  const items = [
    {
      label: "Ticket",
      command: (e) => {
        FileOutputsService.getSaleBudgetTicket(props.saleId)
      },
    },
    {
      label: "A4",
      command: (e) => {
        FileOutputsService.getSaleBudgetA4(props.saleId)
      },
    },
  ]

  return (
    <SplitButton
      type="button"
      icon="fa fa-fw fa-print"
      onClick={printDefaultFormat}
      label={props.saleId}
      model={items}
    />
  )
}

BudgetPrintSplitButton.propTypes = {
  saleId: PropTypes.string.isRequired,
}
