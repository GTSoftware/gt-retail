import React, { Component } from "react"
import { InputText } from "primereact/inputtext"
import { Password } from "primereact/password"
import LoginService from "../service/LoginService.js"
import { Messages } from "primereact/messages"
import { Redirect } from "react-router-dom"
import { LoadingButton } from "./core/LoadingButton"
import "./LogIn.scss"
import PropTypes from "prop-types"

export class Login extends Component {
  static propTypes = {
    onLoginSuccess: PropTypes.func,
  }

  constructor() {
    super()

    this.state = {
      userCredentials: {
        username: "",
        password: "",
      },
      loginDisabled: true,
      loading: false,
    }
  }

  render() {
    const logo = "assets/layout/images/logo-gt-dark.svg"

    if (LoginService.isUserLoggedIn()) {
      return <Redirect to="/" />
    }

    return (
      <div className="p-grid p-fluid">
        <div className="p-col-12 p-lg-6">
          <div className="login-logo--container">
            <img className="login-logo--img" alt="Logo" src={logo} />
          </div>
          <div className="card card-w-title">
            <h1>LogIn</h1>
            <span className="p-float-label p-col-12 login-input--field">
              <InputText
                id="username"
                value={this.state.userCredentials.username}
                onChange={(e) =>
                  this.handlePropertyChange("username", e.target.value)
                }
                autoFocus
                onKeyPress={this.handleEnterKeyPress}
              />
              <label className="login-input--label" htmlFor="username">
                Usuario
              </label>
            </span>

            <span className="p-float-label p-col-12 login-input--field">
              <Password
                id="password"
                feedback={false}
                value={this.state.userCredentials.password}
                onChange={(e) =>
                  this.handlePropertyChange("password", e.target.value)
                }
                onKeyPress={this.handleEnterKeyPress}
              />
              <label className="login-input--label" htmlFor="password">
                Clave
              </label>
            </span>

            <div className="login-button">
              <LoadingButton
                icon="fas fa-lock-open"
                label="LogIn"
                loading={this.state.loading}
                onClick={this.performLogin}
                disabled={this.state.loginDisabled}
              />
            </div>
            <Messages ref={(el) => (this.messages = el)} />
          </div>
        </div>
      </div>
    )
  }

  handlePropertyChange(propertyName, value) {
    let userCredentials = this.state.userCredentials
    userCredentials[propertyName] = value

    this.setState({
      userCredentials,
    })
    this.handleLoginButton()
  }

  handleEnterKeyPress = (event) => {
    if (event.key === "Enter" && !this.state.loginDisabled) {
      this.performLogin()
    }
  }

  handleLoginButton = () => {
    let userCredentials = this.state.userCredentials
    let loginDisabled = true

    if (userCredentials.username.length > 1 && userCredentials.password.length > 1) {
      loginDisabled = false
    }
    this.setState({ loginDisabled })
  }

  performLogin = () => {
    this.setState({ loading: true })
    LoginService.performLogin(
      this.state.userCredentials,
      this.handleLoginDone,
      this.showError
    )
  }

  handleLoginDone = () => {
    if (this.props.onLoginSuccess) {
      this.props.onLoginSuccess()
    }
    this.props.history.replace("/")
  }

  showError = () => {
    this.setState({ loading: false })
    this.messages.show({
      severity: "error",
      summary: "Credenciales no válidas",
      detail: "",
    })
  }
}
