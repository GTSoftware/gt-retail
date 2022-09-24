import React from "react"
import { SplitButton } from "primereact/splitbutton"
import PropTypes from "prop-types"
import FileOutputsService from "../../service/FileOutputsService"

export const InvoicePrintSplitButton = (props) => {
  const printDefaultFormat = () => {
    FileOutputsService.getInvoice(props.saleId)
  }

  const items = [
    {
      label: "Ticket",
      command: (e) => {
        FileOutputsService.getInvoiceTicket(props.saleId)
      },
    },
    {
      label: "A4",
      command: (e) => {
        FileOutputsService.getInvoiceA4(props.saleId)
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

InvoicePrintSplitButton.propTypes = {
  saleId: PropTypes.string.isRequired,
}
