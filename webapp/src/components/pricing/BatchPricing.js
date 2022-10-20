import _ from "lodash"
import React, { useEffect, useRef, useState } from "react"
import { ProductsService } from "../../service/ProductsService"
import { Toast } from "primereact/toast"
import { Dropdown } from "primereact/dropdown"
import { Button } from "primereact/button"
import { SearchProductsTable } from "../core/SearchProductsTable"
import { InputNumber } from "primereact/inputnumber"
import { Checkbox } from "primereact/checkbox"
import { LoadingButton } from "../core/LoadingButton"
import { AddPercentDialog } from "./AddPercentDialog"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { InputText } from "primereact/inputtext"
import { BrandsSelector } from "../core/BrandsSelector"
import { AutocompleteSupplierFilter } from "../core/AutocompleteSupplierFilter"
import { CategoriesSelector } from "../core/CategoriesSelector"
import { SubCategoriesSelector } from "../core/SubCategoriesSelector"
import { v4 as uuid } from "uuid"

export const BatchPricing = () => {
  const [supplyTypes, setSupplyTypes] = useState([])
  const [loading, setLoading] = useState(false)
  const [productsSearchOptions, setProductsSearchOptions] = useState({
    supplyType: null,
    category: null,
    subCategory: null,
    brand: null,
    supplier: null,
    containsText: "",
  })
  const [filteredProducts, setFilteredProducts] = useState([])
  const [totalResults, setTotalResults] = useState(0)
  const [updateOptions, setUpdateOptions] = useState({
    updateCost: false,
    costPercent: 0,
    updatePercents: false,
    percentsToAdd: [],
    percentsToDelete: [],
  })
  const [updatingPrices, setUpdatingPrices] = useState(false)
  const [showAddPercentDialog, setShowAddPercentDialog] = useState(false)
  const productsService = new ProductsService()
  const toast = useRef(null)

  useEffect(() => {
    productsService.getSupplyTypes((supplyTypes) => setSupplyTypes(supplyTypes))
  }, [])

  const renderFilterSection = () => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-12">
          <label htmlFor="supplyType">{"Tipo de proveeduría:"}</label>
          <Dropdown
            id="supplyType"
            dataKey="supplyTypeId"
            options={supplyTypes}
            showClear={true}
            value={productsSearchOptions.supplyType}
            optionLabel="displayName"
            onChange={(e) =>
              handleProductsSearchOptionsChange("supplyType", e.value)
            }
          />
        </div>

        <div className="p-col-12">
          <label htmlFor="supplier">{"Proveedor:"}</label>
          <AutocompleteSupplierFilter
            onSupplierSelect={(supplier) =>
              handleProductsSearchOptionsChange("supplier", supplier)
            }
          />
        </div>

        <div className="p-col-12">
          <label htmlFor="brand">Marca:</label>
          <BrandsSelector
            onBrandSelect={(brand) =>
              handleProductsSearchOptionsChange("brand", brand)
            }
          />
        </div>

        <div className="p-col-6">
          <label htmlFor="category">{"Rubro:"}</label>
          <CategoriesSelector
            onCategorySelect={(category) => {
              handleProductsSearchOptionsChange("category", category)
            }}
          />
        </div>
        <div className="p-col-6">
          <label htmlFor="subCategory">Sub-Rubro:</label>
          <SubCategoriesSelector
            onSubCategorySelect={(subCat) =>
              handleProductsSearchOptionsChange("subCategory", subCat)
            }
            categoryId={_.get(productsSearchOptions, "category.categoryId")}
          />
        </div>
        <div className="p-col-12">
          <label htmlFor="containsText">{"Contiene:"}</label>
          <InputText
            placeholder="La descripción contiene"
            id="containsText"
            onChange={(e) =>
              handleProductsSearchOptionsChange("containsText", e.target.value)
            }
            value={productsSearchOptions.containsText || ""}
          />
        </div>

        <div className="p-col-6">
          <LoadingButton
            label="Aplicar filtros"
            icon="fa fa-fw fa-check"
            loading={loading}
            onClick={handleApplyFilters}
          />
        </div>
      </div>
    )
  }

  const renderProductsPreview = () => {
    return (
      <SearchProductsTable
        onPageEvent={handlePageChange}
        emptyMessage="Aplique filtros para visualizar los productos"
        rows={5}
        totalRecords={totalResults}
        products={filteredProducts}
        showAdditionalColumns={true}
      />
    )
  }

  const renderUpdateParametersSection = () => {
    const { updateCost, costPercent, updatePercents } = updateOptions

    return (
      <div className="p-grid p-fluid">
        {showAddPercentDialog && renderAddPercentDialog()}
        <div className="p-col-6">
          <label htmlFor="updateCostField">{"Actualizar costo:"}</label>
          <div className="p-inputgroup">
            <span className="p-inputgroup-addon">
              <Checkbox
                id="updateCost"
                onChange={(e) => {
                  handleUpdateOptionsChange("updateCost", e.checked)
                }}
                tooltip={"Habilita la actualización del costo"}
                checked={updateCost}
              />
            </span>
            <InputNumber
              id="updateCostField"
              placeholder="Costo"
              maxFractionDigits={4}
              minFractionDigits={2}
              prefix="%"
              showButtons
              mode="decimal"
              disabled={!updateCost}
              value={costPercent}
              onChange={(e) => {
                handleUpdateOptionsChange("costPercent", e.value)
              }}
            />
          </div>
        </div>

        <div className="p-col-6" />

        <div className="p-col-6">
          <label htmlFor="updatePercent">{"Actualizar porcentajes:"}</label>
          <Checkbox
            id="updatePercent"
            onChange={(e) => {
              handleUpdateOptionsChange("updatePercents", e.checked)
            }}
            tooltip={"Habilita la actualización de porcentajes"}
            checked={updatePercents}
          />
        </div>

        <div className="p-col-12">
          <Button
            label="Agregar porcentaje"
            icon="fa fa-fw fa-plus"
            disabled={!updatePercents}
            onClick={() => setShowAddPercentDialog(true)}
          />
        </div>
        <div className="p-col-6">
          {renderPercentsTable("Para agregar", "percentsToAdd")}
        </div>
        <div className="p-col-6">
          {renderPercentsTable("Para borrar", "percentsToDelete")}
        </div>

        <div className="p-col-6">
          <LoadingButton
            label="Aplicar cambios"
            icon="fa fa-fw fa-calculator"
            className="p-button-success"
            disabled={!(updateCost || updatePercents)}
            loading={updatingPrices}
            onClick={handleApplyChanges}
          />
        </div>
      </div>
    )
  }

  const renderAddPercentDialog = () => {
    return (
      <AddPercentDialog
        visible={showAddPercentDialog}
        modal={true}
        acceptCallback={handleAddPercent}
        onHide={() => setShowAddPercentDialog(false)}
      />
    )
  }

  const handleAddPercent = (percent) => {
    let percentItem = { ...percent }

    percentItem.item = uuid()
    if (percentItem.action === "add") {
      updateOptions.percentsToAdd.splice(0, 0, percentItem)
    } else {
      updateOptions.percentsToDelete.splice(0, 0, percentItem)
    }

    setUpdateOptions(updateOptions)
  }

  const handleProductsSearchOptionsChange = (property, value) => {
    let newProductsSearchOptions = { ...productsSearchOptions }

    newProductsSearchOptions[property] = value

    if (property === "category") {
      newProductsSearchOptions.subCategory = null
    }

    setProductsSearchOptions(newProductsSearchOptions)
  }

  const handleUpdateOptionsChange = (property, value) => {
    let newUpdateOptions = { ...updateOptions }

    newUpdateOptions[property] = value

    setUpdateOptions(newUpdateOptions)
  }

  const handleApplyFilters = () => {
    let searchOptions = {
      firstResult: 0,
      maxResults: 5,
      searchFilter: getSearchFilter(),
    }

    productsService.searchProducts(searchOptions, (response) => {
      setFilteredProducts(response.data)
      setTotalResults(response.totalResults)
      setLoading(false)
    })

    setLoading(false)
  }

  const handlePageChange = (pageOptions) => {
    let searchOptions = {
      firstResult: pageOptions.firstResult,
      maxResults: 5,
      searchFilter: getSearchFilter(),
    }

    productsService.searchProducts(searchOptions, (response) => {
      setFilteredProducts(response.data)
      setTotalResults(response.totalResults)
    })
  }

  const getSearchFilter = () => {
    return {
      activo: true,
      idTipoProveeduria: _.get(
        productsSearchOptions,
        "supplyType.supplyTypeId",
        null
      ),
      idRubro: _.get(productsSearchOptions, "category.categoryId", null),
      idSubRubro: _.get(productsSearchOptions, "subCategory.subCategoryId", null),
      idMarca: _.get(productsSearchOptions, "brand.brandId", null),
      idProveedorHabitual: _.get(productsSearchOptions, "supplier.personId", null),
      txt: productsSearchOptions.containsText,
      sortFields: [
        {
          fieldName: "descripcion",
          ascending: true,
        },
      ],
    }
  }

  const handleApplyChanges = () => {
    setUpdatingPrices(true)

    updateOptions.searchFilter = getSearchFilter()

    productsService.updateProductsPricing(updateOptions, handleSuccess, handleError)
  }

  const handleSuccess = () => {
    setUpdatingPrices(false)
    let newUdateOptions = { ...updateOptions }
    newUdateOptions.updatePercents = false
    newUdateOptions.updateCost = false
    setUpdateOptions(newUdateOptions)

    toast.current.show({
      severity: "info",
      summary: "Los precios serán actualizados en segundo plano.",
      detail: "",
    })
  }

  const handleError = (error) => {
    setUpdatingPrices(false)

    toast.current.show({
      severity: "error",
      summary: "No se pudieron actualizar los precios",
      detail: _.get(error, "response.data.message", ""),
    })
  }

  const renderPercentsTable = (header, percentsProperty) => {
    const percents = updateOptions[percentsProperty]

    return (
      <DataTable
        header={header}
        value={percents}
        resizableColumns={true}
        responsive={true}
      >
        <Column field="percentType.displayName" header="Tipo" />
        <Column field="value" header="Valor" />
        <Column
          key="actions"
          body={(rowData) => getRemoveAction(rowData, percentsProperty)}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  const getRemoveAction = (rowData, percentsProperty) => {
    return (
      <Button
        type="button"
        icon="fa fa-fw fa-trash"
        className="p-button-danger"
        onClick={() => removePercent(rowData, percentsProperty)}
        tooltip={"Quitar ítem"}
      />
    )
  }

  const removePercent = (rowData, percentsProperty) => {
    let newUpdateOptions = { ...updateOptions }
    let percents = newUpdateOptions[percentsProperty]

    _.remove(percents, (item) => {
      return item.item === rowData.item
    })
    setUpdateOptions(newUpdateOptions)
  }

  return (
    <div className="card card-w-title">
      <h1>Actualización masiva de precios</h1>
      <Toast ref={toast} />
      {renderFilterSection()}
      {renderProductsPreview()}
      <h3>Productos a modificar: {totalResults}</h3>

      <div className="SeparatorFull" />
      {totalResults > 0 && renderUpdateParametersSection()}
    </div>
  )
}
