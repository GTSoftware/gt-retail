import _ from "lodash"
import React, { Component } from "react"
import PropTypes from "prop-types"
import { FieldError, Form } from "react-jsonschema-form-validation"
import { CustomersService } from "../../service/CustomersService"
import { InputText } from "primereact/inputtext"
import { Dialog } from "primereact/dialog"
import Field from "react-jsonschema-form-validation/dist/Field"
import {
  emailInvalidFormatMessage,
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { LoadingButton } from "../core/LoadingButton"
import { Dropdown } from "primereact/dropdown"
import { AutoComplete } from "primereact/autocomplete"
import { Toast } from "primereact/toast"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import { LocationService } from "../../service/LocationService"

const newCustomerSchema = {
  type: "object",
  properties: {
    email: { type: "string", format: "email" },
    razonSocial: {
      type: "string",
      minLength: 1,
      maxLength: 200,
      pattern: "^[a-zA-ZñÑ. ]*$",
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
    calle: { type: "string", maxLength: 100, pattern: "^[-a-zA-Z. ]*$" },
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

export class AddNewCustomerDialog extends Component {
  static propTypes = {
    handleNewCustomer: PropTypes.func,
  }

  constructor(props, context) {
    super(props, context)

    this.customersService = new CustomersService()
    this.locationService = new LocationService()

    this.state = {
      formData: {
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
        telefonos: [],
      },
      loading: false,
      legalStatusTypes: [],
      genders: [],
      identificationTypes: [],
      responsabilidadesIva: [],
      countries: [],
      provinces: [],
      filteredTowns: [],
      saveDisabled: true,
    }
  }

  componentDidMount() {
    const { legalStatusTypes, responsabilidadesIva, countries } = this.state

    if (legalStatusTypes.length === 0) {
      this.customersService.getLegalStatusTypes(this.handleLegalStatusTypes)
    }
    if (responsabilidadesIva.length === 0) {
      this.customersService.getResponsabilidadesIva(this.handleResponsabilidadesIva)
    }
    if (countries.length === 0) {
      this.locationService.getCountries(this.handleCountries)
    }
  }

  render() {
    const {
      formData,
      legalStatusTypes,
      genders,
      identificationTypes,
      responsabilidadesIva,
      countries,
      provinces,
      filteredTowns,
    } = this.state
    let validationSchema = _.cloneDeep(newCustomerSchema)
    if (_.get(this.state.formData, "tipoPersoneria.id", null)) {
      if (_.get(this.state.formData, "tipoPersoneria.id") === 2) {
        validationSchema.required = validationSchema.required.concat("razonSocial")
      } else {
        validationSchema.required = validationSchema.required.concat(
          "nombres",
          "apellidos"
        )
      }
    }
    return (
      <Dialog {...this.getDialogProps()}>
        <Toast ref={(el) => (this.toast = el)} />
        <Form
          data={this.state.formData}
          onChange={this.handleChange}
          onSubmit={this.handleSubmit}
          schema={validationSchema}
          errorMessages={{
            required: () => fieldRequiredDefaultMessage,
            pattern: () => invalidPatternMessage,
          }}
        >
          <div className="card p-fluid ">
            <div className="p-grid ">
              <div className="p-col-6">
                <label htmlFor="tipoPersoneria">Tipo Personería:</label>

                <Field
                  component={Dropdown}
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("tipoPersoneria", newVal.value)
                  }
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
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("genero", newVal.value)
                  }
                  options={genders}
                  optionLabel="nombreGenero"
                  dataKey="id"
                  name="genero"
                  value={formData.genero}
                />

                <FieldError name="genero" />
              </div>
            </div>

            <div className="p-grid">
              <div className="p-col-3">
                <label htmlFor="tipoDocumento">Tipo Documento:</label>

                <Field
                  component={Dropdown}
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("tipoDocumento", newVal.value)
                  }
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
                    autoFocus={true}
                    useGrouping={false}
                    onBlur={this.checkExistingCustomer}
                    value={formData.documento}
                  />
                  <span className="p-inputgroup-addon">
                    {this.renderIdentificationValidation()}
                  </span>
                </div>
                <FieldError name="documento" />
              </div>
            </div>

            {this.shouldRenderLegalPersonField() && (
              <div className="p-grid">
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
              </div>
            )}

            {!this.shouldRenderLegalPersonField() && (
              <div className="p-grid">
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
              </div>
            )}

            <div className="p-grid">
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
            </div>

            <div className="p-grid">
              <div className="p-col-12">
                <label htmlFor="responsabilidadIva">Responsabilidad IVA:</label>

                <Field
                  component={Dropdown}
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("responsabilidadIva", newVal.value)
                  }
                  options={responsabilidadesIva}
                  optionLabel="nombreResponsabildiad"
                  name="responsabilidadIva"
                  dataKey="id"
                  value={formData.responsabilidadIva}
                />

                <FieldError name="responsabilidadIva" />
              </div>
            </div>

            <div className="SeparatorFull" />

            <div className="p-grid">
              <div className="p-col-4">
                <label htmlFor="pais">País:</label>

                <Field
                  component={Dropdown}
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("pais", newVal.value)
                  }
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
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("provincia", newVal.value)
                  }
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
                  component={AutoComplete}
                  onChange={(newVal, handleFieldChange) =>
                    handleFieldChange("localidad", newVal.value)
                  }
                  suggestions={filteredTowns}
                  field="displayName"
                  name="localidad"
                  completeMethod={(e) => this.filterTowns(e.query)}
                  value={formData.localidad}
                />

                <FieldError name="localidad" />
              </div>
            </div>

            <div className="p-grid">
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
            </div>

            <div className="SeparatorFull" />

            <DataTable
              value={formData.telefonos}
              header={this.getTelefonosTableHeader()}
              editable
              emptyMessage="Agregue un teléfono para el cliente"
            >
              <Column
                header="Código de área"
                field="areaCode"
                editor={(props) => {
                  return this.inputTextEditor(props, "areaCode")
                }}
              />
              <Column
                header="Número"
                field="number"
                editor={(props) => {
                  return this.inputTextEditor(props, "number")
                }}
              />
              <Column
                header="Referencia"
                field="reference"
                editor={(props) => {
                  return this.inputTextEditor(props, "reference")
                }}
              />
            </DataTable>

            <div className="p-grid">
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
            </div>
          </div>

          <div className="SeparatorFull" />

          <LoadingButton
            type="submit"
            label="Guardar"
            loading={this.state.loading}
            icon="fa fa-fw fa-save"
            disabled={this.state.saveDisabled}
          />
        </Form>
      </Dialog>
    )
  }

  renderIdentificationValidation = () => {
    const { saveDisabled } = this.state
    let validationMark = <i className="fa fa-fw fa-times" style={{ color: "red" }} />

    if (!saveDisabled) {
      validationMark = <i className="fa fa-fw fa-check" style={{ color: "green" }} />
    }

    return validationMark
  }

  inputTextEditor = (props, field) => {
    return (
      <div>
        <Field
          component={InputText}
          name={field}
          onChange={(e) => this.onEditorValueChange(props, e.target.value)}
          value={props.rowData[field]}
        />
        <FieldError name={`telefonos.${props.rowIndex}.${field}`} />
      </div>
    )
  }

  onEditorValueChange = (props, value) => {
    let updatedTelefonos = [...props.value]
    let updatedFormData = { ...{}, ...this.state.formData }
    updatedTelefonos[props.rowIndex][props.field] = value

    updatedFormData.telefonos = updatedTelefonos

    this.setState({ formData: updatedFormData })
  }

  getTelefonosTableHeader = () => {
    return (
      <div>
        Teléfonos
        <div style={{ float: "right" }}>
          <Button
            type="button"
            className="p-button-success"
            icon="fa fa-fw fa-plus"
            onClick={this.addNewPhone}
            tooltip={"Agregar nuevo teléfono"}
          />
        </div>
      </div>
    )
  }

  addNewPhone = () => {
    let { formData } = this.state

    formData.telefonos.splice(0, 0, { areaCode: "0", number: "", reference: "" })

    this.setState({ formData: formData })
  }

  handleSubmit = () => {
    const { formData } = this.state

    this.setState({ loading: true })

    this.customersService.addNewCustomer(
      formData,
      this.handleSuccess,
      this.handleError
    )
  }

  handleError = (error) => {
    let detail = "Error desconocido"

    this.setState({ loading: false })

    if (_.get(error, "errorCode") === "400999") {
      detail = error.fieldErrors.map((errorField) => {
        return errorField + "\n"
      })
    }
    if (_.get(error, "errorCode") === "400001") {
      detail = error.message
    }

    this.toast.show({
      severity: "error",
      summary: "No se pudo guardar el cliente",
      detail: detail,
      sticky: true,
    })
  }

  handleSuccess = (createdCustomer) => {
    this.setState({ loading: false })

    const { handleNewCustomer } = this.props

    if (handleNewCustomer) {
      handleNewCustomer(createdCustomer)
    }
    if (this.props.onHide) {
      this.props.onHide()
    }
  }

  filterTowns = (query) => {
    const { provincia } = this.state.formData

    if (query && provincia) {
      this.locationService.getTowns(provincia.id, query, this.handleTowns)
    }
  }

  shouldRenderLegalPersonField = () => {
    return _.get(this.state.formData, "tipoPersoneria.id") === 2
  }

  handleChange = (newFormData) => {
    const oldFormData = this.state.formData

    if (
      _.get(newFormData, "tipoPersoneria.id") !==
      _.get(oldFormData, "tipoPersoneria.id")
    ) {
      this.customersService.getGenders(
        _.get(newFormData, "tipoPersoneria.id"),
        this.handleGenders
      )
      this.customersService.getIdentificationTypes(
        _.get(newFormData, "tipoPersoneria.id"),
        this.handleIdentificationTypes
      )
    }

    if (_.get(newFormData, "pais.id") !== _.get(oldFormData, "pais.id")) {
      this.customersService.getProvinces(newFormData.pais.id, this.handleProvinces)
    }

    this.setState({ formData: newFormData })
  }

  handleGenders = (genders) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.genero = genders[0]

    this.setState({
      genders: genders,
      formData: newFormData,
    })
  }

  handleIdentificationTypes = (identificationTypes) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.tipoDocumento = identificationTypes[0]

    this.setState({
      identificationTypes: identificationTypes,
      formData: newFormData,
    })
  }

  getDialogProps = () => {
    let props = this.props
    let defaultProps = {
      header: "Nuevo cliente",
      modal: true,
      style: { width: "50%" },
    }

    return { ...defaultProps, ...props }
  }

  handleLegalStatusTypes = (legalStatusTypes) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.tipoPersoneria = legalStatusTypes[0]

    this.customersService.getGenders(
      newFormData.tipoPersoneria.id,
      this.handleGenders
    )
    this.customersService.getIdentificationTypes(
      newFormData.tipoPersoneria.id,
      this.handleIdentificationTypes
    )

    this.setState({
      legalStatusTypes: legalStatusTypes,
      formData: newFormData,
    })
  }

  handleResponsabilidadesIva = (responsabilidadesIva) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.responsabilidadIva = responsabilidadesIva[0]

    this.setState({
      responsabilidadesIva: responsabilidadesIva,
      formData: newFormData,
    })
  }

  handleCountries = (countries) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.pais = countries[0]

    this.locationService.getProvinces(newFormData.pais.id, this.handleProvinces)

    this.setState({
      countries: countries,
      formData: newFormData,
    })
  }

  handleProvinces = (provinces) => {
    const oldFormData = this.state.formData
    let newFormData = { ...oldFormData }

    newFormData.provincia = provinces[0]

    this.setState({
      provinces: provinces,
      formData: newFormData,
      filteredTowns: [],
    })
  }

  handleTowns = (towns) => {
    this.setState({
      filteredTowns: towns,
    })
  }

  checkExistingCustomer = () => {
    const { tipoDocumento, documento } = this.state.formData

    if (tipoDocumento && documento) {
      let searchData = {
        idTipoDocumento: tipoDocumento.id,
        documento: documento,
      }
      this.customersService.retrieveCustomer(
        searchData,
        this.handleExistingCustomer,
        this.handleNotExistentCustomer
      )
    } else {
      this.setState({ saveDisabled: true })
    }
  }

  handleExistingCustomer = (customer) => {
    this.toast.show({
      severity: "error",
      summary: "Ya existe un cliente con estos datos",
      detail: `${customer.businessName} ${customer.identification}`,
    })

    this.setState({ saveDisabled: true })
  }

  handleNotExistentCustomer = (error) => {
    if (error.errorCode === "404002") {
      this.setState({ saveDisabled: false })
    }
  }
}
