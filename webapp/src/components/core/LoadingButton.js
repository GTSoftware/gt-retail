import React, {Component} from "react";
import {Button} from "primereact/button";
import PropTypes from 'prop-types';

const loadingIcon = 'fa fa-spinner fa-spin';

export class LoadingButton extends Component {

    static propTypes = {
        loading: PropTypes.bool.isRequired
    }

    render() {
        let buttonProps = {...{}, ...this.props};
        let loadingProps = {
            icon: this.getIcon(buttonProps),
            disabled: this.isDisabled(buttonProps)
        };

        delete buttonProps.loading;

        buttonProps = {...buttonProps, ...loadingProps};

        return <Button {...buttonProps} />
    }

    isDisabled = (buttonProps) => {
        const {loading, disabled} = buttonProps;
        return loading || disabled;
    }

    getIcon = (buttonProps) => {
        const {loading, icon} = buttonProps;
        return loading ? loadingIcon : icon;
    }
}
