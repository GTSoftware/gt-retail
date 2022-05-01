import _ from "lodash"
import React, { useEffect, useRef, useState } from "react"
import { FieldError, Form } from "react-jsonschema-form-validation"
import { CustomersService } from "../../service/CustomersService"
import { InputText } from "primereact/inputtext"
import Field from "react-jsonschema-form-validation/dist/Field"
import {
  emailInvalidFormatMessage,
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { Dropdown } from "primereact/dropdown"
import { LoadingButton } from "../core/LoadingButton"
import { Button } from "primereact/button"
import { Toast } from "primereact/toast"
import { Checkbox } from "primereact/checkbox"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"

const newCustomerSchema = {
  type: "object",
  properties: {
    email: { type: "string", format: "email" },
    razonSocial: {
      type: "string",
      minLength: 1,
      maxLength: 200,
      pattern: "^[a-zA-ZñÑ,.. ]*$",
    },
    apellidos: {
      type: "string",
      minLength: 1,
      maxLength: 60,
      pattern: "^[a-zA-ZñÑ. ]*$",
    },
    nombres: {
      type: "string",
      minLength: 1,
      maxLength: 60,
      pattern: "^[a-zA-ZñÑ. ]*$",
    },
    nombreFantasia: { type: "string", maxLength: 200 },
    calle: { type: "string", maxLength: 100, pattern: "^[-a-zA-Z0-9.. ]*$" },
    altura: { type: "string", maxLength: 50, pattern: "^[-0-9]*$" },
    piso: { type: "string", maxLength: 3, pattern: "^[-0-9]*$" },
    depto: { type: "string", maxLength: 5, pattern: "^[-a-zA-Z0-9]*$" },
    documento: { type: "string", minLength: 7, maxLength: 13, pattern: "^[0-9]*$" },
    tipoPersoneria: { type: "object" },
    genero: { type: "object" },
    tipoDocumento: { type: "object" },
    responsabilidadIva: { type: "object" },
    pais: { type: "object" },
    provincia: { type: "object" },
    localidad: { type: "object" },
    activo: { type: "boolean" },
    telefonos: {
      type: "array",
      items: {
        type: "object",
        properties: {
          areaCode: { type: "string", minLength: 3, maxLength: 5 },
          number: { type: "string", minLength: 6, maxLength: 9 },
          reference: { type: "string" },
        },
        required: ["areaCode", "number"],
      },
    },
  },
  required: [
    "documento",
    "tipoPersoneria",
    "genero",
    "tipoDocumento",
    "calle",
    "altura",
    "piso",
    "depto",
    "responsabilidadIva",
    "pais",
    "provincia",
    "localidad",
  ],
}

export const CustomerDetails = (props) => {
  const customersService = new CustomersService()

  const [loading, setLoading] = useState(false)
  const [saveDisabled, setSaveDisabled] = useState(false)

  const [legalStatusTypes, setLegalStatusTypes] = useState([])
  const [genders, setGenders] = useState([])
  const [responsabilidadesIva, setResponsabilidadesIva] = useState([])
  const [countries, setCountries] = useState([])
  const [provinces, setProvinces] = useState([])
  const [towns, setTowns] = useState([])
  const [identificationTypes, setIdentificationTypes] = useState([])

  const [formData, setFormData] = useState({
    email: "",
    documento: "",
    tipoPersoneria: null,
    genero: null,
    tipoDocumento: null,
    razonSocial: "",
    apellidos: "",
    nombres: "",
    nombreFantasia: "",
    calle: "-",
    altura: "-",
    depto: "-",
    piso: "-",
    responsabilidadIva: null,
    pais: null,
    provincia: null,
    localidad: null,
    activo: true,
    telefonos: [],
  })

  const [customerId, setCustomerId] = useState(props.match.params.customerId)
  const [editingCustomer, setEditingCustomer] = useState(null)

  const toast = useRef(null)

  useEffect(() => {
    customersService.getLegalStatusTypes((data) => {
      setLegalStatusTypes(data)
    })
    customersService.getResponsabilidadesIva(setResponsabilidadesIva)
    customersService.getCountries(setCountries)
  }, [])

  useEffect(() => {
    customersService.getCustomer(customerId, loadCustomer)
  }, [customerId])

  useEffect(() => {
    const { tipoPersoneria } = formData

    if (tipoPersoneria) {
      customersService.getGenders(tipoPersoneria.id, (data) => {
        setGenders(data)
        //setGenero(data[0])
      })
      customersService.getIdentificationTypes(tipoPersoneria.id, (data) => {
        setIdentificationTypes(data)
        //setTipoDocumento(data[0])
      })
    }
  }, [formData?.tipoPersoneria])
  useEffect(() => {
    if (formData.pais) {
      //setProvincia(null)
      //setLocalidad(null)
      setProvinces([])
      setTowns([])
      customersService.getProvinces(formData.pais.id, setProvinces)
    }
  }, [formData?.pais])
  useEffect(() => {
    if (formData.provincia) {
      //setLocalidad(null)
      setTowns([])
      customersService.getTowns(formData.provincia.id, "", setTowns)
    }
  }, [formData?.provincia])

  const loadCustomer = (customer) => {
    setEditingCustomer(customer)

    const customerData = {
      customerId: customer.customerId,
      email: customer.email,
      nombres: customer.nombres,
      apellidos: customer.apellidos,
      razonSocial: customer.razonSocial,
      version: customer.version,
      documento: customer.documento,
      calle: customer.calle,
      altura: customer.altura,
      piso: customer.piso,
      depto: customer.depto,
      pais: customer.pais,
      provincia: customer.provincia,
      localidad: customer.localidad,
      genero: customer.genero,
      tipoPersoneria: customer.tipoPersoneria,
      tipoDocumento: customer.tipoDocumento,
      nombreFantasia: customer.fantasyName || "",
      responsabilidadIva: customer.tipoResponsableIva,
      activo: customer.activo,
      telefonos: transformPhones(customer.phones),
    }
    setFormData({ ...formData, ...customerData })
  }

  const transformPhones = (phones) => {
    if (phones) {
      return phones.map((phone) => {
        const areaCode = phone.phoneNumber.split(" ")[0]
        const number = phone.phoneNumber.split(" ")[1]
        return {
          areaCode: areaCode,
          number: number,
          reference: phone.reference,
          version: phone.version,
          id: phone.id,
        }
      })
    }
  }

  const getValidationSchema = () => {
    let validationSchema = _.cloneDeep(newCustomerSchema)
    const { tipoPersoneria } = formData
    if (tipoPersoneria) {
      if (tipoPersoneria.id === 2) {
        validationSchema.required = validationSchema.required.concat("razonSocial")
      } else {
        validationSchema.required = validationSchema.required.concat(
          "nombres",
          "apellidos"
        )
      }
    }
    return validationSchema
  }

  const getLegalPersonFields = () => {
    const { tipoPersoneria } = formData

    if (tipoPersoneria?.id === 2) {
      return (
        <div className="p-col-12">
          <label htmlFor="razonSocial">Razón Social:</label>

          <Field
            id="razonSocial"
            component={InputText}
            name="razonSocial"
            value={formData.razonSocial}
          />
          <FieldError name="razonSocial" />
        </div>
      )
    }
  }

  const getRegularPersonFields = () => {
    const { tipoPersoneria } = formData

    if (tipoPersoneria?.id === 1) {
      return (
        <>
          <div className="p-col-6">
            <label htmlFor="apellidos">Apellidos:</label>

            <Field
              id="apellidos"
              component={InputText}
              name="apellidos"
              value={formData.apellidos}
            />
            <FieldError name="apellidos" />
          </div>

          <div className="p-col-6">
            <label htmlFor="nombres">Nombres:</label>

            <Field
              id="nombres"
              component={InputText}
              name="nombres"
              value={formData.nombres}
            />
            <FieldError name="nombres" />
          </div>
        </>
      )
    }
  }

  const handleChange = (newData) => {
    setFormData(newData)
  }

  const handleExistingCustomer = (customer) => {
    if (customer.customerId != customerId) {
      toast.current.show({
        severity: "error",
        summary: "Ya existe un cliente con estos datos",
        detail: `${customer.businessName} ${customer.identification}`,
      })

      setSaveDisabled(true)
    }
  }

  const handleNotExistentCustomer = (error) => {
    if (error.errorCode === "404002") {
      setSaveDisabled(false)
    }
  }

  const checkExistingCustomer = () => {
    const { tipoDocumento, documento } = formData

    if (tipoDocumento && documento) {
      const searchData = {
        idTipoDocumento: tipoDocumento.id,
        documento: documento,
      }
      customersService.retrieveCustomer(
        searchData,
        handleExistingCustomer,
        handleNotExistentCustomer
      )
    } else {
      setSaveDisabled(true)
    }
  }

  const handleSubmit = () => {
    setLoading(true)

    if (editingCustomer) {
      const customerToEdit = {
        ...formData,
        ...{
          customerId: editingCustomer.customerId,
          version: editingCustomer.version,
        },
      }
      customersService.updateCustomer(customerToEdit, handleSuccess, handleError)
    } else {
      customersService.addNewCustomer(formData, handleSuccess, handleError)
    }
  }

  const handleSuccess = (createdCustomer) => {
    setLoading(false)
    if (createdCustomer) {
      setCustomerId(createdCustomer.customerId)
    } else {
      customersService.getCustomer(customerId, loadCustomer)
    }
    toast.current.show({
      severity: "success",
      summary: "Cliente guardado con éxito",
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
    if (_.get(error, "errorCode") === "400001") {
      detail = error.message
    }

    toast.current.show({
      severity: "error",
      summary: "No se pudo guardar el cliente",
      detail: detail,
      sticky: true,
    })
  }

  const getTitle = () => {
    if (customerId) {
      return `Edición de cliente: ${customerId}`
    }
    return "Nuevo cliente"
  }

  const getTelefonosTableHeader = () => {
    return (
      <div className="p-fluid ">
        <div className="p-grid ">
          <div className="p-col-11">Teléfonos</div>
          <div className="p-col-1">
            <Button
              type="button"
              className="p-button-success"
              icon="fa fa-fw fa-plus"
              onClick={addNewPhone}
            />
          </div>
        </div>
      </div>
    )
  }

  const addNewPhone = () => {
    formData.telefonos.splice(0, 0, { areaCode: "0", number: "", reference: "" })

    handleChange(formData)
  }

  const inputTextEditor = (props, field) => {
    return (
      <div>
        <Field
          component={InputText}
          name={field}
          onChange={(e) => onEditorValueChange(props, e.target.value)}
          value={props.rowData[field]}
        />
        <FieldError name={`telefonos.${props.rowIndex}.${field}`} />
      </div>
    )
  }

  const onEditorValueChange = (props, value) => {
    let updatedTelefonos = [...props.value]
    let updatedFormData = { ...{}, ...formData }
    updatedTelefonos[props.rowIndex][props.field] = value

    updatedFormData.telefonos = updatedTelefonos

    handleChange(updatedFormData)
  }

  return (
    <div className="card card-w-title">
      <h1>{getTitle()}</h1>
      <Toast ref={toast} />
      <Form
        data={formData}
        schema={getValidationSchema()}
        errorMessages={{
          required: () => fieldRequiredDefaultMessage,
          pattern: () => invalidPatternMessage,
        }}
        onChange={handleChange}
        onSubmit={handleSubmit}
      >
        <div className="card p-fluid ">
          <div className="p-grid ">
            <div className="p-col-6">
              <label htmlFor="tipoPersoneria">Tipo Personería:</label>

              <Field
                component={Dropdown}
                options={legalStatusTypes}
                optionLabel="nombreTipo"
                name="tipoPersoneria"
                dataKey="id"
                value={formData.tipoPersoneria}
              />

              <FieldError name="tipoPersoneria" />
            </div>

            <div className="p-col-6">
              <label htmlFor="genero">Género:</label>

              <Field
                component={Dropdown}
                //onChange={(newVal, handleFieldChange) => handleFieldChange('genero', newVal)}
                options={genders}
                optionLabel="nombreGenero"
                dataKey="id"
                name="genero"
                value={formData.genero}
              />

              <FieldError name="genero" />
            </div>

            <div className="p-col-3">
              <label htmlFor="tipoDocumento">Tipo Documento:</label>

              <Field
                component={Dropdown}
                //onChange={(newVal, handleFieldChange) => handleFieldChange('tipoDocumento', newVal)}
                options={identificationTypes}
                optionLabel="identificationTypeName"
                name="tipoDocumento"
                dataKey="id"
                value={formData.tipoDocumento}
              />

              <FieldError name="tipoDocumento" />
            </div>
            <div className="p-col-9">
              <label htmlFor="documento">Número:</label>

              <div className="p-inputgroup">
                <Field
                  id="documento"
                  component={InputText}
                  name="documento"
                  mode="decimal"
                  useGrouping={false}
                  onBlur={checkExistingCustomer}
                  value={formData.documento}
                />
              </div>
              <FieldError name="documento" />
            </div>

            {getLegalPersonFields()}
            {getRegularPersonFields()}

            <div className="p-col-12">
              <label htmlFor="nombreFantasia">Nombre Fantasía:</label>

              <Field
                id="nombreFantasia"
                component={InputText}
                name="nombreFantasia"
                value={formData.nombreFantasia}
              />
              <FieldError name="nombreFantasia" />
            </div>

            <div className="p-col-12">
              <label htmlFor="responsabilidadIva">Responsabilidad IVA:</label>

              <Field
                component={Dropdown}
                //onChange={(newVal, handleFieldChange) => handleFieldChange('responsabilidadIva', newVal)}
                options={responsabilidadesIva}
                optionLabel="nombreResponsabildiad"
                name="responsabilidadIva"
                dataKey="id"
                value={formData.responsabilidadIva}
              />

              <FieldError name="responsabilidadIva" />
            </div>

            <div className="p-col-4">
              <label htmlFor="pais">País:</label>

              <Field
                component={Dropdown}
                options={countries}
                optionLabel="nombrePais"
                dataKey="id"
                name="pais"
                value={formData.pais}
              />

              <FieldError name="pais" />
            </div>
            <div className="p-col-4">
              <label htmlFor="provincia">Provincia:</label>

              <Field
                component={Dropdown}
                options={provinces}
                optionLabel="nombreProvincia"
                dataKey="id"
                name="provincia"
                value={formData.provincia}
              />

              <FieldError name="provincia" />
            </div>

            <div className="p-col-4">
              <label htmlFor="localidad">Localidad:</label>

              <Field
                component={Dropdown}
                options={towns}
                optionLabel="nombreLocalidad"
                dataKey="id"
                name="localidad"
                value={formData.localidad}
              />

              <FieldError name="localidad" />
            </div>

            <div className="p-col-3">
              <label htmlFor="calle">Calle:</label>

              <Field
                id="calle"
                component={InputText}
                name="calle"
                onFocus={(e) => e.target.select()}
                value={formData.calle}
              />
              <FieldError name="calle" />
            </div>

            <div className="p-col-3">
              <label htmlFor="altura">Altura:</label>

              <Field
                id="altura"
                component={InputText}
                name="altura"
                onFocus={(e) => e.target.select()}
                value={formData.altura}
              />
              <FieldError name="altura" />
            </div>

            <div className="p-col-3">
              <label htmlFor="piso">Piso:</label>

              <Field
                id="piso"
                component={InputText}
                name="piso"
                onFocus={(e) => e.target.select()}
                value={formData.piso}
              />
              <FieldError name="piso" />
            </div>

            <div className="p-col-3">
              <label htmlFor="depto">Dpto:</label>

              <Field
                id="depto"
                component={InputText}
                name="depto"
                onFocus={(e) => e.target.select()}
                value={formData.depto}
              />
              <FieldError name="depto" />
            </div>

            <div className="SeparatorFull p-col-12" />

            <div className="p-col-12">
              <DataTable
                value={formData.telefonos}
                header={getTelefonosTableHeader()}
                editable
                emptyMessage="Agregue un teléfono para el cliente"
              >
                <Column
                  header="Código de área"
                  field="areaCode"
                  editor={(props) => {
                    return inputTextEditor(props, "areaCode")
                  }}
                />
                <Column
                  header="Número"
                  field="number"
                  editor={(props) => {
                    return inputTextEditor(props, "number")
                  }}
                />
                <Column
                  header="Referencia"
                  field="reference"
                  editor={(props) => {
                    return inputTextEditor(props, "reference")
                  }}
                />
              </DataTable>
            </div>

            <div className="p-col-12">
              <label htmlFor="email">Email:</label>
              <Field
                id="email"
                component={InputText}
                name="email"
                value={formData.email}
              />
              <FieldError
                errorMessages={{ format: () => emailInvalidFormatMessage }}
                name="email"
              />
            </div>

            <div className="p-col-12">
              <label htmlFor="activo">Activo:</label>
              <Field
                id="active"
                component={Checkbox}
                name="activo"
                checked={formData.activo}
              />
            </div>
          </div>
        </div>
        <div className="SeparatorFull" />

        <LoadingButton
          type="submit"
          label="Guardar"
          loading={loading}
          icon="fa fa-fw fa-save"
          disabled={saveDisabled}
        />
        <Button
          type="button"
          label="Cerrar"
          className="p-button-secondary"
          icon="fa fa-fw fa-arrow-left"
          onClick={() => {
            window.close()
          }}
        />
      </Form>
    </div>
  )
}
