import React, { useState } from "react"
import { Dialog } from "primereact/dialog"
import { Button } from "primereact/button"
import { InputText } from "primereact/inputtext"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"

const validationSchema = {
  type: "object",
  properties: {
    categoryName: { type: "string" },
  },
  required: ["categoryName"],
}

export const AddEditCategoryDialog = (props) => {
  const [category, setCategory] = useState(props.category || { categoryName: "" })

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <Form
            data={category}
            onChange={(newPercent) => setCategory(newPercent)}
            schema={validationSchema}
            onSubmit={handleAcceptButton}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="p-col-12">
              <label htmlFor="value">{"Nombre Rubro:"}</label>
              <Field
                id="categoryName"
                component={InputText}
                name="categoryName"
                value={category.categoryName}
              />

              <FieldError name="categoryName" />
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
      header: "Rubro",
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  const handleAcceptButton = () => {
    props.onHide()

    if (props.acceptCallback) {
      props.acceptCallback({ ...category })
    }
  }

  return <Dialog {...getDialogProps()}>{renderContent()}</Dialog>
}
