import React, {Component} from "react";
import {Growl} from "primereact/growl";
import {DataTable} from "primereact/datatable";
import {DEFAULT_DATA_TABLE_PROPS} from "../DefaultProps";
import {Column} from "primereact/column";
import {Button} from "primereact/button";
import {InputText} from "primereact/inputtext";
import ShopCartService from "../../service/ShopCartService";
import _ from "lodash";
import {LoadingButton} from "../core/LoadingButton";
import {ShopCartStore} from "../../stores/ShopCartStore";
import {Dialog} from "primereact/dialog";
import LoginService from "../../service/LoginService";

const productColumns = [
    {field: 'codigoPropio', header: 'Código', style: {width: "10%"}},
    {field: 'descripcion', header: 'Descripción', style: {width: "30%"}},
    {field: 'unidadVenta', header: 'Unidad', style: {width: "5%"}},
    {field: 'cantidad', header: 'Cantidad', style: {width: "10%"}},
    {field: 'precioVentaUnitario', header: 'Precio'},
    {field: 'subTotal', header: 'SubTotal'}
];

export class ShopCartItems extends Component {

    constructor(props) {
        super(props);

        this.shopCartStore = new ShopCartStore();
        this.isUserAdmin = LoginService.hasUserRole('ADMINISTRADORES');

        this.state = {
            products: this.shopCartStore.getProducts() || [],
            productToSearch: {
                cantidad: 1,
                productId: "",
                productCode: ""
            },
            itemNumber: this.getNextItemNumber(),
            loadingAddProduct: false,
            showEditItemDialog: false
        };


        this.getItemsTableProps = this.getItemsTableProps.bind(this);
        this.getTableActions = this.getTableActions.bind(this);
        this.renderColumns = this.renderColumns.bind(this);
        this.updateProperty = this.updateProperty.bind(this);
        this.getCartTotal = this.getCartTotal.bind(this);
        this.tryAddProduct = this.tryAddProduct.bind(this);
        this.handleFoundProduct = this.handleFoundProduct.bind(this);
        this.initProductSearch = this.initProductSearch.bind(this);
        this.removeItem = this.removeItem.bind(this);
        this.handleUpdatedItem = this.handleUpdatedItem.bind(this);
        this.handleProductNotFoundError = this.handleProductNotFoundError.bind(this);
        this.handleEnterKeyPress = this.handleEnterKeyPress.bind(this);
        this.handleGoNext = this.handleGoNext.bind(this);
        this.getNextItemNumber = this.getNextItemNumber.bind(this);
        this.renderAddProductSection = this.renderAddProductSection.bind(this);
        this.renderDialogContent = this.renderDialogContent.bind(this);
    }

    render() {
        return (
            <div className="card card-w-title">
                <Growl ref={(el) => this.growl = el}/>
                {this.renderAddProductSection()}
                {this.renderEditProductDialog()}

                <DataTable {...this.getItemsTableProps()} >
                    {this.renderColumns()}
                </DataTable>
                <div className="p-card-footer">

                    <div className="p-col p-justify-end p-align-end p-grid">
                        <Button label="Siguiente"
                                className="shop-cart--footer-actions-button"
                                iconPos="right"
                                disabled={this.state.products.length === 0}
                                icon="fa fa-fw fa-arrow-right"
                                onClick={this.handleGoNext}/>
                    </div>
                </div>
            </div>
        )
    }

    renderAddProductSection() {
        return (
            <div className="p-card-body p-fluid p-grid">
                <div className="p-col-12 p-lg-4">
                    <InputText id="id"
                               autoFocus
                               onChange={(e) => {
                                   this.updateProperty('productId', e.target.value)
                               }}
                               value={this.state.productToSearch.productId}
                               keyfilter="int"
                               placeholder="Id"
                               className="p-col-1"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-12 p-lg-4">
                    <InputText id="codigo"
                               onChange={(e) => {
                                   this.updateProperty('productCode', e.target.value)
                               }}
                               value={this.state.productToSearch.productCode}
                               placeholder="Código"
                               className="p-col-1"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-2 p-lg-1">
                    <InputText id="cantidad"
                               keyfilter="num"
                               onChange={(e) => {
                                   this.updateProperty('cantidad', e.target.value)
                               }}
                               value={this.state.productToSearch.cantidad}
                               placeholder="Cantidad"
                               className="p-col-2"
                               onKeyPress={this.handleEnterKeyPress}
                    />
                </div>
                <div className="p-col-3 p-lg-3">
                    <LoadingButton type="button"
                                   icon="fa fa-fw fa-plus"
                                   className="p-button-success shop-cart--add-product-button"
                                   onClick={this.tryAddProduct}
                                   loading={this.state.loadingAddProduct}
                                   tooltip={'Agregar producto'}/>

                    <Button type="button"
                            className="shop-cart--search-product-button"
                            icon="fa fa-fw fa-search"
                            tooltip={'Buscar producto'}/>

                    <Button type="button"
                            className="p-button-secondary"
                            icon="fa fa-fw fa-cog"
                            tooltip={'Opciones'}/>
                </div>
            </div>
        );
    }

