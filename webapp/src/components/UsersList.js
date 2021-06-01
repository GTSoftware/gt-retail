import React, { Component } from "react"
import UsersService from "../service/UsersService"
import SucursalesService from "../service/SucursalesService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import { Dialog } from "primereact/dialog"
import { InputText } from "primereact/inputtext"
import { Toast } from "primereact/toast"
import { Dropdown } from "primereact/dropdown"
import { DEFAULT_DATA_TABLE_PROPS } from "./DefaultProps"

const userColumns = [
  { field: "id", header: "Id" },
  { field: "login", header: "LogIn" },
  { field: "nombreUsuario", header: "Nombre" },
  { field: "idSucursal.nombreSucursal", header: "Sucursal" },
]

export class UsersList extends Component {
  constructor() {
    super()

    this.state = {
      loading: true,
      displayDialog: false,
      users: [],
      sucursales: [],
      selectedUser: null,
    }

    this.addNewUser = this.addNewUser.bind(this)
    this.handleEditUser = this.handleEditUser.bind(this)
    this.renderDialog = this.renderDialog.bind(this)
    this.getTableActions = this.getTableActions.bind(this)
    this.renderColumns = this.renderColumns.bind(this)
    this.handleResetPassword = this.handleResetPassword.bind(this)
    this.handleRetrieveSucursales = this.handleRetrieveSucursales.bind(this)
    this.getUsersTableProps = this.getUsersTableProps.bind(this)
    this.renderPlaceholder = this.renderPlaceholder.bind(this)
    this.renderMainContent = this.renderMainContent.bind(this)
  }

  componentDidMount() {
    UsersService.retrieveUsers((response) => this.handleRetrieveUsers(response))

    SucursalesService.retrieveActiveSucursales((response) =>
      this.handleRetrieveSucursales(response)
    )
  }

  render() {
    let contentToRender

    if (this.state.loading) {
      contentToRender = this.renderPlaceholder()
    } else {
      contentToRender = this.renderMainContent()
    }

    return contentToRender
  }

  renderMainContent() {
    return (
      <div className="card card-w-title">
        <Toast ref={(el) => (this.toast = el)} />
        <DataTable {...this.getUsersTableProps()}>{this.renderColumns()}</DataTable>
        {this.renderDialog()}
      </div>
    )
  }

  renderPlaceholder() {
    return <span className="fa fa-spinner fa-spin fa-3x" />
  }

  getUsersTableProps() {
    let header = (
      <div className="p-clearfix" style={{ lineHeight: "1.87em" }}>
        Usuarios
      </div>
    )
    let footer = (
      <div className="p-clearfix" style={{ width: "100%" }}>
        <Button
          style={{ float: "left" }}
          label="Nuevo"
          icon="fa fa-fw fa-plus"
          onClick={this.addNewUser}
        />
      </div>
    )

    let tableProps = Object.assign({}, DEFAULT_DATA_TABLE_PROPS, {
      value: this.state.users,
      header: header,
      footer: footer,
    })

    return tableProps
  }

  getTableActions(rowData, column) {
    return (
      <div className="p-grid p-fluid">
        <Button
          type="button"
          icon="fa fa-fw fa-lock"
          className="p-button-warning"
          tooltip={"Resetear clave"}
          onClick={() => this.handleResetPassword(rowData)}
        />
        <Button
          type="button"
          icon="fa fa-fw fa-edit"
          tooltip={"Editar usuario"}
          onClick={() => this.handleEditUser(rowData)}
        />
      </div>
    )
  }

  renderColumns() {
    let columns = userColumns.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          sortable
          filter
        />
      )
    })
    columns = columns.concat(
      <Column
        key="actions"
        body={this.getTableActions}
        style={{ textAlign: "center", width: "6em" }}
      />
    )

    return columns
  }

  handleRetrieveUsers(data) {
    this.setState({
      users: data,
      loading: false,
    })
  }

  handleRetrieveSucursales(data) {
    if (data) {
      data.forEach((suc) => {
        suc.businessString = "[" + suc.id + "] " + suc.nombreSucursal
      })

      this.setState({ sucursales: data })
    }
  }

  addNewUser() {
    this.newUser = true
    this.setState({
      selectedUser: {
        login: "",
        nombreUsuario: "",
        idSucursal: null,
      },
      displayDialog: true,
    })
  }

  handleEditUser(user) {
    this.newUser = false

    this.setState({
      displayDialog: true,
      selectedUser: Object.assign({}, user),
    })
  }

  renderDialog() {
    let dialogFooter = (
      <div className="ui-dialog-buttonpane p-clearfix">
        <Button label="Guardar" icon="fa fa-fw fa-save" onClick={this.save} />
      </div>
    )

    return (
      <Dialog
        visible={this.state.displayDialog}
        header="Detalles de usuario"
        modal={true}
        footer={dialogFooter}
        onHide={() => this.setState({ displayDialog: false })}
      >
        {this.state.selectedUser && (
          <div className="p-grid p-fluid">
            <div className="p-col-4" style={{ padding: ".75em" }}>
              <label htmlFor="login">LogIn</label>
            </div>
            <div className="p-col-8" style={{ padding: ".5em" }}>
              <InputText
                id="login"
                onChange={(e) => {
                  this.updateProperty("login", e.target.value)
                }}
                value={this.state.selectedUser.login}
                required={true}
              />
            </div>

            <div className="p-col-4" style={{ padding: ".75em" }}>
              <label htmlFor="nombreUsuario">Nombre</label>
            </div>
            <div className="p-col-8" style={{ padding: ".5em" }}>
              <InputText
                id="nombreUsuario"
                onChange={(e) => {
                  this.updateProperty("nombreUsuario", e.target.value)
                }}
                value={this.state.selectedUser.nombreUsuario}
                required={true}
              />
            </div>
            <div className="p-col-4" style={{ padding: ".75em" }}>
              <label htmlFor="sucursal">Sucursal</label>
            </div>
            <div className="p-col-8" style={{ padding: ".5em" }}>
              <Dropdown
                id="sucursal"
                itemTemplate={this.getSucursalTemplate}
                optionLabel="businessString"
                dataKey="id"
                value={this.state.selectedUser.idSucursal}
                options={this.state.sucursales}
                onChange={(event) => {
                  this.updateProperty("idSucursal", event.value)
                }}
                placeholder="Sucursal"
                required={true}
              />
            </div>
          </div>
        )}
      </Dialog>
    )
  }

  updateProperty(property, value) {
    let selectedUser = this.state.selectedUser

    selectedUser[property] = value
    this.setState({ selectedUser: selectedUser })
  }

  handleResetPassword(rowData) {
    UsersService.resetUserPassword(
      rowData,
      (response) =>
        this.toast.show({
          severity: "info",
          summary: "Clave reestablecida a:",
          detail: response.newPassword,
        }),
      (error) =>
        this.toast.show({
          severity: "error",
          summary: "Error al intentar reestablecer la clave",
          detail: error.message,
        })
    )
  }
}
