import _ from "lodash"
import React, { useRef, useState } from "react"
import { Toast } from "primereact/toast"
import { Column } from "primereact/column"
import { DataTable } from "primereact/datatable"
import { DEFAULT_DATA_TABLE_PROPS } from "../DefaultProps"
import { Button } from "primereact/button"
import FileOutputsService from "../../service/FileOutputsService"
import { LoadingButton } from "../core/LoadingButton"
import {
  formatDate,
  getBeginOfToday,
  getEndOfToday,
  serializeDate,
} from "../../utils/DateUtils"
import { Calendar } from "primereact/calendar"
import { SalesService } from "../../service/SalesService"
import { TabPanel, TabView } from "primereact/tabview"
import ShopCartService from "../../service/ShopCartService"
import { MultiSelect } from "primereact/multiselect"
import { AutoComplete } from "primereact/autocomplete"
import { TriStateCheckbox } from "primereact/tristatecheckbox"
import LoginService from "../../service/LoginService"

import { InvoicePrintSplitButton } from "../core/InvoicePrintSplitButton"
import { BudgetPrintSplitButton } from "../core/BudgetPrintSplitButton"
import { AutocompleteSupplierFilter } from "../core/AutocompleteSupplierFilter"
import { SuppliersService } from "../../service/SuppliersService"
import { exportExcel } from "../../utils/ExcelExporter"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"
import { InputText } from "primereact/inputtext"
import { InputTextarea } from "primereact/inputtextarea"
import { Checkbox } from "primereact/checkbox"
import { Dropdown } from "primereact/dropdown"

const invoiceSchema = {
  type: "object",
  properties: {
    notes: { type: "string", maxLength: 2048 },
    supplier: { type: "object" },
  },
  required: ["supplier"],
}

export const NewSupplierInvoice = () => {
  const [loading, setLoading] = useState(false)
  const [selectedSupplier, setSelectedSupplier] = useState(null)
  const toast = useRef()
  const suppliersService = new SuppliersService()
  const [formData, setFormData] = useState()

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Nuevo comprobante de proveedor</h1>
      <Form
        data={formData}
        schema={invoiceSchema}
        errorMessages={{
          required: () => fieldRequiredDefaultMessage,
          pattern: () => invalidPatternMessage,
        }}
      >
        <div className="p-card-body p-fluid p-grid">
          <div className="p-col-3">{"Proveedor:"}</div>
          <div className="p-col-9">
            <Field
              id="code"
              component={AutocompleteSupplierFilter}
              name="code"
              value={"code"}
            />
            <FieldError name="code" />
          </div>
          <div className="p-col-3">{"Fecha de comprobante:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={Calendar}
              name="description"
              value={"desc"}
            />
            <FieldError name="description" />
          </div>

          <div className="p-col-3">{"Período fiscal:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={Dropdown}
              name="description"
              value={"desc"}
            />
            <FieldError name="description" />
          </div>

          <div className="p-col-3">{"Tipo de comprobante:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={Dropdown}
              name="description"
              value={"desc"}
            />
            <FieldError name="description" />
          </div>

          <div className="p-col-3">{"Letra de factura:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={InputText}
              name="description"
              value={"desc"}
            />
            <FieldError name="description" />
          </div>

          <div className="p-col-3">{"Punto de venta:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={InputText}
              name="description"
              value={"desc"}
            />
            <FieldError name="description" />
          </div>

          <div className="p-col-3">{"Número:"}</div>
          <div className="p-col-9">
            <Field
              id="description"
              component={InputText}
              name="description"
              value={"numero"}
            />
            <FieldError name="description" />
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