    renderEditProductDialog() {
        let dialogFooter = (
            <div className="ui-dialog-buttonpane p-clearfix">
                <Button label="Aceptar" icon="fa fa-fw fa-check" onClick={() => this.handleUpdateItem()}/>
            </div>
        );

        return (
            <Dialog visible={this.state.showEditItemDialog}
                    modal={true}
                    header={'Editar producto'}
                    footer={dialogFooter}
                    onHide={() => this.setState({
                        showEditItemDialog: false,
                        itemToEdit: null
                    })}>
                {this.renderDialogContent()}
            </Dialog>
        );
    }

    renderDialogContent() {
        const showEditItemDialog = this.state.showEditItemDialog;
        let dialogContent = null;

        if (showEditItemDialog) {
            dialogContent = (
                <div className="p-grid p-fluid">
                    {this.renderDescripcionToEdit()}
                    <div className="p-col-4">
                        <label htmlFor="cantidad">Cantidad:</label>
                    </div>
                    <div className="p-col-8">
                        <InputText id="cantidad"
                                   keyfilter="pnum"
                                   onChange={(e) => {
                                       this.handleItemToEditPropertyChange('cantidad', e.target.value)
                                   }}
                                   value={this.state.itemToEdit.cantidad}
                                   required={true}/>
                    </div>
                </div>
            );
        }

        return dialogContent;
    }

    renderDescripcionToEdit() {
        let descripcion = null

        if (this.isUserAdmin) {
            descripcion = (
                <>
                    <div className="p-col-4">
                        <label htmlFor="descripcion">Descripción:</label>
                    </div>
                    <div className="p-col-8">
                        <InputText id="descripcion"
                                   onChange={(e) => {
                                       this.handleItemToEditPropertyChange('descripcion', e.target.value)
                                   }}
                                   value={this.state.itemToEdit.descripcion}
                                   required={true}
                                   disabled={!this.isUserAdmin}
                        />
                    </div>
                </>
            );
        }

        return descripcion;
    }

    renderColumns() {

        let columns = productColumns.map((col, i) => {
            if (col.field === 'cantidad') {
                return <Column key={col.field} field={col.field} header={col.header} style={col.style}
                               body={this.cantidadTemplate}/>;
            }

            return <Column key={col.field} field={col.field} header={col.header} style={col.style}/>;
        });
        columns = columns.concat(
            <Column key="actions"
                    body={this.getTableActions}
                    style={{textAlign: 'center', width: '7em'}}/>
        );

        return columns;
    }

    handleItemToEditPropertyChange(property, value) {
        let itemToEdit = this.state.itemToEdit;

        itemToEdit[property] = value.toUpperCase();

        this.setState({itemToEdit: itemToEdit});
    }

    getItemsTableProps() {
        let header = <div className="p-clearfix">Productos</div>;
        let footer = <div className="p-clearfix">
            <label className="shop-cart--cart-total">TOTAL: ${this.getCartTotal()}</label>
        </div>;

        let tableProps = Object.assign({}
            , DEFAULT_DATA_TABLE_PROPS,
            {
                value: this.state.products,
                header: header,
                footer: footer,
                resizableColumns: true,
                responsive: true
            });

        return tableProps;
    }

    cantidadTemplate(rowData, column) {
        if (rowData.stockActualEnSucursal < rowData.cantidad) {
            return (
                <span>
                    <Button icon="fa fa-fw fa-exclamation-triangle"
                            className="shop-cart--not-enough-quantity"
                            type="button"
                            label={rowData.cantidad}
                            iconPos="right"
                            tooltip={`No hay stock suficiente para esta cantidad. Stock: ${rowData.stockActualEnSucursal}`}/>
                </span>
            )
        }
        return <span>{rowData.cantidad}</span>
    }

