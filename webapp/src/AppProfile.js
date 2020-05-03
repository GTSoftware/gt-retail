import React, {Component} from 'react';
import classNames from 'classnames';
import LoginService from "./service/LoginService.js";
import PropTypes from "prop-types";

export class AppProfile extends Component {

    static propTypes = {
        onLogout: PropTypes.func
    }

    constructor() {
        super();
        this.state = {
            expanded: false
        };
        this.onClick = this.onClick.bind(this);
    }

    onClick(event) {
        this.setState({expanded: !this.state.expanded});
        event.preventDefault();
    }

    render() {
        return (
            <div className="layout-profile">
                <div>
                    {/*<img src="assets/layout/images/profile.png" alt=""/>*/}
                    <i className="fa fa-user-circle fa-5x"/>
                </div>
                <button className="p-link layout-profile-link" onClick={this.onClick}>
                    <span className="username">{LoginService.getUserDetails().completeUserName}</span>
                    <i className="fas fa-fw fa-cog "/>
                </button>
                <ul className={classNames({'layout-profile-expanded': this.state.expanded})}>
                    <li>
                        <button className="p-link"><i className="fas fa-fw fa-user"/><span>Cuenta</span></button>
                    </li>
                    {/*<li>*/}
                    {/*    <button className="p-link"><i className="pi pi-fw pi-inbox"/><span>Notifications</span><span*/}
                    {/*        className="menuitem-badge">2</span></button>*/}
                    {/*</li>*/}
                    <li>
                        <button className="p-link" onClick={this.handleLogOut}><i
                            className="fas fa-fw fa-power-off"/><span>Salir</span></button>
                    </li>
                </ul>
            </div>
        );
    }

    handleLogOut = () => {
        LoginService.performLogout();

        if (this.props.onLogout) {
            this.props.onLogout();
        }
    }
}
