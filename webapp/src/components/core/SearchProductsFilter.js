import _ from "lodash"
import React, { useState } from "react"
import { TabPanel, TabView } from "primereact/tabview"
import { InputText } from "primereact/inputtext"
import { Checkbox } from "primereact/checkbox"
import { LoadingButton } from "./LoadingButton"
import { TriStateCheckbox } from "primereact/tristatecheckbox"
import { BrandsSelector } from "./BrandsSelector"
import { CategoriesSelector } from "./CategoriesSelector"
import { SubCategoriesSelector } from "./SubCategoriesSelector"
import { AutocompleteSupplierFilter } from "./AutocompleteSupplierFilter"

export const SearchProductsFilter = ({ searchProductsCallback, loading }) => {
  const [searchFilter, setSearchFilter] = useState({
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
  })
  const [activeSearchTab, setActiveSearchTab] = useState(0)

  const renderBasicSearchOptions = () => {
    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-12 p-lg-3">{renderTextSearch()}</div>
        <div className="p-col-12 p-lg-2">{renderCodeSearch()}</div>
        <div className="p-col-12 p-lg-2">{renderFactoryCodeSearch()}</div>
        <div className="p-col-12 p-lg-2">{renderStockAvailableSearch()}</div>
        <div className="p-col-12 p-lg-2">{renderSearchAllPlacesCheckbox()}</div>
      </div>
    )
  }

  const renderSearchAllPlacesCheckbox = () => {
    return (
      <>
        <Checkbox
          id="buscarTodo"
          onChange={(e) => {
            handleSearchFilterPropertyChange("buscarEnTodosLados", e.checked)
          }}
          checked={searchFilter.buscarEnTodosLados}
        />
        <label htmlFor="buscarTodo" className="p-checkbox-label">
          {"Buscar todo"}
        </label>
      </>
    )
  }

  const renderStockAvailableSearch = () => {
    return (
      <>
        <Checkbox
          id="soloStock"
          onChange={(e) => {
            handleSearchFilterPropertyChange("conStock", e.checked)
          }}
          checked={searchFilter.conStock}
        />
        <label htmlFor="soloStock" className="p-checkbox-label">
          {"Solo con stock"}
        </label>
      </>
    )
  }

  const renderFactoryCodeSearch = () => {
    return (
      <InputText
        id="codigoFabricaInput"
        onChange={(e) => {
          handleSearchFilterPropertyChange("codigoFabrica", e.target.value)
        }}
        value={searchFilter.codigoFabrica}
        placeholder="Código Fabricante"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const renderCodeSearch = () => {
    return (
      <InputText
        id="codigoInput"
        onChange={(e) => {
          handleSearchFilterPropertyChange("codigoPropio", e.target.value)
        }}
        value={searchFilter.codigoPropio}
        placeholder="Código propio"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const renderTextSearch = () => {
    return (
      <InputText
        id="searchText"
        autoFocus
        onChange={(e) => {
          handleSearchFilterPropertyChange("txt", e.target.value)
        }}
        value={searchFilter.txt}
        placeholder="Términos de búsqueda"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const renderAdvancedSearchOptions = () => {
    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-12 p-lg-2">
          <BrandsSelector
            onBrandSelect={(brand) => {
              handleSearchFilterPropertyChange("idMarca", _.get(brand, "brandId"))
            }}
          />
        </div>
        <div className="p-col-12 p-lg-2">
          <CategoriesSelector
            onCategorySelect={(category) => {
              handleSearchFilterPropertyChange(
                "idRubro",
                _.get(category, "categoryId")
              )
            }}
          />
        </div>
        <div className="p-col-12 p-lg-2">
          <SubCategoriesSelector
            onSubCategorySelect={(subCategory) => {
              handleSearchFilterPropertyChange(
                "idSubRubro",
                _.get(subCategory, "subCategoryId")
              )
            }}
            categoryId={searchFilter.idRubro}
          />
        </div>
        <div className="p-col-12 p-lg-1">{renderIdSearch(searchFilter)}</div>
        <div className="p-col-12 p-lg-1">
          {renderEnabledSearchCheckbox(searchFilter)}
        </div>
        <div className="p-col-12 p-lg-6">{renderSupplierSearch()}</div>
      </div>
    )
  }

  const renderEnabledSearchCheckbox = () => {
    return (
      <>
        <TriStateCheckbox
          id="activoCheck"
          onChange={(e) => {
            handleSearchFilterPropertyChange("activo", e.value)
          }}
          value={searchFilter.activo}
        />
        <label htmlFor="activoCheck" className="p-checkbox-label">
          {"Activos"}
        </label>
      </>
    )
  }

  const renderIdSearch = () => {
    return (
      <InputText
        id="idInput"
        onChange={(e) => {
          handleSearchFilterPropertyChange("idProducto", e.target.value)
        }}
        value={searchFilter.idProducto}
        placeholder="Id del producto"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const renderSearchButton = () => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-12 p-lg-12">
          <LoadingButton
            type="button"
            icon="fa fa-fw fa-filter"
            loading={loading}
            label={"Buscar"}
            onClick={() => searchProductsCallback(searchFilter)}
          />
        </div>
      </div>
    )
  }

  const handleSearchFilterPropertyChange = (property, value) => {
    let newSearchFilter = Object.assign({}, searchFilter)

    newSearchFilter[property] = value

    if (property === "idRubro") {
      newSearchFilter.idSubRubro = null
    }

    setSearchFilter(newSearchFilter)
  }

  const handleEnterKeyPress = (event) => {
    if (event.key === "Enter") {
      searchProductsCallback(searchFilter)
    }
  }

  const renderSupplierSearch = () => {
    return (
      <AutocompleteSupplierFilter
        onSupplierSelect={(supplier) => {
          handleSearchFilterPropertyChange(
            "idProveedorHabitual",
            _.get(supplier, "personId")
          )
        }}
      />
    )
  }

  return (
    <div>
      <TabView
        activeIndex={activeSearchTab}
        onTabChange={(e) => setActiveSearchTab(e.index)}
      >
        <TabPanel header="Básico" leftIcon="fa fa-fw fa-search">
          {renderBasicSearchOptions()}
        </TabPanel>
        <TabPanel header="Avanzado" leftIcon="fa fa-fw fa-cog">
          {renderAdvancedSearchOptions()}
        </TabPanel>
      </TabView>
      {renderSearchButton()}
    </div>
  )
}
