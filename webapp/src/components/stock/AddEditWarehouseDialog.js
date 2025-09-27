import React, { useState } from "react"
import { Dialog } from "primereact/dialog"
import { Button } from "primereact/button"
import { InputText } from "primereact/inputtext"
import { InputSwitch } from "primereact/inputswitch"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"

const validationSchema = {
  type: "object",
  properties: {
    warehouseName: { type: "string" },
    branchId: { type: "number" },
    countryId: { type: "number" },
    provinceId: { type: "number" },
    localityId: { type: "number" },
    address: { type: "string" },
    active: { type: "boolean" },
  },
  required: [
    "warehouseName",
    "branchId",
    "countryId",
    "provinceId",
    "localityId",
    "active",
  ],
}

export const AddEditWarehouseDialog = (props) => {
  const [warehouse, setWarehouse] = useState(
    props.warehouse || {
      warehouseName: "",
      address: "",
      active: true,
      branchId: null,
      countryId: null,
      provinceId: null,
      localityId: null,
    }
  )

  const renderContent = () => {
    return (
      <div>
        <div className="p-grid p-fluid">
          <Form
            data={warehouse}
            onChange={(newData) => setWarehouse(newData)}
            schema={validationSchema}
            onSubmit={handleAcceptButton}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="p-col-12">
              <label htmlFor="warehouseName">Nombre depósito:</label>
              <Field
                id="warehouseName"
                component={InputText}
                name="warehouseName"
                value={warehouse.warehouseName}
              />
              <FieldError name="warehouseName" />
            </div>

            <div className="p-col-12">
              <label htmlFor="address">Dirección:</label>
              <Field
                id="address"
                component={InputText}
                name="address"
                value={warehouse.address}
              />
            </div>

            <div className="p-col-12">
              <label htmlFor="branchId">ID Sucursal:</label>
              <Field
                id="branchId"
                component={InputText}
                name="branchId"
                value={warehouse.branchId}
              />
              <FieldError name="branchId" />
            </div>

            <div className="p-col-12">
              <label htmlFor="countryId">ID País:</label>
              <Field
                id="countryId"
                component={InputText}
                name="countryId"
                value={warehouse.countryId}
              />
              <FieldError name="countryId" />
            </div>

            <div className="p-col-12">
              <label htmlFor="provinceId">ID Provincia:</label>
              <Field
                id="provinceId"
                component={InputText}
                name="provinceId"
                value={warehouse.provinceId}
              />
              <FieldError name="provinceId" />
            </div>

            <div className="p-col-12">
              <label htmlFor="localityId">ID Localidad:</label>
              <Field
                id="localityId"
                component={InputText}
                name="localityId"
                value={warehouse.localityId}
              />
              <FieldError name="localityId" />
            </div>

            <div className="p-col-12">
              <label htmlFor="active" style={{ marginRight: "10px" }}>
                Activo:
              </label>
              <InputSwitch
                id="active"
                checked={warehouse.active}
                onChange={(e) => setWarehouse({ ...warehouse, active: e.value })}
              />
              <FieldError name="active" />
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
      header: "Depósito",
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  const handleAcceptButton = () => {
    props.onHide()

    if (props.acceptCallback) {
      props.acceptCallback({ ...warehouse })
    }
  }

  return <Dialog {...getDialogProps()}>{renderContent()}</Dialog>
}
