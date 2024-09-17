import _ from "lodash"
import React, { useRef, useState } from "react"
import { Toast } from "primereact/toast"
import { Column } from "primereact/column"
import { DataTable } from "primereact/datatable"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { Button } from "primereact/button"
import { LoadingButton } from "../core/LoadingButton"
import {
  formatDate,
  getBeginOfToday,
  getEndOfToday,
  serializeDate,
} from "../../utils/DateUtils"
import { Calendar } from "primereact/calendar"

import { AutocompleteSupplierFilter } from "../core/AutocompleteSupplierFilter"
import { SuppliersService } from "../../service/SuppliersService"

import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"
import { InputText } from "primereact/inputtext"
import { InputTextarea } from "primereact/inputtextarea"
import { FiscalPeriodSelector } from "../core/FiscalPeriodSelector"
import { SaleTypeSelector } from "../core/SaleTypeSelector"
import { AddTaxDialog } from "./AddTaxDialog"
import { v4 as uuid } from "uuid"

const invoiceSchema = {
  type: "object",
  properties: {
    notes: { type: "string", maxLength: 2048 },
    supplier: { type: "object" },
    invoiceDate: { type: "object" },
    fiscalPeriod: { type: "object" },
    invoiceType: { type: "object" },
    letter: { type: "string", maxLength: 1, pattern: "^[ABCMabcm]+$" },
    pointOfSale: { type: "string", maxLength: 5, pattern: "^\\d*$" },
    invoiceNumber: { type: "string", pattern: "^\\d*$" },
    invoiceDetails: { type: "array" },
    grossIncomePerceptionAmount: { pattern: "^(?!0\\d)\\d*(\\.\\d{1,2})?$" },
    taxPerceptionAmount: { pattern: "^(?!0\\d)\\d*(\\.\\d{1,2})?$" },
  },
  required: [
    "supplier",
    "fiscalPeriod",
    "invoiceDate",
    "invoiceType",
    "letter",
    "pointOfSale",
    "invoiceNumber",
    "invoiceDetails",
    "taxPerceptionAmount",
    "grossIncomePerceptionAmount",
  ],
}

