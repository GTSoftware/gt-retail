import _ from "lodash";
import React, {Component} from "react";
import {ProductsService} from "../../service/ProductsService";
import {Growl} from "primereact/growl";
import {Dropdown} from "primereact/dropdown";
import {Button} from "primereact/button";
import {SearchProductsTable} from "../core/SearchProductsTable";
import {AutoComplete} from "primereact/autocomplete";
import {InputNumber} from "primereact/inputnumber";
import {Checkbox} from "primereact/checkbox";
import {LoadingButton} from "../core/LoadingButton";
import {AddPercentDialog} from "./AddPercentDialog";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";

export class BatchPricing extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            categories: [],
            subCategories: [],
            brands: [],
            supplyTypes: [],
            percentTypes: [],
            productsSearchOptions: {
                supplyType: null,
                category: null,
                subCategory: null,
                brand: null,
                supplier: null
            },
            loading: false,
            filteredProducts: [],
            totalResults: 0,
            filteredSuppliers: [],
            updateOptions: {
                updateCost: false,
                costPercent: 0,
                updatePercents: false,
                percentsToAdd: [],
                percentsToDelete: []
            },
            updatingPrices: false,
            itemNumber: 1
        }

        this.productsService = new ProductsService();
    }

    componentDidMount() {
        const {categories, brands, supplyTypes, percentTypes} = this.state;

        if (categories.length === 0) {
            this.productsService.getCategories((categories) => this.setState({categories: categories}));
        }

        if (brands.length === 0) {
            this.productsService.getBrands((brands) => this.setState({brands: brands}));
        }

        if (supplyTypes.length === 0) {
            this.productsService.getSupplyTypes((supplyTypes) => this.setState({supplyTypes: supplyTypes}));
        }

        if (percentTypes.length === 0) {
            this.productsService.getPercentTypes((percentTypes) => this.setState({percentTypes: percentTypes}));
        }
    }

    render() {

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                <h1>Actualización masiva de precios</h1>
                {this.renderFilterSection()}
                {this.renderProductsPreview()}
                <h3>Productos a modificar: {this.state.totalResults}</h3>

                <div className="SeparatorFull"/>
                {(this.state.totalResults > 0) && this.renderUpdateParametersSection()}
            </div>
        )
    }

    renderFilterSection = () => {
        const {supplyTypes, productsSearchOptions, brands, categories, subCategories} = this.state;
        return (
            <div className="p-grid p-fluid">
                <div className="p-col-12">
                    <label htmlFor="supplyType">Tipo de proveeduría:</label>
                    <Dropdown id="supplyType"
                              dataKey="supplyTypeId"
                              options={supplyTypes}
                              showClear={true}
                              value={productsSearchOptions.supplyType}
                              optionLabel="displayName"
                              onChange={(e) => this.handleProductsSearchOptionsChange('supplyType', e.value)}
                    />
                </div>

                <div className="p-col-12">
                    <label htmlFor="supplier">Proveedor:</label>
                    <AutoComplete minLength={2} placeholder="Comience a escribir para buscar un proveedor"
                                  delay={500}
                                  id="supplier"
                                  completeMethod={(event) => this.filterSuppliers(event.query)}
                                  suggestions={this.state.filteredSuppliers}
                                  field="displayName"
                                  onChange={(e) => this.handleProductsSearchOptionsChange('supplier', e.value)}
                                  value={productsSearchOptions.supplier || ''}

                    />
                </div>

                <div className="p-col-12">
                    <label htmlFor="brand">Marca:</label>
                    <Dropdown id="brand"
                              filter={true}
                              dataKey="brandId"
                              options={brands}
                              showClear={true}
                              value={productsSearchOptions.brand}
                              optionLabel="displayName"
                              onChange={(e) => this.handleProductsSearchOptionsChange('brand', e.value)}
                    />
                </div>

                <div className="p-col-6">
                    <label htmlFor="category">Rubro:</label>
                    <Dropdown id="category"
                              dataKey="categoryId"
                              filter={true}
                              options={categories}
                              showClear={true}
                              value={productsSearchOptions.category}
                              optionLabel="displayName"
                              onChange={(e) => this.handleProductsSearchOptionsChange('category', e.value)}
                    />
                </div>
                <div className="p-col-6">
                    <label htmlFor="subCategory">Sub-Rubro:</label>
                    <Dropdown id="subCategory"
                              dataKey="subCategoryId"
                              filter={true}
                              options={subCategories}
                              showClear={true}
                              value={productsSearchOptions.subCategory}
                              optionLabel="displayName"
                              onChange={(e) => this.handleProductsSearchOptionsChange('subCategory', e.value)}
                    />
                </div>
                <div className="p-col-6">
                    <LoadingButton label="Aplicar filtros" icon="fa fa-fw fa-check"
                                   loading={this.state.loading}
                                   onClick={this.handleApplyFilters}/>
                </div>
            </div>
        )
    }

    renderProductsPreview = () => {
        return (
            <SearchProductsTable onPageEvent={this.handlePageChange}
                                 emptyMessage="Aplique filtros para visualizar los productos"
                                 rows={5}
                                 totalRecords={this.state.totalResults}
                                 products={this.state.filteredProducts}/>
        )
    }

    renderUpdateParametersSection = () => {
        const {updateCost, costPercent, updatePercents} = this.state.updateOptions;

        return (
            <div className="p-grid p-fluid">
                {this.renderAddPercentDialog()}
                <div className="p-col-6">
                    <label htmlFor="updateCostField">Actualizar costo:</label>
                    <div className="p-inputgroup">
                        <span className="p-inputgroup-addon">
                            <Checkbox id="updateCost"
                                      onChange={(e) => {
                                          this.handleUpdateOptionsChange('updateCost', e.checked)
                                      }}
                                      tooltip={"Habilita la actualización del costo"}
                                      checked={updateCost}
                            />
                        </span>
                        <InputNumber id="updateCostField"
                                     placeholder="Costo"
                                     maxFractionDigits={4}
                                     minFractionDigits={2}
                                     prefix="%"
                                     showButtons
                                     mode="decimal"
                                     disabled={!updateCost}
                                     value={costPercent}
                                     onChange={(e) => {
                                         this.handleUpdateOptionsChange('costPercent', e.target.value)
                                     }}
                        />
                    </div>
                </div>

                <div className="p-col-6"/>


                <div className="p-col-6">
                    <label htmlFor="updatePercent">Actualizar porcentajes:</label>
                    <Checkbox id="updatePercent"
                              onChange={(e) => {
                                  this.handleUpdateOptionsChange('updatePercents', e.checked)
                              }}
                              tooltip={"Habilita la actualización de porcentajes"}
                              checked={updatePercents}
                    />

                </div>

                <div className="p-col-12">
                    <Button label="Agregar porcentaje"
                            icon="fa fa-fw fa-plus"
                            disabled={!updatePercents}
                            onClick={() => this.setState({showAddPercentDialog: true})}/>
                </div>
                <div className="p-col-6">
                    {this.renderPercentsTable('Para agregar', 'percentsToAdd')}
                </div>
                <div className="p-col-6">
                    {this.renderPercentsTable('Para borrar', 'percentsToDelete')}
                </div>

                <div className="p-col-6">
                    <LoadingButton label="Aplicar cambios"
                                   icon="fa fa-fw fa-calculator"
                                   className="p-button-success"
                                   disabled={!(updateCost || updatePercents)}
                                   loading={this.state.updatingPrices}
                                   onClick={this.handleApplyChanges}/>
                </div>
            </div>

        );
    }

    renderAddPercentDialog = () => {
        return (
            <AddPercentDialog visible={this.state.showAddPercentDialog}
                              modal={true}
                              acceptCallback={this.handleAddPercent}
                              onHide={() => this.setState({showAddPercentDialog: false})}/>
        )
    }

    handleAddPercent = (percent) => {
        let percentItem = {...percent};
        let {updateOptions, itemNumber} = this.state;

        percentItem.item = itemNumber;
        if (percentItem.action === 'add') {
            updateOptions.percentsToAdd.splice(0, 0, percentItem);
        } else {
            updateOptions.percentsToDelete.splice(0, 0, percentItem);
        }

        this.setState({
            updateOptions: updateOptions,
            itemNumber: itemNumber + 1
        });
    }

    handleProductsSearchOptionsChange = (property, value) => {
        let {productsSearchOptions} = this.state;

        productsSearchOptions[property] = value;

        this.setState({productsSearchOptions: productsSearchOptions});

        if (property === 'category') {
            if (value) {
                this.productsService.getSubCategories(value.categoryId, (subCat) => this.setState({subCategories: subCat}));
            } else {
                this.setState({subCategories: []});
            }
        }
    }

    handleUpdateOptionsChange = (property, value) => {
        let {updateOptions} = this.state;

        updateOptions[property] = value;

        this.setState({updateOptions: updateOptions});
    }

    handleApplyFilters = () => {
        let searchOptions = {
            firstResult: 0,
            maxResults: 5,
            searchFilter: this.getSearchFilter()
        }

        this.productsService.searchProducts(searchOptions, (response) => this.setState({
            filteredProducts: response.data,
            totalResults: response.totalResults,
            loading: false
        }));

        this.setState({
            loading: true,
            first: 0
        });
    }

    handlePageChange = (pageOptions) => {
        let searchOptions = {
            firstResult: pageOptions.firstResult,
            maxResults: 5,
            searchFilter: this.getSearchFilter()
        }

        this.productsService.searchProducts(searchOptions, (response) => this.setState({
            filteredProducts: response.data,
            totalResults: response.totalResults
        }));

    }

    getSearchFilter = () => {
        const {productsSearchOptions} = this.state;

        return {
            activo: true,
            idTipoProveeduria: _.get(productsSearchOptions, 'supplyType.supplyTypeId', null),
            idRubro: _.get(productsSearchOptions, 'category.categoryId', null),
            idSubRubro: _.get(productsSearchOptions, 'subCategory.subCategoryId', null),
            idMarca: _.get(productsSearchOptions, 'brand.brandId', null),
            idProveedorHabitual: _.get(productsSearchOptions, 'supplier.personId', null),
            sortFields: [{
                fieldName: "descripcion",
                ascending: true
            }]
        }
    }

    filterSuppliers = (query) => {
        this.productsService.searchSuppliers(query, (suppliers) => this.setState({filteredSuppliers: suppliers}));
    }

    handleApplyChanges = () => {
        let {updateOptions} = this.state;
        this.setState({updatingPrices: true});

        updateOptions.searchFilter = this.getSearchFilter();

        this.productsService.updateProductsPricing(updateOptions, this.handleSuccess, this.handleError);
    }

    handleSuccess = () => {
        this.setState({updatingPrices: false});

        this.growl.show({
            severity: 'info',
            summary: 'Precios actualizados exitosamente',
            detail: ''
        });
    }

    handleError = (error) => {
        this.setState({updatingPrices: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudieron actualizar los precios',
            detail: _.get(error, 'response.data.message', '')
        });
    }

    renderPercentsTable = (header, percentsProperty) => {
        const percents = this.state.updateOptions[percentsProperty];

        return (
            <DataTable header={header}
                       value={percents}
                       resizableColumns={true}
                       responsive={true}>
                <Column field="percentType.nombreTipo" header="Tipo"/>
                <Column field="value" header="Valor"/>
                <Column key="actions"
                        body={(rowData) => this.getRemoveAction(rowData, percentsProperty)}
                        style={{textAlign: 'center', width: '7em'}}/>
            </DataTable>
        )
    }

    getRemoveAction = (rowData, percentsProperty) => {
        return (
            <Button type="button"
                    icon="fa fa-fw fa-trash"
                    className="p-button-danger"
                    onClick={() => this.removePercent(rowData, percentsProperty)}
                    tooltip={'Quitar ítem'}/>
        )
    }

    removePercent = (rowData, percentsProperty) => {
        const {updateOptions} = this.state;
        let percents = updateOptions[percentsProperty];

        _.remove(percents, (item) => {
            return (item.item === rowData.item)
        });

        this.setState({updateOptions: updateOptions});
    }
}
