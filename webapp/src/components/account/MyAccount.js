import React, { useRef, useState } from "react"
import LoginService from "../../service/LoginService"
import { UsersService } from "../../service/UsersService"
import { LoadingButton } from "../core/LoadingButton"
import { isNotEmpty } from "../../utils/StringUtils"
import { Password } from "primereact/password"
import { Toast } from "primereact/toast"
import _ from "lodash"

export const MyAccount = () => {
  const usersService = new UsersService()

  const [password, setPassword] = useState("")
  const [confirmPassword, setConfirmPassword] = useState("")
  const [loading, setLoading] = useState(false)
  const toast = useRef()

  const changePassword = () => {
    if (isNotEmpty(password) && password === confirmPassword) {
      setLoading(true)
      const userId = LoginService.getUserDetails().id
      usersService.changePassword(
        userId,
        password,
        handleSuccessChangePassword,
        handleErrorChangePassword
      )
    }
  }

  const handleSuccessChangePassword = () => {
    setPassword("")
    setConfirmPassword("")
    setLoading(false)
    toast.current.show({
      severity: "info",
      summary: "Contraseña actualizada exitosamente",
    })
  }

  const handleErrorChangePassword = (err) => {
    setPassword("")
    setConfirmPassword("")
    setLoading(false)
    let detail = "Error desconocido"
    if (_.get(err, "errorCode") === "400999") {
      detail = err.fieldErrors.map((errorField) => {
        return errorField + "\n"
      })
    }
    toast.current.show({
      severity: "error",
      summary: "Error al cambiar la contraseña",
      detail: detail,
    })
  }

  return (
    <div className="card card-w-title">
      <h1>Cuenta de usuario</h1>
      <Toast ref={toast} />
      <div className="p-grid p-fluid">
        <div className="p-col-8">
          <label>Nombre de usuario:</label>
          <label>{LoginService.getUserDetails().completeUserName}</label>
        </div>
        <div className="p-col-8">
          <label>Roles asignados:</label>
          <ul>
            {LoginService.getUserDetails().userRoles.map((role, index) => (
              <li key={index}>{role}</li>
            ))}
          </ul>
        </div>
        <div className="p-col-8">
          <label>Sucursal:</label>
          <p>{LoginService.getUserDetails().sucursalName}</p>
        </div>
      </div>

      <h2>Cambiar clave</h2>

      <div className="p-grid p-fluid">
        <div className="p-col-8">
          <label>Ingrese su nueva clave:</label>
          <Password
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type="password"
          />
        </div>
        <div className="p-col-8">
          <label>Repita su nueva clave:</label>
          <Password
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            type="password"
          />
        </div>
      </div>
      <LoadingButton
        loading={loading}
        label={"Cambiar contraseña"}
        onClick={changePassword}
        icon="fa fa-fw fa-key"
      />
    </div>
  )
}
