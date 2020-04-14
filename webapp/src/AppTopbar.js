import React, {Component} from 'react';
import PropTypes from 'prop-types';
import LoginService from "./service/LoginService";

export class AppTopbar extends Component {

    static defaultProps = {
        onToggleMenu: null
    };

    static propTypes = {
        onToggleMenu: PropTypes.func.isRequired
    };

    render() {
        return (
            <div className="layout-topbar clearfix">
                <button className="p-link layout-menu-button" onClick={this.props.onToggleMenu}>
                    <span className="fa fa-fw fa-bars"/>
                </button>
                <div className="layout-topbar-icons">
                    <button className="p-link">
                        <span className="layout-topbar-item-text">
                            {LoginService.getUserDetails().sucursalName}
                        </span>
                        <span className="layout-topbar-icon fa fa-fw fa-house-user"/>
                    </button>
                </div>
                {/*<div className="layout-topbar-icons">*/}
                {/*    <span className="layout-topbar-search">*/}
                {/*        <InputText type="text" placeholder="Search" />*/}
                {/*        <span className="layout-topbar-search-icon pi pi-search"/>*/}
                {/*    </span>*/}
                {/*    <button className="p-link">*/}
                {/*        <span className="layout-topbar-item-text">Events</span>*/}
                {/*        <span className="layout-topbar-icon pi pi-calendar"/>*/}
                {/*        <span className="layout-topbar-badge">5</span>*/}
                {/*    </button>*/}
                {/*    <button className="p-link">*/}
                {/*        <span className="layout-topbar-item-text">Settings</span>*/}
                {/*        <span className="layout-topbar-icon pi pi-cog"/>*/}
                {/*    </button>*/}
                {/*    <button className="p-link">*/}
                {/*        <span className="layout-topbar-item-text">User</span>*/}
                {/*        <span className="layout-topbar-icon pi pi-user"/>*/}
                {/*    </button>*/}
                {/*</div>*/}
            </div>
        );
    }
}