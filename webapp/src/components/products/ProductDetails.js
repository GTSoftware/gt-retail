import _ from "lodash"
import React, { Component } from "react"
import { ProductsService } from "../../service/ProductsService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import PropTypes from "prop-types"
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

const newProduct = {
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
}

export class ProductDetails extends Component {
  static propTypes = {
    match: PropTypes.shape({
      params: PropTypes.shape({
        productId: PropTypes.string.isRequired,
      }),
    }),
  }

  constructor(props, context) {
    super(props, context)

    this.state = {
      productId: props.match.params.productId,
      product: null,
      loading: true,
      activeTab: 0,
    }

    this.productsService = new ProductsService()
  }

  componentDidMount() {
    const { product, productId } = this.state

    if (!product) {
      if (productId === "new") {
        this.setState({ product: newProduct, loading: false, productId: null })
      } else {
        this.productsService.getProduct(
          productId,
          (productInfo) => this.handleGetProductInfo(productInfo),
          this.handleGetProductError
        )
      }
    }
  }

  render() {
    const { product } = this.state

    return (
      <div className="card card-w-title">
        <Toast ref={(el) => (this.toast = el)} />
        <h1>{this.getTitle()}</h1>
        {product && (
          <Form
            data={product}
            onChange={(formData) => {
              this.updatePrices(formData)
              this.setState({ product: formData })
            }}
            schema={productSchema}
            onSubmit={this.handleSubmit}
            errorMessages={{
              required: () => fieldRequiredDefaultMessage,
              pattern: () => invalidPatternMessage,
            }}
          >
            <div className="card ">
              <h1 style={{ fontSize: "16px" }}>Identificación</h1>
              {this.renderIdentificationSection()}
            </div>

            <div className="card ">
              <h1 style={{ fontSize: "16px" }}>Precios</h1>
              {this.renderPricingSection()}
            </div>

            <div className="card ">
              <h1 style={{ fontSize: "16px" }}>Clasificación</h1>
              {this.renderClassificationSection()}
            </div>

            <div className="card ">
              <h1 style={{ fontSize: "16px" }}>Stock</h1>
              {this.renderStockSection()}
            </div>

            <LoadingButton
              type="submit"
              label="Guardar"
              loading={this.state.loading}
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
        )}
      </div>
    )
  }

  handleSubmit = () => {
    const { product } = this.state

    this.setState({ loading: true })

    if (product.productId) {
      this.productsService.updateProduct(
        product,
        this.handleSuccess,
        this.handleError
      )
    } else {
      this.productsService.createProduct(
        product,
        this.handleCreationSuccess,
        this.handleError
      )
    }
  }

  handleSuccess = () => {
    const { productId } = this.state

    this.toast.show({
      severity: "info",
      summary: `El producto ${productId} fue editado exitosamente`,
    })

    this.productsService.getProduct(
      productId,
      (productInfo) => this.handleGetProductInfo(productInfo),
      this.handleGetProductError
    )
  }

  handleCreationSuccess = (productInfo) => {
    const { productId } = productInfo

    this.toast.show({
      severity: "info",
      summary: `El producto ${productId} fue creado exitosamente`,
    })

    this.handleGetProductInfo(productInfo)
  }

  handleError = (error) => {
    this.setState({ loading: false })

    this.toast.show({
      severity: "error",
      summary: "Ocurrió un error al actualizar el producto",
      detail: error.message,
    })
  }

  getTitle = () => {
    const { productId } = this.state

    if (productId) {
      return `Edición de producto: ${productId}`
    }
    return "Nuevo producto"
  }

  handleGetProductError = (error) => {
    this.toast.show({
      severity: "error",
      summary: `No se ha podido encontrar el producto [${error.errorCode}]`,
      detail: _.get(error, "message", ""),
    })
  }

