import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { BrandsService } from "../../service/BrandsService"

export class BrandsSelector extends Component {
  static propTypes = {
    onBrandSelect: PropTypes.func.isRequired,
  }
  constructor(props) {
    super(props)

    this.state = {
      loadedBrands: false,
      brands: [],
      selectedBrand: null,
    }

    this.service = new BrandsService()
  }

  componentDidMount() {
    const { loadedBrands } = this.state

    if (!loadedBrands) {
      this.service.getBrands((brands) =>
        this.setState({ brands: brands, loadedBrands: true })
      )
    }
  }

  render() {
    const { selectedBrand, brands } = this.state

    return (
      <Dropdown
        id="brand"
        placeholder={"Marca"}
        filter={true}
        dataKey="brandId"
        options={brands}
        showClear={true}
        value={selectedBrand}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedBrand: value,
    })

    this.props.onBrandSelect(value)
  }
}