export const NewSupplierInvoice = () => {
  const [loading, setLoading] = useState(false)
  const toast = useRef()
  const [formData, setFormData] = useState({
    invoiceDetails: [],
    invoiceDate: getBeginOfToday(),
    grossIncomePerceptionAmount: 0,
    taxPerceptionAmount: 0,
  })
  const [showAddTaxDialog, setShowAddTaxDialog] = useState(false)
  const supplierService = new SuppliersService()

  const renderTopSection = () => {
    const {
      fiscalPeriod,
      supplier,
      invoiceDate,
      invoiceType,
      letter,
      pointOfSale,
      invoiceNumber,
    } = formData

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Proveedor:"}</div>
        <div className="p-col-9">
          <AutocompleteSupplierFilter
            selectedSupplier={supplier}
            onSupplierSelect={(supplier) =>
              handlePropertyChange("supplier", supplier)
            }
          />
          <FieldError name="supplier" />
        </div>
        <div className="p-col-3">{"Fecha de comprobante:"}</div>
        <div className="p-col-9">
          <Field
            id="invoiceDate"
            component={Calendar}
            name="invoiceDate"
            value={invoiceDate}
          />
          <FieldError name="invoiceDate" />
        </div>

        <div className="p-col-3">{"Tipo de comprobante:"}</div>
        <div className="p-col-9">
          <SaleTypeSelector
            onSaleTypeSelect={(it) => handlePropertyChange("invoiceType", it)}
            selectedSaleType={invoiceType}
          />
          <FieldError name="description" />
        </div>

        <div className="p-col-3">{"Letra de factura:"}</div>
        <div className="p-col-9">
          <Field id="letter" component={InputText} name="letter" value={letter} />
          <FieldError name="letter" />
        </div>

        <div className="p-col-3">{"Punto de venta:"}</div>
        <div className="p-col-9">
          <Field
            id="pointOfSale"
            component={InputText}
            name="pointOfSale"
            value={pointOfSale}
          />
          <FieldError name="pointOfSale" />
        </div>

        <div className="p-col-3">{"Número:"}</div>
        <div className="p-col-9">
          <Field
            id="invoiceNumber"
            component={InputText}
            name="invoiceNumber"
            value={invoiceNumber}
          />
          <FieldError name="invoiceNumber" />
        </div>

        <div className="p-col-3">{"Período fiscal:"}</div>
        <div className="p-col-9">
          <FiscalPeriodSelector
            value={fiscalPeriod}
            onChange={(fp) => handlePropertyChange("fiscalPeriod", fp)}
          />
          <FieldError name="fiscalPeriod" />
        </div>
      </div>
    )
  }

  const renderTaxesTable = () => {
    return (
      <div>
        <DataTable {...getItemsTableProps()}>
          <Column
            header={"Alícuota"}
            body={(rowData) => {
              return rowData.taxRate.displayName
            }}
          />
          <Column header={"Importe IVA"} field={"taxAmount"} />
          <Column header={"Neto gravado"} field={"baseAmount"} />
          <Column header={"No Gravado"} field={"nonTaxableAmount"} />
        </DataTable>
      </div>
    )
  }

  const getItemsTableProps = () => {
    const { invoiceDetails } = formData
    const header = <div className="p-clearfix">Alícuotas del comprobante</div>
    const footer = (
      <div className="p-clearfix">
        <label>Alícuotas: {invoiceDetails?.length}</label>
      </div>
    )

    return {
      ...DEFAULT_DATA_TABLE_PROPS,
      ...{
        value: invoiceDetails,
        header: header,
        footer: footer,
        lazy: false,
        resizableColumns: true,
        emptyMessage: "Agregue alícuotas al comprobante",
      },
    }
  }

  const handlePropertyChange = (property, value) => {
    let invoice = { ...formData }

    invoice[property] = value

    handleChange(invoice)
  }

  const handleChange = (newData) => {
    setFormData(newData)
  }

  const handleSubmit = () => {
    setLoading(true)
    supplierService.storeInvoice(formData, handleSuccess, handleError)
  }

  const handleSuccess = (createdInvoice) => {
    setLoading(false)

    toast.current.show({
      severity: "success",
      summary: `Comprobante guardado con éxito: ${createdInvoice.invoiceId}`,
    })
  }

  const handleError = (error) => {
    let detail = "Error desconocido"

    setLoading(false)

    if (_.get(error, "errorCode") === "400999") {
      detail = error.fieldErrors.map((errorField) => {
        return errorField + "\n"
      })
    }

    toast.current.show({
      severity: "error",
      summary: "No se pudo guardar el comprobante",
      detail: detail,
      sticky: true,
    })
  }

  const handleAddTax = (newTax) => {
    let baseAmount = 0
    let nonTaxableAmount = 0
    let taxAmount = +parseFloat(newTax.taxAmount).toFixed(2)
    if (newTax.taxRate.taxable) {
      baseAmount = +(taxAmount / (newTax.taxRate.rate / 100)).toFixed(2)
      nonTaxableAmount = 0
    } else {
      nonTaxableAmount = taxAmount
      baseAmount = 0
      taxAmount = 0
    }

    let newInvoiceDetails = {
      item: uuid(),
      taxRate: newTax.taxRate,
      taxAmount: taxAmount,
      baseAmount,
      nonTaxableAmount,
    }

    let newFormData = { ...formData }
    newFormData.invoiceDetails.push(newInvoiceDetails)
    handleChange(newFormData)
  }
  const renderShowAddTaxDialog = () => {
    return (
      <AddTaxDialog
        visible={showAddTaxDialog}
        modal={true}
        acceptCallback={handleAddTax}
        onHide={() => setShowAddTaxDialog(false)}
      />
    )
  }

  const renderInvoiceTotal = () => {
    return (
      <div className="p-grid p-card-body">
        <div className="p-col-3">{"Total de comprobante:"}</div>
        <div className="p-col-9">
          <label style={{ fontSize: "20px" }}>${getInvoiceTotal()}</label>
        </div>
      </div>
    )
  }

  const getInvoiceTotal = () => {
    const { invoiceDetails, grossIncomePerceptionAmount, taxPerceptionAmount } =
      formData

    let total = 0

    if (invoiceDetails && invoiceDetails.length > 0) {
      invoiceDetails.map(
        (invoiceLine) =>
          (total +=
            invoiceLine.taxAmount +
            invoiceLine.baseAmount +
            invoiceLine.nonTaxableAmount)
      )
    }
    total += +grossIncomePerceptionAmount || 0
    total += +taxPerceptionAmount || 0

    return +total.toFixed(2)
  }

  const renderPerceptionAmounts = () => {
    const { grossIncomePerceptionAmount, taxPerceptionAmount } = formData

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Percepción de ingresos brutos:"}</div>
        <div className="p-col-9">
          <Field
            id="grossIncomePerceptionAmount"
            component={InputText}
            name="grossIncomePerceptionAmount"
            value={grossIncomePerceptionAmount}
          />
          <FieldError name="grossIncomePerceptionAmount" />
        </div>

        <div className="p-col-3">{"Percepción de IVA:"}</div>
        <div className="p-col-9">
          <Field
            id="taxPerceptionAmount"
            component={InputText}
            name="taxPerceptionAmount"
            value={taxPerceptionAmount}
          />
          <FieldError name="taxPerceptionAmount" />
        </div>
      </div>
    )
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Nuevo comprobante de proveedor</h1>
      {showAddTaxDialog && renderShowAddTaxDialog()}
      <Form
        data={formData}
        schema={invoiceSchema}
        errorMessages={{
          required: () => fieldRequiredDefaultMessage,
          pattern: () => invalidPatternMessage,
        }}
        onChange={handleChange}
        onSubmit={handleSubmit}
      >
        {renderTopSection()}

        <Button
          label="Agregar alícuota"
          type="button"
          onClick={() => setShowAddTaxDialog(true)}
          icon="fa fa-fw fa-plus"
        />

        {renderTaxesTable()}

        {renderPerceptionAmounts()}

        {renderInvoiceTotal()}

        <div className="p-card-body p-fluid p-grid">
          <div className="p-col-3">{"Notas:"}</div>
          <div className="p-col-9">
            <Field
              id="notes"
              component={InputTextarea}
              name="notes"
              value={formData.notes}
            />
            <FieldError name="notes" />
          </div>
        </div>

        <LoadingButton
          type="submit"
          label="Guardar"
          loading={loading}
          icon="fa fa-fw fa-save"
        />
      </Form>
    </div>
  )
}
