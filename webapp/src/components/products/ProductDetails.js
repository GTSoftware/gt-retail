import _ from "lodash"
import React, { useEffect, useRef, useState } from "react"
import { ProductsService } from "../../service/ProductsService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import { Toast } from "primereact/toast"
import { InputText } from "primereact/inputtext"
import { Checkbox } from "primereact/checkbox"
import { InputTextarea } from "primereact/inputtextarea"
import {
  fieldRequiredDefaultMessage,
  invalidPatternMessage,
} from "../../custom-error-form.messages"
import { FieldError, Form } from "react-jsonschema-form-validation"
import Field from "react-jsonschema-form-validation/dist/Field"
import { FiscalTaxRateSelector } from "../core/FiscalTaxRateSelector"
import { v4 as uuid } from "uuid"
import { PercentTypesSelector } from "../core/PercentTypesSelector"
import { isNotEmpty } from "../../utils/StringUtils"
import { PriceListSelector } from "../core/PriceListSelector"
import { LoadingButton } from "../core/LoadingButton"
import { ProductSupplyTypeSelector } from "../core/ProductSupplyTypeSelector"
import { CategoriesSelector } from "../core/CategoriesSelector"
import { SubCategoriesSelector } from "../core/SubCategoriesSelector"
import { BrandsSelector } from "../core/BrandsSelector"
import { ProductUnitTypeSelector } from "../core/ProductUnitTypeSelector"
import { AutocompleteSupplierFilter } from "../core/AutocompleteSupplierFilter"

const productSchema = {
  type: "object",
  properties: {
    code: {
      type: "string",
      minLength: 1,
      maxLength: 11,
      pattern: "^[A-Z0-9-._/]*$",
    },
    description: { type: "string", maxLength: 200 },
    factoryCode: { type: "string", maxLength: 60 },
    observations: { type: "string", maxLength: 2048 },
    location: { type: "string", maxLength: 60 },
    grossCost: { pattern: "^(-)?(?!0\\d)\\d*(\\.\\d{1,4})?$" },
    purchaseUnitsToSaleUnitEquivalence: { pattern: "^(?!0\\d)\\d*(\\.\\d{1,2})?$" },
    fiscalTaxRate: { type: "object" },
    salePrices: { type: "array" },
    percentages: { type: "array" },
    supplyType: { type: "object" },
    category: { type: "object" },
    subCategory: { type: "object" },
    brand: { type: "object" },
    purchaseUnit: { type: "object" },
    saleUnit: { type: "object" },
    regularSupplier: { type: "object" },
    minimumStock: { pattern: "^(?!0\\d)\\d*(\\.\\d{1,2})?$" },
  },
  required: [
    "description",
    "code",
    "grossCost",
    "fiscalTaxRate",
    "salePrices",
    "supplyType",
    "category",
    "subCategory",
    "brand",
    "purchaseUnitsToSaleUnitEquivalence",
    "purchaseUnit",
    "saleUnit",
    "minimumStock",
  ],
}

const NEW_PRODUCT = {
  productId: null,
  code: "",
  description: "",
  factoryCode: "",
  observations: "",
  location: "",
  grossCost: 0,
  purchaseUnitsToSaleUnitEquivalence: 1,
  fiscalTaxRate: null,
  salePrices: [],
  percentages: [],
  supplyType: null,
  category: null,
  subCategory: null,
  brand: null,
  purchaseUnit: null,
  saleUnit: null,
  regularSupplier: null,
  minimumStock: 0,
  activo: true,
}

