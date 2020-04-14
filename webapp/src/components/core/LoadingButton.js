import React, {Component} from "react";
import {Button} from "primereact/button";

const loadingIcon = 'fa fa-spinner fa-spin';

export class LoadingButton extends Component {

    render() {
        let buttonProps = this.props;
        let loadingProps = {
            icon: buttonProps.loading ? loadingIcon : buttonProps.icon,
            disabled: buttonProps.loading || buttonProps.disabled
        };

        buttonProps = {...buttonProps, ...loadingProps};

        return <Button {...buttonProps} />
    }
}