import _ from "lodash"
import React, { Component } from "react"
import { TabPanel, TabView } from "primereact/tabview"
import { InputText } from "primereact/inputtext"
import { Checkbox } from "primereact/checkbox"
import PropTypes from "prop-types"
import { LoadingButton } from "./LoadingButton"
import { TriStateCheckbox } from "primereact/tristatecheckbox"
import { BrandsSelector } from "./BrandsSelector"
import { CategoriesSelector } from "./CategoriesSelector"
import { SubCategoriesSelector } from "./SubCategoriesSelector"
import { AutocompleteSupplierFilter } from "./AutocompleteSupplierFilter"

export class SearchProductsFilter extends Component {
  static propTypes = {
    searchProductsCallback: PropTypes.func.isRequired,
    loading: PropTypes.bool.isRequired,
  }

  constructor(props, context) {
    super(props, context)

    this.state = {
      activeSearchTab: 0,
      searchFilter: {
        activo: true,
        txt: "",
        conStock: true,
        buscarEnTodosLados: false,
        codigoPropio: null,
        codigoFabrica: null,
        idProducto: null,
        idMarca: null,
        idRubro: null,
        idSubRubro: null,
        sortFields: [
          {
            fieldName: "descripcion",
            ascending: true,
          },
        ],
      },
    }
  }

  render() {
    return (
      <div>
        <TabView
          activeIndex={this.state.activeSearchTab}
          onTabChange={(e) => this.setState({ activeSearchTab: e.index })}
        >
          <TabPanel header="Básico" leftIcon="fa fa-fw fa-search">
            {this.renderBasicSearchOptions()}
          </TabPanel>
          <TabPanel header="Avanzado" leftIcon="fa fa-fw fa-cog">
            {this.renderAdvancedSearchOptions()}
          </TabPanel>
        </TabView>
        {this.renderSearchButton()}
      </div>
    )
  }

