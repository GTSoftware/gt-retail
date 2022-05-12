import React, { useEffect, useRef, useState } from "react"
import { InputText } from "primereact/inputtext"
import { Password } from "primereact/password"
import LoginService from "../service/LoginService.js"
import { Messages } from "primereact/messages"
import { Redirect, useHistory } from "react-router-dom"
import { LoadingButton } from "./core/LoadingButton"
import "./LogIn.scss"
import PropTypes from "prop-types"

const LOGO = "assets/layout/images/logo-gt-dark.svg"

export const Login = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [loginDisabled, setLoginDisabled] = useState(true)
  const [loading, setLoading] = useState(false)
  const messages = useRef(null)
  const history = useHistory()

  const handleLoginDisabled = () => {
    if (username.length > 1 && password.length > 1) {
      setLoginDisabled(false)
    } else {
      setLoginDisabled(true)
    }
  }

  useEffect(handleLoginDisabled, [username, password])

  const handleEnterKeyPress = (event) => {
    if (event.key === "Enter" && !loginDisabled) {
      performLogin()
    }
  }

  const performLogin = () => {
    setLoading(true)
    LoginService.performLogin({ username, password }, handleLoginDone, showError)
  }

  const handleLoginDone = () => {
    if (onLoginSuccess) {
      onLoginSuccess()
    }
    history.replace("/")
  }

  const showError = () => {
    setLoading(false)

    messages.current.show({
      severity: "error",
      summary: "Credenciales no válidas",
      detail: "",
    })
  }

  const loginForm = (
    <div className="p-grid p-fluid p-justify-center ">
      <div className="p-col-6 p-lg-6 ">
        <div className="login-logo--container">
          <img className="login-logo--img" alt="Logo" src={LOGO} />
        </div>
        <div className="card card-w-title ">
          <h1>LogIn</h1>
          <span className="p-float-label p-col-12 login-input--field">
            <InputText
              id="username"
              value={username}
              onChange={(e) => {
                setUsername(e.target.value)
              }}
              autoFocus
              onKeyPress={handleEnterKeyPress}
            />
            <label className="login-input--label" htmlFor="username">
              Usuario
            </label>
          </span>

          <span className="p-float-label p-col-12 login-input--field">
            <Password
              id="password"
              feedback={false}
              value={password}
              onChange={(e) => {
                setPassword(e.target.value)
              }}
              onKeyPress={handleEnterKeyPress}
            />
            <label className="login-input--label" htmlFor="password">
              Clave
            </label>
          </span>

          <div className="login-button">
            <LoadingButton
              icon="fas fa-lock-open"
              label="LogIn"
              loading={loading}
              onClick={performLogin}
              disabled={loginDisabled}
            />
          </div>
          <Messages ref={messages} />
        </div>
      </div>
    </div>
  )

  const getLoginForm = () => {
    if (LoginService.isUserLoggedIn()) {
      return <Redirect to="/" />
    }
    return loginForm
  }

  return getLoginForm()
}

Login.propTypes = {
  onLoginSuccess: PropTypes.func,
}
