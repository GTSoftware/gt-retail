import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { ProductsService } from "../../service/ProductsService"

export class PercentTypesSelector extends Component {
  static propTypes = {
    onChange: PropTypes.func.isRequired,
    selectedItem: PropTypes.object,
  }

  constructor(props) {
    super(props)

    this.state = {
      loaded: false,
      items: [],
      selectedItem: props.selectedItem || null,
    }

    this.service = new ProductsService()
  }

  componentDidMount() {
    const { loaded } = this.state

    if (!loaded) {
      this.service.getPercentTypes((items) =>
        this.setState({ items: items, loaded: true })
      )
    }
  }

  render() {
    const { selectedItem, items } = this.state

    return (
      <Dropdown
        id="percentType"
        placeholder={"Tipo de porcentaje"}
        filter={true}
        dataKey="percentTypeId"
        options={items}
        showClear={true}
        value={selectedItem}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedItem: value,
    })

    this.props.onChange(value)
  }
}
