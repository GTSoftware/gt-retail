import React, { useState } from "react"
import LoginService from "./service/LoginService.js"
import classNames from "classnames"

export const AppProfile = ({ onLogout }) => {
  const [expanded, setExpanded] = useState(false)

  const handleLogout = () => {
    LoginService.performLogout()
    if (onLogout) {
      onLogout()
    }
  }

  const handleClick = (event) => {
    setExpanded((prevExpanded) => !prevExpanded)
    event.preventDefault()
  }

  return (
    <div className="layout-profile">
      <div>
        {/* <img src="assets/layout/images/profile.png" alt=""/> */}
        <i className="fa fa-user-circle fa-5x" />
      </div>
      <button className="p-link layout-profile-link" onClick={handleClick}>
        <span className="username">
          {LoginService.getUserDetails().completeUserName}
        </span>
        <i className="fas fa-fw fa-cog " />
      </button>
      <ul className={classNames({ "layout-profile-expanded": expanded })}>
        <li>
          <button
            className="p-link"
            onClick={() => {
              window.location = `#/my-account`
            }}
          >
            <i className="fas fa-fw fa-user" />
            <span>Cuenta</span>
          </button>
        </li>
        {/* <li>
          <button className="p-link">
            <i className="pi pi-fw pi-inbox"/>
            <span>Notifications</span>
            <span className="menuitem-badge">2</span>
          </button>
        </li> */}
        <li>
          <button className="p-link" onClick={handleLogout}>
            <i className="fas fa-fw fa-power-off" />
            <span>Salir</span>
          </button>
        </li>
      </ul>
    </div>
  )
}
