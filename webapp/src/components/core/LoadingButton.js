import React from "react"
import { Button } from "primereact/button"
import PropTypes from "prop-types"

const LOADING_ICON = "fa fa-spinner fa-spin"

export const LoadingButton = (props) => {
  const isDisabled = (buttonProps) => {
    const { loading, disabled } = buttonProps

    return loading || disabled
  }

  const getIcon = (buttonProps) => {
    const { loading, icon } = buttonProps

    return loading ? LOADING_ICON : icon
  }

  const getLoadingProps = () => {
    const customProps = {
      icon: getIcon(props),
      disabled: isDisabled(props),
    }

    return { ...{}, ...props, ...customProps }
  }

  return <Button {...getLoadingProps()} />
}

LoadingButton.propTypes = {
  loading: PropTypes.bool.isRequired,
}
