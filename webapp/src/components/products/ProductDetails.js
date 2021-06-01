import _ from "lodash"
import React, { Component } from "react"
import { ProductsService } from "../../service/ProductsService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"
import PropTypes from "prop-types"
import { Toast } from "primereact/toast"
import { TabPanel, TabView } from "primereact/tabview"
import { InputText } from "primereact/inputtext"
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
    fiscalTaxRate: { type: "object" },
    salePrices: { type: "array" },
    percentages: { type: "array" },
  },
  required: ["description", "code", "grossCost", "fiscalTaxRate", "salePrices"],
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
      this.productsService.getProduct(
        productId,
        (productInfo) => this.handleGetProductInfo(productInfo),
        this.handleGetProductError
      )
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
            <TabView
              activeIndex={this.state.activeTab}
              onTabChange={(e) => this.setState({ activeTab: e.index })}
            >
              <TabPanel header="Identificación">
                {this.renderIdentificationSection()}
              </TabPanel>
              <TabPanel header="Precio">{this.renderPricingSection()}</TabPanel>
              <TabPanel header="Clasificación">
                {/*{this.renderAdvancedSearchOptions()}*/}
              </TabPanel>
              <TabPanel header="Stock">
                {/*{this.renderAdvancedSearchOptions()}*/}
              </TabPanel>
            </TabView>
            <LoadingButton
              type="submit"
              label="Guardar"
              loading={this.state.loading}
              icon="fa fa-fw fa-save"
            />
          </Form>
        )}
      </div>
    )
  }

  handleSubmit = () => {
    const { product } = this.state

    this.setState({ loading: true })

    this.productsService.updateProduct(product, this.handleSuccess, this.handleError)
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
      return "Edición de producto"
    }
    return "Nuevo producto"
  }

  handleGetProductError = (error) => {
    this.toast.show({
      severity: "error",
      summary: "No se pudo encontrar el producto",
      detail: _.get(error, "message", ""),
    })
  }

  renderIdentificationSection = () => {
    const {
      productId,
      description,
      code,
      factoryCode,
      observations,
    } = this.state.product

    return (
      <div className="p-card-body p-fluid p-grid">
        <label className="p-col-3 ">{"Id:"}</label>
        <div className="p-col-9 ">{productId}</div>
        <div className="p-col-3 ">{"Código:"}</div>
        <div className="p-col-9 ">
          <Field id="code" component={InputText} name="code" value={code} />
          <FieldError name="code" />
        </div>
        <div className="p-col-3 ">{"Descripción:"}</div>
        <div className="p-col-9 ">
          <Field
            id="description"
            component={InputText}
            name="description"
            value={description}
          />
          <FieldError name="description" />
        </div>
        <div className="p-col-3 ">{"Código de fábrica:"}</div>
        <div className="p-col-9 ">
          <Field
            id="factoryCode"
            component={InputText}
            name="factoryCode"
            value={factoryCode}
          />
          <FieldError name="factoryCode" />
        </div>
        <div className="p-col-3 ">{"Observaciones:"}</div>
        <div className="p-col-9 ">
          <Field
            id="observations"
            component={InputTextarea}
            name="observations"
            value={observations || ""}
          />
          <FieldError name="observations" />
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

    // if (property === "idRubro") {
    //   searchFilter.idSubRubro = null
    // }
    this.updatePrices(product)
    this.setState({ product })
  }

  handleGetProductInfo = (productInfo) => {
    let { percentages, salePrices } = productInfo

    percentages.forEach((percent) => {
      percent.uid = uuid()
    })
    salePrices.forEach((salePrice) => {
      salePrice.uid = uuid()
    })

    this.setState({ product: productInfo, loading: false })
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
      salePrice.finalPrice = (netPrice * taxRate).toFixed(2)
    })

    this.setState({ product })
  }
}