    getTableActions(rowData, column) {
        let buttonToRender = null;

        if (rowData.deletable) {
            buttonToRender =
                <div className="p-grid p-fluid">
                    <Button type="button" icon="fa fa-fw fa-trash"
                            className="p-button-danger"
                            onClick={() => (this.removeItem(rowData))}
                            tooltip={'Quitar ítem'}/>

                    <Button type="button"
                            icon="fa fa-fw fa-edit"
                            tooltip={'Editar ítem'}
                            onClick={() => {
                                this.handleEditRow(rowData)
                            }}
                    />
                </div>
        }

        return buttonToRender;
    }

    updateProperty(property, value) {
        let productToSearch = this.state.productToSearch;

        productToSearch[property] = value;
        this.setState({productToSearch: productToSearch});
    }

    getCartTotal() {
        let total = 0;
        this.state.products.map((product) => total = total + product.subTotal);

        return +(total).toFixed(2);
    }

    tryAddProduct() {
        if (this.state.productToSearch.productCode || this.state.productToSearch.productId) {
            this.setState({loadingAddProduct: true});

            ShopCartService.addProduct(this.state.productToSearch)
                .then(
                    (response) => this.handleFoundProduct(response.data, 0)
                )
                .catch(
                    error => this.handleProductNotFoundError(error)
                )
        }
    }

    handleFoundProduct(product, atIndex, itemToEdit) {
        let state = this.state;
        let products = state.products;
        let itemNumberIncrement = 1;


        product.itemNumber = state.itemNumber;
        product.cantidadEditable = true;
        product.deletable = true;
        products.splice(atIndex, 0, product);

        if (itemToEdit) {
            product.descripcion = itemToEdit.descripcion;
        }

        if (product.discountItem) {
            product.discountItem.cantidadEditable = false;
            product.discountItem.itemNumber = product.itemNumber + 1;
            product.discountItem.parentItem = product.itemNumber;
            product.discountItem.deletable = false;

            products.splice(atIndex + 1, 0, product.discountItem);

            delete product.discountItem;

            itemNumberIncrement++;
        }

        this.initProductSearch();
        this.setState({
            products: products,
            itemNumber: state.itemNumber + itemNumberIncrement,
            loadingAddProduct: false
        })
    }

    handleUpdateItem() {
        let itemToEdit = this.state.itemToEdit;
        let cantidad;

        if (itemToEdit) {
            cantidad = parseFloat(itemToEdit.cantidad).toFixed(2);
            itemToEdit.cantidad = cantidad < 0.01 ? 1 : cantidad;

            this.handleUpdatedItem(itemToEdit);
            this.setState({showEditItemDialog: false})
        }
    }

    initProductSearch() {
        this.setState({
            productToSearch: {
                cantidad: 1,
                productId: "",
                productCode: ""
            }
        });
    }

    removeItem(rowData) {
        let products = this.state.products;
        let index;

        for (let i = 0; i < products.length; i++) {
            if (products[i].itemNumber === rowData.itemNumber) {
                index = i;
            }
        }

        _.remove(products, (item) => {
            return (item.itemNumber === rowData.itemNumber || item.parentItem === rowData.itemNumber)
        });

        this.setState({products: products});

        return index;
    }

    handleEditRow(rowData) {
        this.setState({
            showEditItemDialog: true,
            itemToEdit: {...{}, ...rowData}
        });
    }

    handleUpdatedItem(itemToEdit) {
        let removedIndex = this.removeItem(itemToEdit);

        ShopCartService.addProduct({
            cantidad: itemToEdit.cantidad,
            productId: itemToEdit.id
        })
            .then(
                (response) => this.handleFoundProduct(response.data, removedIndex, itemToEdit)
            )
            .catch(
                error => this.handleProductNotFoundError(error)
            )
    }

    handleProductNotFoundError(error) {
        this.setState({loadingAddProduct: false});

        this.growl.show({
            severity: 'error',
            summary: 'No se pudo encontrar el producto',
            detail: error.response.data.message
        });
    }

    handleEnterKeyPress(event) {
        if (event.key === 'Enter' && !this.state.loadingAddProduct) {
            this.tryAddProduct();
        }
    }

    handleGoNext() {
        this.shopCartStore.setProducts(this.state.products);
        this.props.goNextCallback();
    }

    getNextItemNumber() {
        const products = this.shopCartStore.getProducts();

        if (!products || products.length === 0) {
            return 1;
        } else {
            return Math.max.apply(Math, products.map((p) => {
                return p.itemNumber;
            })) + 1;
        }
    }
}