  renderIdentificationSection = () => {
    const { productId, description, code, factoryCode, observations, active } =
      this.state.product

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
            onBlur={this.validateProductCode}
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

  renderPricingSection = () => {
    const { grossCost, fiscalTaxRate, netCost } = this.state.product

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
              this.handleProductPropertyChange("fiscalTaxRate", taxRate)
            }
          />
          <FieldError name="fiscalTaxRate" />
        </div>
        <div className="p-col-12">{this.renderPercentsTable()}</div>
        <div className="p-col-6">
          <label className="p-col-3">{"Costo Neto:"}</label>
          <label className="p-col-3">{netCost}</label>
        </div>
        <div className="p-col-12">{this.renderPricesTable()}</div>
      </div>
    )
  }

  renderPercentsTable = () => {
    const { percentages } = this.state.product

    return (
      <DataTable
        header={this.renderPercentTableHeader()}
        value={percentages}
        resizableColumns={true}
        responsive={true}
        editMode={"cell"}
      >
        <Column
          field="percentType.displayName"
          header="Tipo"
          editor={(props) => this.getPercentTypeEditor(props)}
        />
        <Column
          field="rate"
          header="Valor"
          editor={(props) => this.getPercentValueEditor(props)}
          editorValidator={this.numberValidator}
        />
        <Column
          key="actions"
          body={(rowData) => this.getPercentsTableActions(rowData)}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  renderPricesTable = () => {
    const { salePrices } = this.state.product

    return (
      <DataTable
        header={this.renderPriceTableHeader()}
        value={salePrices}
        resizableColumns={true}
        responsive={true}
        editMode={"cell"}
      >
        <Column
          field="priceList.displayName"
          header="Lista"
          editor={(props) => this.getPriceListEditor(props)}
        />
        <Column
          field="utility"
          header="Utilidad"
          editor={(props) => this.getUtilityValueEditor(props)}
          editorValidator={this.numberValidator}
        />
        <Column field="netPrice" header="Precio Neto" />
        <Column
          field="finalPrice"
          header="Precio Final"
          editor={(props) => this.getFinalPriceEditor(props)}
          editorValidator={this.numberValidator}
        />
        <Column
          key="actions"
          body={(rowData) => this.getPriceTableActions(rowData)}
          style={{ textAlign: "center", width: "7em" }}
        />
      </DataTable>
    )
  }

  renderClassificationSection = () => {
    const {
      supplyType,
      category,
      subCategory,
      brand,
      saleUnit,
      purchaseUnit,
      purchaseUnitsToSaleUnitEquivalence,
      regularSupplier,
    } = this.state.product

    return (
      <div className="p-card-body p-fluid p-grid">
        <div className="p-col-3">{"Tipo de producto:"}</div>
        <div className="p-col-9">
          <ProductSupplyTypeSelector
            selectedSupplyType={supplyType}
            onSupplyTypeSelect={(supplyType) =>
              this.handleProductPropertyChange("supplyType", supplyType)
            }
          />
          <FieldError name="supplyType" />
        </div>

        <div className="p-col-3">{"Proveedor habitual:"}</div>
        <div className="p-col-9">
          <AutocompleteSupplierFilter
            selectedSupplier={regularSupplier}
            onSupplierSelect={(supplier) =>
              this.handleProductPropertyChange("regularSupplier", supplier)
            }
          />
          <FieldError name="regularSupplier" />
        </div>

        <div className="p-col-3">{"Rubro:"}</div>
        <div className="p-col-9">
          <CategoriesSelector
            selectedCategory={category}
            onCategorySelect={(category) =>
              this.handleProductPropertyChange("category", category)
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
                this.handleProductPropertyChange("subCategory", subCategory)
              }
            />
          )}
          <FieldError name="subCategory" />
        </div>

        <div className="p-col-3">{"Marca:"}</div>
        <div className="p-col-9">
          <BrandsSelector
            selectedBrand={brand}
            onBrandSelect={(brand) =>
              this.handleProductPropertyChange("brand", brand)
            }
          />
          <FieldError name="brand" />
        </div>

        <div className="p-col-3">{"Unidad de compra:"}</div>
        <div className="p-col-9">
          <ProductUnitTypeSelector
            selectedUnitType={purchaseUnit}
            onSelectUnitType={(purchaseUnit) =>
              this.handleProductPropertyChange("purchaseUnit", purchaseUnit)
            }
          />
          <FieldError name="purchaseUnit" />
        </div>

        <div className="p-col-3">{"Unidad de venta:"}</div>
        <div className="p-col-9">
          <ProductUnitTypeSelector
            selectedUnitType={saleUnit}
            onSelectUnitType={(saleUnit) =>
              this.handleProductPropertyChange("saleUnit", saleUnit)
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

  renderStockSection = () => {
    const { minimumStock } = this.state.product

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

  getPercentTypeEditor = (props) => {
    const { rowData } = props

    return (
      <PercentTypesSelector
        selectedItem={rowData["percentType"]}
        onChange={(percentType) =>
          this.handlePercentChange(rowData, "percentType", percentType)
        }
      />
    )
  }

  getUtilityValueEditor = (props) => {
    const { rowData } = props

    return (
      <InputText
        value={rowData["utility"]}
        onChange={(e) =>
          this.handleSalePriceChange(rowData, "utility", e.target.value)
        }
      />
    )
  }

  getFinalPriceEditor = (props) => {
    const { rowData } = props
    const finalPrice = "finalPrice"

    return (
      <InputText
        value={rowData[finalPrice]}
        onChange={(e) =>
          this.handleSalePriceChange(rowData, finalPrice, e.target.value)
        }
      />
    )
  }

  getPercentValueEditor = (props) => {
    const { rowData } = props

    return (
      <InputText
        value={rowData["rate"]}
        onChange={(e) => this.handlePercentChange(rowData, "rate", e.target.value)}
      />
    )
  }

  handlePercentChange = (percent, field, value) => {
    let percentages = Object.assign([], this.state.product.percentages)
    const index = percentages.findIndex((item) => item.uid === percent.uid)

    percentages[index][field] = value

    this.handleProductPropertyChange("percentages", percentages)
  }

  numberValidator = (e) => {
    const { rowData, field } = e.columnProps
    const value = String(rowData[field])

    return isNotEmpty(value) && value.match("^(-)?(?!0\\d)\\d*(\\.\\d{1,4})?$")
  }

  getPercentsTableActions = (rowData) => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-6">
          <Button
            type="button"
            icon="fa fa-fw fa-trash"
            className="p-button-danger"
            onClick={() => this.removePercent(rowData)}
            tooltip={"Quitar ítem"}
          />
        </div>
      </div>
    )
  }

  removePercent = (productPercent) => {
    let percentages = Object.assign([], this.state.product.percentages)

    _.remove(percentages, (item) => {
      return item.uid === productPercent.uid
    })

    this.handleProductPropertyChange("percentages", percentages)
  }

  addPercent = () => {
    let percentages = Object.assign([], this.state.product.percentages)
    const newPercent = {
      uid: uuid(),
      rate: "",
      percentType: { displayName: "" },
    }

    percentages.push(newPercent)

    this.handleProductPropertyChange("percentages", percentages)
  }

  getPriceListEditor = (props) => {
    const { rowData } = props
    const fieldName = "priceList"

    return (
      <PriceListSelector
        selectedItem={rowData[fieldName]}
        onChange={(percentType) =>
          this.handleSalePriceChange(rowData, fieldName, percentType)
        }
      />
    )
  }

  handleSalePriceChange = (salePrice, field, value) => {
    let salePrices = Object.assign([], this.state.product.salePrices)
    const index = salePrices.findIndex((item) => item.uid === salePrice.uid)

    salePrices[index][field] = value

    if (field === "finalPrice") {
      const taxRate = parseFloat(this.state.product.fiscalTaxRate.rate) / 100 + 1
      const netCost = parseFloat(this.state.product.netCost)
      const salePrice = parseFloat(value)
      const utility = ((salePrice / taxRate - netCost) / netCost) * 100

      salePrices[index].utility = utility.toFixed(4)
    }

    this.handleProductPropertyChange("salePrices", salePrices)
  }

  removePrice = (salePrice) => {
    let prices = Object.assign([], this.state.product.salePrices)

    _.remove(prices, (item) => {
      return item.uid === salePrice.uid
    })

    this.handleProductPropertyChange("salePrices", prices)
  }

  addSalePrice = () => {
    let salePrices = Object.assign([], this.state.product.salePrices)
    const newSalePrice = {
      uid: uuid(),
      utility: "",
      finalPrice: "",
      netPrice: "",
      priceList: { displayName: "" },
    }

    salePrices.push(newSalePrice)

    this.handleProductPropertyChange("salePrices", salePrices)
  }

  getPriceTableActions = (rowData) => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-6">
          <Button
            type="button"
            icon="fa fa-fw fa-trash"
            className="p-button-danger"
            onClick={() => this.removePrice(rowData)}
            tooltip={"Quitar ítem"}
          />
        </div>
      </div>
    )
  }

  handleProductPropertyChange = (property, value) => {
    let product = { ...this.state.product }

    product[property] = value

    if (property === "category") {
      product.subCategory = null
    }

    this.updatePrices(product)
    this.setState({ product })
  }

  handleGetProductInfo = (productInfo) => {
    let { percentages, salePrices, productId } = productInfo

    percentages.forEach((percent) => {
      percent.uid = uuid()
    })
    salePrices.forEach((salePrice) => {
      salePrice.uid = uuid()
    })

    this.setState({ product: productInfo, loading: false, productId: productId })
  }

  renderPercentTableHeader = () => {
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
            onClick={this.addPercent}
          />
        </div>
      </div>
    )
  }

  renderPriceTableHeader = () => {
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
            onClick={this.addSalePrice}
          />
        </div>
      </div>
    )
  }

  updatePrices = (product) => {
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

    this.setState({ product })
  }

  validateProductCode = () => {
    const { product } = this.state

    if (product.code) {
      this.productsService.validateProductCode(this.state.product, (error) =>
        this.handleDuplicatedCodeError(error)
      )
    }
  }

  handleDuplicatedCodeError = (error) => {
    this.toast.show({
      severity: "error",
      summary: `Código duplicado [${error.errorCode}]`,
      detail: error.message,
    })
  }
}
