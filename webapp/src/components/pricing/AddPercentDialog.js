import React, { useState } from "react"
import { Dialog } from "primereact/dialog"
import { Button } from "primereact/button"
import { RadioButton } from "primereact/radiobutton"
import { InputText } from "primereact/inputtext"
import { PercentTypesSelector } from "../core/PercentTypesSelector"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"

const ACTION_TYPE = {
  ADD: "add",
  REMOVE: "remove",
}

const validationSchema = {
  type: "object",
  properties: {
    action: { type: "string" },
    value: { pattern: "^(-)?(?!0\\d)\\d*(\\.\\d{1,4})?$" },
    percentType: { type: "object" },
  },
  required: ["action", "value", "percentType"],
}

export const AddPercentDialog = (props) => {
  const [percent, setPercent] = useState({
    action: ACTION_TYPE.ADD,
    percentType: null,
    value: "",
  })

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <Form
            data={percent}
            onChange={(newPercent) => setPercent(newPercent)}
            schema={validationSchema}
            onSubmit={handleAcceptButton}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="p-col-12">
              <Field
                id="addAction"
                component={RadioButton}
                name="action"
                value={ACTION_TYPE.ADD}
                checked={percent.action === ACTION_TYPE.ADD}
              />

              <label htmlFor="addAction" className="p-radiobutton-label">
                {"Agregar"}
              </label>
            </div>
            <div className="p-col-12">
              <Field
                id="removeAction"
                component={RadioButton}
                name="action"
                value={ACTION_TYPE.REMOVE}
                checked={percent.action === ACTION_TYPE.REMOVE}
              />
              <FieldError name="removeAction" />
              <label htmlFor="removeAction" className="p-radiobutton-label">
                {"Remover"}
              </label>
            </div>

            <div className="p-col-12">
              <label htmlFor="percentType">{"Tipo:"}</label>
              <PercentTypesSelector
                selectedItem={percent.percentType}
                onChange={(percentType) =>
                  handlePropertyChange("percentType", percentType)
                }
              />
              <FieldError name="percentType" />
            </div>

            <div className="p-col-12">
              <label htmlFor="value">{"Valor:"}</label>
              <Field
                id="value"
                component={InputText}
                name="value"
                value={percent.value}
              />

              <FieldError name="value" />
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
      header: "Agregar porcentaje",
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  const handleAcceptButton = () => {
    props.onHide()

    if (props.acceptCallback) {
      props.acceptCallback({ ...percent })
    }
  }

  const handlePropertyChange = (property, value) => {
    let newPercent = { ...percent }

    newPercent[property] = value

    setPercent(newPercent)
  }

  return <Dialog {...getDialogProps()}>{renderContent()}</Dialog>
}