  renderBasicSearchOptions = () => {
    const { searchFilter } = this.state

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-12 p-lg-3">{this.renderTextSearch(searchFilter)}</div>
        <div className="p-col-12 p-lg-2">{this.renderCodeSearch(searchFilter)}</div>
        <div className="p-col-12 p-lg-2">
          {this.renderFactoryCodeSearch(searchFilter)}
        </div>
        <div className="p-col-12 p-lg-2">
          {this.renderStockAvailableSearch(searchFilter)}
        </div>
        <div className="p-col-12 p-lg-2">
          {this.renderSearchAllPlacesCheckbox(searchFilter)}
        </div>
      </div>
    )
  }

  renderSearchAllPlacesCheckbox = (searchFilter) => {
    return (
      <>
        <Checkbox
          id="buscarTodo"
          onChange={(e) => {
            this.handleSearchFilterPropertyChange("buscarEnTodosLados", e.checked)
          }}
          checked={searchFilter.buscarEnTodosLados}
        />
        <label htmlFor="buscarTodo" className="p-checkbox-label">
          {"Buscar todo"}
        </label>
      </>
    )
  }

  renderStockAvailableSearch = (searchFilter) => {
    return (
      <>
        <Checkbox
          id="soloStock"
          onChange={(e) => {
            this.handleSearchFilterPropertyChange("conStock", e.checked)
          }}
          checked={searchFilter.conStock}
        />
        <label htmlFor="soloStock" className="p-checkbox-label">
          Solo con stock
        </label>
      </>
    )
  }

  renderFactoryCodeSearch = (searchFilter) => {
    return (
      <InputText
        id="codigoFabricaInput"
        onChange={(e) => {
          this.handleSearchFilterPropertyChange("codigoFabrica", e.target.value)
        }}
        value={searchFilter.codigoFabrica}
        placeholder="Código Fabricante"
        onKeyPress={this.handleEnterKeyPress}
      />
    )
  }

  renderCodeSearch = (searchFilter) => {
    return (
      <InputText
        id="codigoInput"
        onChange={(e) => {
          this.handleSearchFilterPropertyChange("codigoPropio", e.target.value)
        }}
        value={searchFilter.codigoPropio}
        placeholder="Código propio"
        onKeyPress={this.handleEnterKeyPress}
      />
    )
  }

  renderTextSearch = (searchFilter) => {
    return (
      <InputText
        id="searchText"
        autoFocus
        onChange={(e) => {
          this.handleSearchFilterPropertyChange("txt", e.target.value)
        }}
        value={searchFilter.txt}
        placeholder="Términos de búsqueda"
        onKeyPress={this.handleEnterKeyPress}
      />
    )
  }

  renderAdvancedSearchOptions = () => {
    const { searchFilter } = this.state

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-12 p-lg-2">
          <BrandsSelector
            onBrandSelect={(brand) => {
              this.handleSearchFilterPropertyChange(
                "idMarca",
                _.get(brand, "brandId")
              )
            }}
          />
        </div>
        <div className="p-col-12 p-lg-2">
          <CategoriesSelector
            onCategorySelect={(category) => {
              this.handleSearchFilterPropertyChange(
                "idRubro",
                _.get(category, "categoryId")
              )
            }}
          />
        </div>
        <div className="p-col-12 p-lg-2">
          <SubCategoriesSelector
            onSubCategorySelect={(subCategory) => {
              this.handleSearchFilterPropertyChange(
                "idSubRubro",
                _.get(subCategory, "subCategoryId")
              )
            }}
            categoryId={searchFilter.idRubro}
          />
        </div>
        <div className="p-col-12 p-lg-1">{this.renderIdSearch(searchFilter)}</div>
        <div className="p-col-12 p-lg-1">
          {this.renderEnabledSearchCheckbox(searchFilter)}
        </div>
        <div className="p-col-12 p-lg-6">{this.renderSupplierSearch()}</div>
      </div>
    )
  }

  renderEnabledSearchCheckbox = (searchFilter) => {
    return (
      <>
        <TriStateCheckbox
          id="activoCheck"
          onChange={(e) => {
            this.handleSearchFilterPropertyChange("activo", e.value)
          }}
          value={searchFilter.activo}
        />
        <label htmlFor="activoCheck" className="p-checkbox-label">
          {"Activos"}
        </label>
      </>
    )
  }

  renderIdSearch = (searchFilter) => {
    return (
      <InputText
        id="idInput"
        onChange={(e) => {
          this.handleSearchFilterPropertyChange("idProducto", e.target.value)
        }}
        value={searchFilter.idProducto}
        placeholder="Id del producto"
        onKeyPress={this.handleEnterKeyPress}
      />
    )
  }

  renderSearchButton = () => {
    const { loading, searchProductsCallback } = this.props

    return (
      <div className="p-grid p-fluid">
        <div className="p-col-12 p-lg-12">
          <LoadingButton
            type="button"
            icon="fa fa-fw fa-filter"
            loading={loading}
            label={"Buscar"}
            onClick={() => searchProductsCallback(this.state.searchFilter)}
          />
        </div>
      </div>
    )
  }

  handleSearchFilterPropertyChange = (property, value) => {
    let { searchFilter } = this.state

    searchFilter[property] = value

    if (property === "idRubro") {
      searchFilter.idSubRubro = null
    }

    this.setState({ searchFilter: searchFilter })
  }

  handleEnterKeyPress = (event) => {
    if (event.key === "Enter") {
      this.props.searchProductsCallback(this.state.searchFilter)
    }
  }

  renderSupplierSearch = () => {
    return (
      <AutocompleteSupplierFilter
        onSupplierSelect={(supplier) => {
          this.handleSearchFilterPropertyChange(
            "idProveedorHabitual",
            _.get(supplier, "personId")
          )
        }}
      />
    )
  }
}
