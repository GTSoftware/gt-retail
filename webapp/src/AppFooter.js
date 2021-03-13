import React, { Component } from "react"

export class AppFooter extends Component {
  render() {
    return (
      <div className="layout-footer">
        <span className="footer-text" style={{ marginRight: "5px" }}>
          GTSoftware
        </span>
        <img src="assets/layout/images/logo-gt-dark.svg" alt="" width="80" />
        <span className="footer-text" style={{ marginLeft: "5px" }}>
          <a href="mailto: rotatomel@gmail.com">rotatomel@gmail.com</a>
        </span>
      </div>
    )
  }
}
