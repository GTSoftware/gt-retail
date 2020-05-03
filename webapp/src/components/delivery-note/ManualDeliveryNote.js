import React, {Component} from "react";
import {SelectButton} from "primereact/selectbutton";
import {Dropdown} from "primereact/dropdown";
import {AutoComplete} from "primereact/autocomplete";
import {DeliveryNotesService} from "../../service/DeliveryNotesService";
import {Button} from "primereact/button";
import {Panel} from "primereact/panel";
import {InputText} from "primereact/inputtext";
import {LoadingButton} from "../core/LoadingButton";
import {ProductsService} from "../../service/ProductsService";
import {Checkbox} from "primereact/checkbox";
import {Growl} from "primereact/growl";
import {DataTable} from "primereact/datatable";
import {DEFAULT_DATA_TABLE_PROPS} from "../DefaultProps";
import {Column} from "primereact/column";
import _ from "lodash";
import {InputTextarea} from "primereact/inputtextarea";
import FileOutputsService from "../../service/FileOutputsService";
import {SearchProductsDialog} from "../shop-cart/SearchProductsDialog";

const DELIVERY_DIRECTION = {
    INTERNAL: 'Interno',
    EXTERNAL: 'Externo'
}

const DELIVERY_DIRECTIONS = [DELIVERY_DIRECTION.INTERNAL, DELIVERY_DIRECTION.EXTERNAL];

