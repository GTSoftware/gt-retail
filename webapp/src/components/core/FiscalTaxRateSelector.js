import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { FiscalTaxRateService } from "../../service/FiscalTaxRateService"

export class FiscalTaxRateSelector extends Component {
  static propTypes = {
    onTaxRateSelect: PropTypes.func.isRequired,
    selectedTaxRate: PropTypes.object,
  }

  constructor(props) {
    super(props)

    this.state = {
      loaded: false,
      taxRates: [],
      selectedTaxRate: props.selectedTaxRate || null,
    }

    this.service = new FiscalTaxRateService()
  }

  componentDidMount() {
    const { loaded } = this.state

    if (!loaded) {
      this.service.getTaxRates((taxRates) =>
        this.setState({ taxRates: taxRates, loaded: true })
      )
    }
  }

  render() {
    const { selectedTaxRate, taxRates } = this.state

    return (
      <Dropdown
        id="taxRate"
        placeholder={"Tasa IVA"}
        filter={true}
        dataKey="taxRateId"
        options={taxRates}
        showClear={true}
        value={selectedTaxRate}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedTaxRate: value,
    })

    this.props.onTaxRateSelect(value)
  }
}
