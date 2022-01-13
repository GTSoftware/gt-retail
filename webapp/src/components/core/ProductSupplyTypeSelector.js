import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { ProductsService } from "../../service/ProductsService"

export class ProductSupplyTypeSelector extends Component {
  static propTypes = {
    onSupplyTypeSelect: PropTypes.func.isRequired,
    selectedSupplyType: PropTypes.object,
  }

  constructor(props) {
    super(props)

    this.state = {
      loaded: false,
      supplyTypes: [],
      selectedSupplyType: props.selectedSupplyType || null,
    }

    this.service = new ProductsService()
  }

  componentDidMount() {
    const { loaded } = this.state

    if (!loaded) {
      this.service.getSupplyTypes((supplyTypes) =>
        this.setState({ supplyTypes: supplyTypes, loaded: true })
      )
    }
  }

  render() {
    const { selectedSupplyType, supplyTypes } = this.state

    return (
      <Dropdown
        id="supplyType"
        placeholder={"Tipo de proveeduria"}
        filter={true}
        dataKey="supplyTypeId"
        options={supplyTypes}
        showClear={true}
        value={selectedSupplyType}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedSupplyType: value,
    })

    this.props.onSupplyTypeSelect(value)
  }
}
