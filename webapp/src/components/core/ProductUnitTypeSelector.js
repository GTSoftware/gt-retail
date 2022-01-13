import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { ProductsService } from "../../service/ProductsService"

export class ProductUnitTypeSelector extends Component {
  static propTypes = {
    onSelectUnitType: PropTypes.func.isRequired,
    selectedUnitType: PropTypes.object,
  }

  constructor(props) {
    super(props)

    this.state = {
      loaded: false,
      unitTypes: [],
      selectedUnitType: props.selectedUnitType || null,
    }

    this.service = new ProductsService()
  }

  componentDidMount() {
    const { loaded } = this.state

    if (!loaded) {
      this.service.getUnitTypes((unitTypes) =>
        this.setState({ unitTypes: unitTypes, loaded: true })
      )
    }
  }

  render() {
    const { selectedUnitType, unitTypes } = this.state

    return (
      <Dropdown
        id="unitType"
        placeholder={""}
        filter={true}
        dataKey="unitTypeId"
        options={unitTypes}
        showClear={true}
        value={selectedUnitType}
        optionLabel="unitName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedUnitType: value,
    })

    this.props.onSelectUnitType(value)
  }
}
