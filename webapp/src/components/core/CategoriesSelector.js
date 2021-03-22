import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { CategoriesService } from "../../service/CategoriesService"

export class CategoriesSelector extends Component {
  static propTypes = {
    onCategorySelect: PropTypes.func.isRequired,
  }

  constructor(props) {
    super(props)

    this.state = {
      loaded: false,
      categories: [],
      selectedCategory: null,
    }

    this.service = new CategoriesService()
  }

  componentDidMount() {
    const { loaded } = this.state

    if (!loaded) {
      this.service.getCategories((categories) =>
        this.setState({ categories: categories, loaded: true })
      )
    }
  }

  render() {
    const { selectedCategory, categories } = this.state

    return (
      <Dropdown
        id="category"
        placeholder={"Rubro"}
        filter={true}
        dataKey="categoryId"
        options={categories}
        showClear={true}
        value={selectedCategory}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedCategory: value,
    })

    this.props.onCategorySelect(value)
  }
}
