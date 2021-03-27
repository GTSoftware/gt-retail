import React, { Component } from "react"
import { Dropdown } from "primereact/dropdown"
import PropTypes from "prop-types"
import { CategoriesService } from "../../service/CategoriesService"

export class SubCategoriesSelector extends Component {
  static propTypes = {
    onSubCategorySelect: PropTypes.func.isRequired,
    categoryId: PropTypes.number,
    selectedSubCategory: PropTypes.object,
  }

  constructor(props) {
    super(props)

    this.state = {
      subCategories: [],
      selectedSubCategory: props.selectedSubCategory || null,
    }

    this.service = new CategoriesService()
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const { categoryId } = this.props

    if (prevProps.categoryId !== categoryId) {
      if (categoryId) {
        this.service.getSubCategories(categoryId, (subCategories) =>
          this.setState({ subCategories: subCategories })
        )
      } else {
        this.setState({ subCategories: [] })
      }
    }
  }

  render() {
    const { selectedSubCategory, subCategories } = this.state

    return (
      <Dropdown
        id="subCategory"
        placeholder={"Sub-Rubro"}
        filter={true}
        dataKey="subCategoryId"
        options={subCategories}
        showClear={true}
        value={selectedSubCategory}
        optionLabel="displayName"
        onChange={(e) => this.handleSelectionChange(e.value)}
      />
    )
  }

  handleSelectionChange = (value) => {
    this.setState({
      selectedSubCategory: value,
    })

    this.props.onSubCategorySelect(value)
  }
}
