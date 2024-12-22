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
    subCategoryName: { type: "string" },
  },
  required: ["subCategoryName"],
}

export const AddEditSubCategoryDialog = (props) => {
  const [subCategory, setSubCategory] = useState(
    props.subCategory || { subCategoryName: "" }
  )

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <Form
            data={subCategory}
            onChange={(newPercent) => setSubCategory(newPercent)}
            schema={validationSchema}
            onSubmit={handleAcceptButton}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="p-col-12">
              <label htmlFor="value">{"Nombre Sub-Rubro:"}</label>
              <Field
                id="subCategoryName"
                component={InputText}
                name="subCategoryName"
                value={subCategory.subCategoryName}
              />

              <FieldError name="subCategoryName" />
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
      header: "SubRubro",
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  const handleAcceptButton = () => {
    props.onHide()

    if (props.acceptCallback) {
      props.acceptCallback({ ...subCategory })
    }
  }

  return <Dialog {...getDialogProps()}>{renderContent()}</Dialog>
}