export class ManualDeliveryNote extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            originDirection: DELIVERY_DIRECTION.EXTERNAL,
            destinationDirection: DELIVERY_DIRECTION.INTERNAL,
            deliveryItems: [],
            deliveryTypes: [],
            warehouses: [],
            filteredPersons: [],
            origin: null,
            deliveryType: null,
            destination: null,
            firstStepDone: false,
            panelCollapsed: false,
            productToSearch: {
                productId: '',
                productCode: '',
                supplierCode: '',
                quantity: 1,
                usePurchaseUnits: false
            },
            loadingAddProduct: false,
            itemNumber: 1,
            observations: '',
            savingDeliveryNote: false,
            savedDeliveryNoteId: null
        }

        this.deliveryNotesService = new DeliveryNotesService();
        this.productsService = new ProductsService();
    }

    componentDidMount() {
        const {deliveryTypes, warehouses} = this.state;

        if (deliveryTypes.length === 0) {
            this.deliveryNotesService.getDeliveryTypes(this.handleDeliveryTypes);
        }
        if (warehouses.length === 0) {
            this.deliveryNotesService.getWarehouses(this.handleWarehouses);
        }
    }

    render() {
        const {firstStepDone} = this.state;

        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                {this.renderSearchProductsDialog()}
                <h1>Nuevo remito manual</h1>
                <Panel header={this.getPanelHeader()} toggleable={true}
                       collapsed={this.state.panelCollapsed}
                       onToggle={(e) => this.setState({panelCollapsed: e.value})}>
                    <div className="p-grid p-fluid">

                        {this.renderOriginSection()}
                        {this.renderDestinationSection()}
                        {this.renderDeliveryTypeSection()}

                    </div>
                </Panel>
                {this.renderConfirmFirstStepButton()}

                {firstStepDone && <div className="SeparatorFull"/>}
                {firstStepDone && this.renderAddItemsSection()}
                {firstStepDone && this.renderDeliveryItemsSection()}

                {firstStepDone && <div className="SeparatorFull"/>}
                {firstStepDone && this.renderFooterSection()}
            </div>
        )
    }

    getPanelHeader = () => {
        const {firstStepDone, origin, destination, deliveryType} = this.state;
        let header = "Origen y destino";

        if (firstStepDone) {
            header = `Origen: ${origin.displayName} Destino: ${destination.displayName} - ${deliveryType.nombreTipo}`
        }

        return header;
    }

    renderOriginSection = () => {
        const {originDirection} = this.state;

        return (
            <div className="p-col-6">
                <h3>Origen:</h3>
                <SelectButton disabled={this.state.firstStepDone}
                              value={originDirection}
                              options={DELIVERY_DIRECTIONS}
                              onChange={(e) => this.handleDeliveryDirectionChange('origin', e.value)}/>
                {this.renderOriginSelector()}
            </div>
        )
    }

    renderOriginSelector = () => {
        const {originDirection, warehouses, origin} = this.state;
        let originSelector = (
            <Dropdown id="internalOrigin"
                      optionLabel="displayName"
                      placeholder="Seleccione un depósito"
                      options={warehouses}
                      value={origin}
                      disabled={this.state.firstStepDone}
                      onChange={(e) => this.setState({origin: e.value})}/>
        );
        if (DELIVERY_DIRECTION.EXTERNAL === originDirection) {
            originSelector = <AutoComplete minLength={2} placeholder="Comience a escribir para buscar una persona"
                                           delay={500}
                                           id="originPersonField"
                                           completeMethod={(event) => this.filterPersons(event.query)}
                                           suggestions={this.state.filteredPersons}
                                           field="displayName"
                                           required={true}
                                           disabled={this.state.firstStepDone}
                                           onChange={(e) => this.setState({origin: e.value})}
                                           value={origin || ''}

            />
        }

        return originSelector;
    }

    renderDestinationSection = () => {
        const {destinationDirection} = this.state;

        return (
            <div className="p-col-6">

                <h3>Destino:</h3>
                <SelectButton
                    disabled={this.state.firstStepDone}
                    value={destinationDirection}
                    options={DELIVERY_DIRECTIONS}
                    onChange={(e) => this.handleDeliveryDirectionChange('destination', e.value)}/>
                {this.renderDestinationSelector()}

            </div>
        )
    }

    renderDestinationSelector = () => {
        const {destinationDirection, warehouses, destination} = this.state;
        let destinationSelector = (
            <Dropdown id="internalDestination"
                      disabled={this.state.firstStepDone}
                      optionLabel="displayName"
                      placeholder="Seleccione un depósito"
                      options={warehouses}
                      value={destination}
                      onChange={(e) => this.setState({destination: e.value})}/>
        );
        if (DELIVERY_DIRECTION.EXTERNAL === destinationDirection) {
            destinationSelector = <AutoComplete minLength={2} placeholder="Comience a escribir para buscar una persona"
                                                delay={500}
                                                disabled={this.state.firstStepDone}
                                                id="destinationPersonField"
                                                completeMethod={(event) => this.filterPersons(event.query)}
                                                suggestions={this.state.filteredPersons}
                                                field="displayName"
                                                required={true}
                                                onChange={(e) => this.setState({destination: e.value})}
                                                value={destination || ''}

            />
        }

        return destinationSelector;
    }

    renderDeliveryTypeSection = () => {
        const {deliveryTypes, deliveryType} = this.state;

        return (
            <div className="p-col-12">
                <h3>Tipo de movimiento:</h3>
                <Dropdown id="deliveryType"
                          optionLabel="nombreTipo"
                          placeholder="Seleccione el tipo de movimiento"
                          options={deliveryTypes}
                          value={deliveryType}
                          disabled={this.state.firstStepDone}
                          onChange={(e) => this.setState({deliveryType: e.value})}/>
            </div>
        )
    }

    renderConfirmFirstStepButton = () => {
        const {firstStepDone, origin, destination, deliveryType, originDirection, destinationDirection} = this.state;
        let nextStepButton = null;

        if (!firstStepDone && (origin && destination && deliveryType)
            && !(DELIVERY_DIRECTION.EXTERNAL === originDirection && DELIVERY_DIRECTION.EXTERNAL === destinationDirection)) {
            nextStepButton = <Button label="Confirmar"
                                     className="p-button-success"
                                     onClick={() => this.setState({
                                         firstStepDone: true,
                                         panelCollapsed: true
                                     })}
                                     icon="fa fa-fw fa-arrow-down"/>;
        }

        return nextStepButton;
    }

    renderAddItemsSection = () => {
        const {productToSearch} = this.state;

        return (
            <div className="p-card-body p-fluid p-grid">
                <div className="p-col-1 p-lg-1">
                    <InputText id="id"
                               autoFocus
                               onChange={(e) => {
                                   this.handleSearchProductPropertyChange('productId', e.target.value)
                               }}
                               value={productToSearch.productId}
                               keyfilter="int"
                               placeholder="Id"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-2 p-lg-2">
                    <InputText id="supplierCode"
                               onChange={(e) => {
                                   this.handleSearchProductPropertyChange('supplierCode', e.target.value)
                               }}
                               value={productToSearch.supplierCode}
                               placeholder="Código de fábrica"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-2 p-lg-2">
                    <InputText id="codigo"
                               onChange={(e) => {
                                   this.handleSearchProductPropertyChange('productCode', e.target.value)
                               }}
                               value={productToSearch.productCode}
                               placeholder="Código propio"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-2 p-lg-2">
                    <div className="p-inputgroup">

                        <span className="p-inputgroup-addon">
                            <Checkbox id="usePurchaseUnits"
                                      onChange={(e) => {
                                          this.handleSearchProductPropertyChange('usePurchaseUnits', e.checked)
                                      }}
                                      tooltip={"Usar unidades de compra"}
                                      checked={productToSearch.usePurchaseUnits}
                            />
                        </span>
                        <InputText id="cantidad"
                                   keyfilter="num"
                                   onChange={(e) => {
                                       this.handleSearchProductPropertyChange('quantity', e.target.value)
                                   }}
                                   value={productToSearch.quantity}
                                   placeholder="Cantidad"
                                   onKeyPress={this.handleEnterKeyPress}
                        />
                    </div>

                </div>

                <div className="p-col-2 p-lg-2">
                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-plus"
                                   className="p-button-success shop-cart--add-product-button"
                                   onClick={this.tryAddProduct}
                                   loading={this.state.loadingAddProduct}
                                   tooltip={'Agregar producto'}/>

                    <Button type="button"
                            className="shop-cart--search-product-button"
                            icon="fa fa-fw fa-search"
                            tooltip={'Buscar productos'}
                            onClick={() => this.setState({showSearchProductsDialog: true})}
                    />

                </div>
            </div>
        );
    }

    renderDeliveryItemsSection = () => {
        const internalOrigin = DELIVERY_DIRECTION.INTERNAL === this.state.originDirection;
        const internalDestination = DELIVERY_DIRECTION.INTERNAL === this.state.destinationDirection;

        return (
            <DataTable {...this.getItemsTableProps()} >
                <Column key='productId' field='productId' header='Id' style={{width: "5%"}}/>
                <Column key='productCode' field='productCode' header='Código' style={{width: "8%"}}/>
                <Column key='supplierCode' field='supplierCode' header='Cod. Fábrica' style={{width: "8%"}}/>
                <Column key='description' field='description' header='Descripción' style={{width: "20%"}}/>
                <Column key='totalStock' field='totalStock' header='Stock total'/>
                <Column key='quantity' field='quantity' header='Cantidad'/>

                {internalOrigin &&
                <Column key='originWarehouseNewStock' field='originWarehouseNewStock' header='Remanente en origen'/>}

                {internalDestination &&
                <Column key='destinationWarehouseNewStock' field='destinationWarehouseNewStock'
                        header='Stock en destino'/>}

                <Column key='purchaseUnits' field='purchaseUnits' header='Un. Venta'/>
                <Column key='saleUnits' field='saleUnits' header='Un. Compra'/>
                <Column key="actions"
                        body={this.getTableActions}
                        style={{textAlign: 'center', width: '7em'}}/>
            </DataTable>
        )
    }

    renderFooterSection = () => {
        const {savedDeliveryNoteId} = this.state;

        return (
            <div className="p-fluid p-grid">
                <div className="p-col-12">
                    <label htmlFor="observaciones">Observaciones:</label>
                    <InputTextarea value={this.state.observations}
                                   onChange={(e) => this.setState({observations: e.target.value})}/>
                </div>
                <div className="p-col-6">
                    {!savedDeliveryNoteId && <LoadingButton loading={this.state.savingDeliveryNote}
                                                            className="p-button-success"
                                                            icon="fa fa-fw fa-save"
                                                            label="Guardar"
                                                            disabled={this.state.deliveryItems.length === 0}
                                                            onClick={this.saveDeliveryNote}
                    />}
                    {savedDeliveryNoteId && <Button label="Imprimir Remito"
                                                    icon="fa fa-fw fa-print"
                                                    onClick={() => {
                                                        FileOutputsService.getDeliveryNote(savedDeliveryNoteId);
                                                    }}
                    />}
                </div>
            </div>
        )
    }

    renderSearchProductsDialog = () => {
        return (
            <SearchProductsDialog visible={this.state.showSearchProductsDialog}
                                  modal={true}
                                  acceptCallback={this.handleSelectedProduct}
                                  onHide={() => this.setState({showSearchProductsDialog: false})}/>
        )
    }

    getItemsTableProps() {
        let header = <div className="p-clearfix">Productos</div>;
        let footer = <div className="p-clearfix">
            <label>Renglones: {this.state.deliveryItems.length}</label>
        </div>;

        return {
            ...DEFAULT_DATA_TABLE_PROPS,
            ...{
                value: this.state.deliveryItems,
                header: header,
                footer: footer,
                resizableColumns: true,
                responsive: true,
                emptyMessage: 'Todavía no se han agregado productos al remito'
            }
        }
    }

    getTableActions = (rowData) => {
        return (
            <Button type="button" icon="fa fa-fw fa-trash"
                    className="p-button-danger"
                    onClick={() => (this.removeItem(rowData))}
                    tooltip={'Quitar ítem'}/>
        )
    }

    handleDeliveryDirectionChange = (direction, value) => {
        if (value) {
            if (direction === 'origin') {
                this.setState({
                    originDirection: value,
                    origin: null
                });
            } else {
                this.setState({
                    destinationDirection: value,
                    destination: null
                });
            }
        }
    }

    handleWarehouses = (warehouses) => {
        this.setState({warehouses: warehouses});
    }

    handleDeliveryTypes = (deliveryTypes) => {
        this.setState({deliveryTypes: deliveryTypes});
    }

    filterPersons = (query) => {
        this.deliveryNotesService.searchPersons(query, (data) => this.setState({filteredPersons: data}));
    }

    handleSearchProductPropertyChange = (property, value) => {
        let {productToSearch} = this.state;
        productToSearch[property] = value;

        this.setState({productToSearch: productToSearch});
    }

    tryAddProduct = () => {
        let searchCriteria = {...this.state.productToSearch};

        if (this.shouldSearch(searchCriteria)) {

            this.setState({loadingAddProduct: true});

            if (DELIVERY_DIRECTION.INTERNAL === this.state.originDirection) {
                searchCriteria.originWarehouseId = this.state.origin.warehouseId;
            }

            if (DELIVERY_DIRECTION.INTERNAL === this.state.destinationDirection) {
                searchCriteria.destinationWarehouseId = this.state.destination.warehouseId;
            }

            this.deliveryNotesService.addProduct(searchCriteria, this.handleAddItem, this.handleError);
        }
    }

    shouldSearch = (searchCriteria) => {
        return searchCriteria.supplierCode || searchCriteria.productCode || searchCriteria.productId;
    }

    handleAddItem = (deliveryItem) => {
        let {deliveryItems, itemNumber} = this.state;

        deliveryItem.itemNumber = itemNumber + 1;
        this.setState({
            loadingAddProduct: false,
            itemNumber: itemNumber + 1
        });

        deliveryItems.splice(0, 0, deliveryItem);
        this.clearProductSearch();
    }

    handleError = (error) => {
        this.setState({loadingAddProduct: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudo encontrar el producto',
            detail: error.response.data.message
        });
    }

    handleSaveError = (error) => {
        this.setState({savingDeliveryNote: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudo guardar el remito',
            detail: _.get(error, 'response.data.message', '')
        });
    }

    clearProductSearch = () => {
        let {productToSearch} = this.state;

        productToSearch.quantity = 1;
        productToSearch.productId = '';
        productToSearch.productCode = '';
        productToSearch.supplierCode = '';

        this.setState({productToSearch: productToSearch});
    }

    removeItem = (rowData) => {
        let {deliveryItems} = this.state;

        _.remove(deliveryItems, (item) => {
            return (item.itemNumber === rowData.itemNumber)
        });

        this.setState({deliveryItems: deliveryItems});
    }

    handleEnterKeyPress = (event) => {
        if (event.key === 'Enter' && !this.state.loadingAddProduct) {
            this.tryAddProduct();
        }
    }

    saveDeliveryNote = () => {
        const {originDirection, destinationDirection, origin, destination, deliveryItems, observations, deliveryType} = this.state;
        let deliveryNote = {
            originDirection: originDirection,
            destinationDirection: destinationDirection,
            origin: origin,
            destination: destination,
            deliveryNoteItems: deliveryItems,
            observations: observations,
            deliveryType: deliveryType
        };

        this.setState({savingDeliveryNote: true});

        this.deliveryNotesService.saveDeliveryNote(deliveryNote, this.handleSaveSuccess, this.handleSaveError);
    }

    handleSaveSuccess = (savedDeliveryNoteId) => {
        this.setState({
            savingDeliveryNote: false,
            savedDeliveryNoteId: savedDeliveryNoteId
        });

        this.growl.show({
            severity: 'info',
            summary: 'Remito generado exitosamente',
            detail: `Número: ${savedDeliveryNoteId}`
        });
    }

    handleSelectedProduct = (searchProduct) => {
        let {productToSearch} = this.state;

        productToSearch.productId = searchProduct.id;

        this.setState({productToSearch: productToSearch});
        this.tryAddProduct();
    }
}
