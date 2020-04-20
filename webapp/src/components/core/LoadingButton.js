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
            icon: buttonProps.loading ? loadingIcon : buttonProps.icon,
            disabled: buttonProps.loading || buttonProps.disabled
        };

        delete buttonProps.loading;

        buttonProps = {...buttonProps, ...loadingProps};

        return <Button {...buttonProps} />
    }
}