export const ProductDetails = (props) => {
  const productsService = new ProductsService()

  const [loading, setLoading] = useState(false)
  const [productId, setProductId] = useState(props.match.params.productId)
  const [editingProduct, setEditingProduct] = useState(null)

  const [formData, setFormData] = useState(NEW_PRODUCT)

  const toast = useRef(null)

  useEffect(() => {
    if (productId) {
      productsService.getProduct(
        productId,
        handleGetProductInfo,
        handleGetProductError
      )
    }
  }, [productId])

  const getTitle = () => {
    if (productId) {
      return `Edición de producto: ${productId}`
    }
    return "Nuevo producto"
  }

  const handleSubmit = () => {
    setLoading(true)

    if (productId) {
      productsService.updateProduct(formData, handleSuccess, handleError)
    } else {
      productsService.createProduct(formData, handleCreationSuccess, handleError)
    }
  }

  const handleSuccess = () => {
    toast.current.show({
      severity: "info",
      summary: `El producto ${productId} fue editado exitosamente`,
    })

    productsService.getProduct(
      productId,
      (productInfo) => handleGetProductInfo(productInfo),
      handleGetProductError
    )
  }

  const handleCreationSuccess = (productInfo) => {
    const { productId } = productInfo

    toast.current.show({
      severity: "info",
      summary: `El producto ${productId} fue creado exitosamente`,
    })

    setProductId(productId)
  }

  const handleError = (error) => {
    setLoading(false)
    toast.current.show({
      severity: "error",
      summary: "Ocurrió un error al actualizar el producto",
      detail: error.message,
    })
  }
  const handleGetProductError = (error) => {
    toast.current.show({
      severity: "error",
      summary: `No se ha podido encontrar el producto [${error.errorCode}]`,
      detail: _.get(error, "message", ""),
    })
  }
  const renderIdentificationSection = () => {
    const { description, code, factoryCode, observations, active } = formData
    return (
      <div className="p-card-body p-fluid p-grid">
        <label className="p-col-3 ">{"Id:"}</label>
        <div className="p-col-9">{productId}</div>
        <div className="p-col-3">{"Código:"}</div>
        <div className="p-col-9">
          <Field
            id="code"
            component={InputText}
            name="code"
            value={code}
            onBlur={validateProductCode}
          />
          <FieldError name="code" />
        </div>
        <div className="p-col-3">{"Descripción:"}</div>
        <div className="p-col-9">
          <Field
            id="description"
            component={InputText}
            name="description"
            value={description}
          />
          <FieldError name="description" />
        </div>
        <div className="p-col-3">{"Código de fábrica:"}</div>
        <div className="p-col-9">
          <Field
            id="factoryCode"
            component={InputText}
            name="factoryCode"
            value={factoryCode}
          />
          <FieldError name="factoryCode" />
        </div>
        <div className="p-col-3">{"Observaciones:"}</div>
        <div className="p-col-9">
          <Field
            id="observations"
            component={InputTextarea}
            name="observations"
            value={observations || ""}
          />
          <FieldError name="observations" />
        </div>

        <div className="p-col-3">{"Activo:"}</div>
        <div className="p-col-9">
          <Field id="active" component={Checkbox} name="active" checked={active} />
        </div>
      </div>
    )
  }

  const renderPricingSection = () => {
    const { grossCost, fiscalTaxRate, netCost } = formData
    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Costo Bruto:"}</div>
        <div className="p-col-3">
          <Field
            id="grossCost"
            component={InputText}
            name="grossCost"
            value={grossCost}
          />
          <FieldError name="grossCost" />
        </div>
        <div className="p-col-3">{"Tasa de IVA:"}</div>
        <div className="p-col-3">
          <FiscalTaxRateSelector
            selectedTaxRate={fiscalTaxRate}
            onTaxRateSelect={(taxRate) =>
              handleProductPropertyChange("fiscalTaxRate", taxRate)
            }
          />
          <FieldError name="fiscalTaxRate" />
        </div>
        <div className="p-col-12">{renderPercentsTable()}</div>
        <div className="p-col-6">
          <label className="p-col-3">{"Costo Neto:"}</label>
          <label className="p-col-3">{netCost}</label>
        </div>
        <div className="p-col-12">{renderPricesTable()}</div>
      </div>
    )
  }

  const renderPercentsTable = () => {
    const { percentages } = formData

    return (
      <DataTable
        header={renderPercentTableHeader()}
        value={percentages}
        resizableColumns={true}
        responsive={true}
        editMode={"cell"}
      >
        <Column
          field="percentType.displayName"
          header="Tipo"
          editor={(props) => getPercentTypeEditor(props)}
        />
        <Column
          field="rate"
          header="Valor"
          editor={(props) => getPercentValueEditor(props)}
          editorValidator={numberValidator}
        />
        <Column
          key="actions"
          body={(rowData) => getPercentsTableActions(rowData)}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  const renderPricesTable = () => {
    const { salePrices } = formData

    return (
      <DataTable
        header={renderPriceTableHeader()}
        value={salePrices}
        resizableColumns={true}
        responsive={true}
        editMode={"cell"}
      >
        <Column
          field="priceList.displayName"
          header="Lista"
          editor={(props) => getPriceListEditor(props)}
        />
        <Column
          field="utility"
          header="Utilidad"
          editor={(props) => getUtilityValueEditor(props)}
          editorValidator={numberValidator}
        />
        <Column field="netPrice" header="Precio Neto" />
        <Column
          field="finalPrice"
          header="Precio Final"
          editor={(props) => getFinalPriceEditor(props)}
          editorValidator={numberValidator}
        />
        <Column
          key="actions"
          body={(rowData) => getPriceTableActions(rowData)}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  const renderClassificationSection = () => {
    const {
      supplyType,
      category,
      subCategory,
      brand,
      saleUnit,
      purchaseUnit,
      purchaseUnitsToSaleUnitEquivalence,
      regularSupplier,
    } = formData

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Tipo de producto:"}</div>
        <div className="p-col-9">
          <ProductSupplyTypeSelector
            selectedSupplyType={supplyType}
            onSupplyTypeSelect={(supplyType) =>
              handleProductPropertyChange("supplyType", supplyType)
            }
          />
          <FieldError name="supplyType" />
        </div>

        <div className="p-col-3">{"Proveedor habitual:"}</div>
        <div className="p-col-9">
          <AutocompleteSupplierFilter
            selectedSupplier={regularSupplier}
            onSupplierSelect={(supplier) =>
              handleProductPropertyChange("regularSupplier", supplier)
            }
          />
          <FieldError name="regularSupplier" />
        </div>

        <div className="p-col-3">{"Rubro:"}</div>
        <div className="p-col-9">
          <CategoriesSelector
            selectedCategory={category}
            onCategorySelect={(category) =>
              handleProductPropertyChange("category", category)
            }
          />
          <FieldError name="category" />
        </div>
        <div className="p-col-3">{"Sub-Rubro:"}</div>
        <div className="p-col-9">
          {category && (
            <SubCategoriesSelector
              categoryId={category.categoryId}
              selectedSubCategory={subCategory}
              onSubCategorySelect={(subCategory) =>
                handleProductPropertyChange("subCategory", subCategory)
              }
            />
          )}
          <FieldError name="subCategory" />
        </div>

        <div className="p-col-3">{"Marca:"}</div>
        <div className="p-col-9">
          <BrandsSelector
            selectedBrand={brand}
            onBrandSelect={(brand) => handleProductPropertyChange("brand", brand)}
          />
          <FieldError name="brand" />
        </div>

        <div className="p-col-3">{"Unidad de compra:"}</div>
        <div className="p-col-9">
          <ProductUnitTypeSelector
            selectedUnitType={purchaseUnit}
            onSelectUnitType={(purchaseUnit) =>
              handleProductPropertyChange("purchaseUnit", purchaseUnit)
            }
          />
          <FieldError name="purchaseUnit" />
        </div>

        <div className="p-col-3">{"Unidad de venta:"}</div>
        <div className="p-col-9">
          <ProductUnitTypeSelector
            selectedUnitType={saleUnit}
            onSelectUnitType={(saleUnit) =>
              handleProductPropertyChange("saleUnit", saleUnit)
            }
          />
          <FieldError name="saleUnit" />
        </div>

        <div className="p-col-3">{"Equivalencia un. compra por un. venta:"}</div>
        <div className="p-col-3">
          <Field
            id="purchaseUnitsToSaleUnitEquivalence"
            component={InputText}
            name="purchaseUnitsToSaleUnitEquivalence"
            value={purchaseUnitsToSaleUnitEquivalence}
          />
          <FieldError name="purchaseUnitsToSaleUnitEquivalence" />
        </div>
      </div>
    )
  }

  const renderStockSection = () => {
    const { minimumStock } = formData

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Stock mínimo:"}</div>
        <div className="p-col-3">
          <Field
            id="minimumStock"
            component={InputText}
            name="minimumStock"
            value={minimumStock}
          />
          <FieldError name="minimumStock" />
        </div>
      </div>
    )
  }

  const getPercentTypeEditor = (props) => {
    const { rowData } = props

    return (
      <PercentTypesSelector
        selectedItem={rowData["percentType"]}
        onChange={(percentType) =>
          handlePercentChange(rowData, "percentType", percentType)
        }
      />
    )
  }

  const getUtilityValueEditor = (props) => {
    const { rowData } = props

    return (
      <InputText
        value={rowData["utility"]}
        onChange={(e) => handleSalePriceChange(rowData, "utility", e.target.value)}
      />
    )
  }

  const getFinalPriceEditor = (props) => {
    const { rowData } = props
    const finalPrice = "finalPrice"

    return (
      <InputText
        value={rowData[finalPrice]}
        onChange={(e) => handleSalePriceChange(rowData, finalPrice, e.target.value)}
      />
    )
  }

  const getPercentValueEditor = (props) => {
    const { rowData } = props

    return (
      <InputText
        value={rowData["rate"]}
        onChange={(e) => handlePercentChange(rowData, "rate", e.target.value)}
      />
    )
  }

  const handlePercentChange = (percent, field, value) => {
    let percentages = Object.assign([], formData.percentages)
    const index = percentages.findIndex((item) => item.uid === percent.uid)

    percentages[index][field] = value

    handleProductPropertyChange("percentages", percentages)
  }

  const numberValidator = (e) => {
    const { rowData, field } = e.columnProps
    const value = String(rowData[field])

    return isNotEmpty(value) && value.match("^(-)?(?!0\\d)\\d*(\\.\\d{1,4})?$")
  }

  const getPercentsTableActions = (rowData) => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-6">
          <Button
            type="button"
            icon="fa fa-fw fa-trash"
            className="p-button-danger"
            onClick={() => removePercent(rowData)}
            tooltip={"Quitar ítem"}
          />
        </div>
      </div>
    )
  }

  const removePercent = (productPercent) => {
    let percentages = Object.assign([], formData.percentages)

    _.remove(percentages, (item) => {
      return item.uid === productPercent.uid
    })

    handleProductPropertyChange("percentages", percentages)
  }

  const addPercent = () => {
    let percentages = Object.assign([], formData.percentages)
    const newPercent = {
      uid: uuid(),
      rate: "",
      percentType: { displayName: "" },
    }

    percentages.push(newPercent)

    handleProductPropertyChange("percentages", percentages)
  }

  const getPriceListEditor = (props) => {
    const { rowData } = props
    const fieldName = "priceList"

    return (
      <PriceListSelector
        selectedItem={rowData[fieldName]}
        onChange={(percentType) =>
          handleSalePriceChange(rowData, fieldName, percentType)
        }
      />
    )
  }

  const handleSalePriceChange = (salePrice, field, value) => {
    let salePrices = Object.assign([], formData.salePrices)
    const index = salePrices.findIndex((item) => item.uid === salePrice.uid)

    salePrices[index][field] = value

    if (field === "finalPrice") {
      const taxRate = parseFloat(formData.fiscalTaxRate.rate) / 100 + 1
      const netCost = parseFloat(formData.netCost)
      const salePrice = parseFloat(value)
      const utility = ((salePrice / taxRate - netCost) / netCost) * 100

      salePrices[index].utility = utility.toFixed(4)
    }

    handleProductPropertyChange("salePrices", salePrices)
  }

  const removePrice = (salePrice) => {
    let prices = Object.assign([], formData.salePrices)

    _.remove(prices, (item) => {
      return item.uid === salePrice.uid
    })

    handleProductPropertyChange("salePrices", prices)
  }

  const addSalePrice = () => {
    let salePrices = Object.assign([], formData.salePrices)
    const newSalePrice = {
      uid: uuid(),
      utility: "",
      finalPrice: "",
      netPrice: "",
      priceList: { displayName: "" },
    }

    salePrices.push(newSalePrice)

    handleProductPropertyChange("salePrices", salePrices)
  }

  const getPriceTableActions = (rowData) => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-6">
          <Button
            type="button"
            icon="fa fa-fw fa-trash"
            className="p-button-danger"
            onClick={() => removePrice(rowData)}
            tooltip={"Quitar ítem"}
          />
        </div>
      </div>
    )
  }

  const handleProductPropertyChange = (property, value) => {
    let product = { ...formData }

    product[property] = value

    if (property === "category") {
      product.subCategory = null
    }

    handleChange(product)
  }

  const handleGetProductInfo = (productInfo) => {
    setEditingProduct(productInfo)

    let { percentages, salePrices } = productInfo

    percentages.forEach((percent) => {
      percent.uid = uuid()
    })
    salePrices.forEach((salePrice) => {
      salePrice.uid = uuid()
    })

    setLoading(false)
    setFormData({ ...formData, ...productInfo })
  }

  const renderPercentTableHeader = () => {
    return (
      <div className="p-fluid p-grid">
        <div className="p-col-8">
          <div>{"Porcentajes"}</div>
        </div>
        <div className="p-col-4">
          <Button
            label="Agregar porcentaje"
            icon="fa fa-fw fa-plus"
            className="p-button-success"
            type="button"
            onClick={addPercent}
          />
        </div>
      </div>
    )
  }

  const renderPriceTableHeader = () => {
    return (
      <div className="p-fluid p-grid">
        <div className="p-col-8">
          <div>{"Precios"}</div>
        </div>
        <div className="p-col-4">
          <Button
            label="Agregar precio"
            icon="fa fa-fw fa-plus"
            className="p-button-success"
            type="button"
            onClick={addSalePrice}
          />
        </div>
      </div>
    )
  }

  const updatePrices = (product) => {
    const taxRate = parseFloat(_.get(product, "fiscalTaxRate.rate", "0")) / 100 + 1
    const percentages = product.percentages
    const salePrices = product.salePrices
    let netCost = parseFloat(product.grossCost)

    percentages.forEach((p) => {
      if (p.percentType.percent) {
        netCost += netCost * (p.rate / 100)
      } else {
        netCost += p.value
      }
    })

    product.netCost = netCost.toFixed(4)

    salePrices.forEach((salePrice) => {
      const utility = parseFloat(salePrice.utility) / 100
      const netPrice = netCost + netCost * utility

      salePrice.netPrice = netPrice.toFixed(4)
      salePrice.finalPrice = (netPrice * taxRate).toFixed(4)
    })
  }

  const validateProductCode = () => {
    if (formData.code) {
      productsService.validateProductCode(formData, (error) =>
        handleDuplicatedCodeError(error)
      )
    }
  }

  const handleDuplicatedCodeError = (error) => {
    toast.current.show({
      severity: "error",
      summary: `Código duplicado [${error.errorCode}]`,
      detail: error.message,
    })
  }

  const handleChange = (newData) => {
    updatePrices(newData)
    setFormData(newData)
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>{getTitle()}</h1>

      <Form
        data={formData}
        onChange={handleChange}
        schema={productSchema}
        onSubmit={handleSubmit}
        errorMessages={{
          required: () => fieldRequiredDefaultMessage,
          pattern: () => invalidPatternMessage,
        }}
      >
        <div className="card ">
          <h1 style={{ fontSize: "16px" }}>Identificación</h1>
          {renderIdentificationSection()}
        </div>

        <div className="card ">
          <h1 style={{ fontSize: "16px" }}>Precios</h1>
          {renderPricingSection()}
        </div>

        <div className="card ">
          <h1 style={{ fontSize: "16px" }}>Clasificación</h1>
          {renderClassificationSection()}
        </div>

        <div className="card ">
          <h1 style={{ fontSize: "16px" }}>Stock</h1>
          {renderStockSection()}
        </div>

        <LoadingButton
          type="submit"
          label="Guardar"
          loading={loading}
          icon="fa fa-fw fa-save"
        />
        <Button
          type="button"
          label="Cerrar"
          className="p-button-secondary"
          icon="fa fa-fw fa-arrow-left"
          onClick={() => {
            window.close()
          }}
        />
      </Form>
    </div>
  )
}
