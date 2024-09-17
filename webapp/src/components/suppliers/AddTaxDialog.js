import React, { useState } from "react"
import { Dialog } from "primereact/dialog"
import { Button } from "primereact/button"
import { InputText } from "primereact/inputtext"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"
import { FiscalTaxRateSelector } from "../core/FiscalTaxRateSelector"

const validationSchema = {
  type: "object",
  properties: {
    taxRate: { type: "object" },
    taxAmount: { pattern: "^(-)?(?!0\\d)\\d*(\\.\\d{1,4})?$" },
  },
  required: ["taxRate", "taxAmount"],
}

export const AddTaxDialog = (props) => {
  const [tax, setTax] = useState({
    taxRate: null,
    taxAmount: "",
  })

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <Form
            data={tax}
            onChange={(newPercent) => setTax(newPercent)}
            schema={validationSchema}
            onSubmit={handleAcceptButton}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="p-col-12">
              <label htmlFor="tax">{"Alícuota:"}</label>
              <FiscalTaxRateSelector
                selectedTaxRate={tax.taxRate}
                onTaxRateSelect={(taxRate) =>
                  handlePropertyChange("taxRate", taxRate)
                }
              />
              <FieldError name="tax" />
            </div>

            <div className="p-col-12">
              <label htmlFor="value">{"Importe alícuota:"}</label>
              <Field
                id="taxAmount"
                component={InputText}
                name="taxAmount"
                value={tax.taxAmount}
              />

              <FieldError name="taxAmount" />
            </div>

            {renderFooter()}
          </Form>
        </div>
      </div>
    )
  }

  const renderFooter = () => {
    return (
      <div className="p-col-6">
        <Button
          label="Aceptar"
          icon="fa fa-fw fa-check"
          type={"submit"}
          className="p-button-success"
        />
      </div>
    )
  }

  const getDialogProps = () => {
    let defaultProps = {
      header: "Agregar alícuota",
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  const handleAcceptButton = () => {
    props.onHide()

    if (props.acceptCallback) {
      props.acceptCallback({ ...tax })
    }
  }

  const handlePropertyChange = (property, value) => {
    let newPercent = { ...tax }

    newPercent[property] = value

    setTax(newPercent)
  }

  return <Dialog {...getDialogProps()}>{renderContent()}</Dialog>
}
