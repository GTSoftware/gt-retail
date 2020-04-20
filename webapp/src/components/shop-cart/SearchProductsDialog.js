import React, {Component} from "react";
import {Dialog} from "primereact/dialog";
import {TabPanel, TabView} from 'primereact/tabview';
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {ProductsService} from "../../service/ProductsService";
import LoginService from "../../service/LoginService";
import {Button} from "primereact/button";
import {InputText} from "primereact/inputtext";
import PropTypes from 'prop-types';
import {Checkbox} from "primereact/checkbox";


const productColumns = [
    {field: 'id', header: 'Id'},
    {field: 'codigoPropio', header: 'Código'},
    {field: 'descripcion', header: 'Descripción', style: {width: "30%"}},
    {field: 'precioVenta', header: 'Precio', style: {"fontWeight": "bold"}},
    {field: 'tipoUnidadVenta.nombreUnidad', header: 'Unidad'},
    {field: 'stockActualEnSucursal', header: 'Stock'},
    {field: 'marca.nombreMarca', header: 'Marca'}
];

export class SearchProductsDialog extends Component {

    static propTypes = {
        acceptCallback: PropTypes.func
    }

    constructor(props, context) {
        super(props, context);

        this.productsService = new ProductsService();

        this.state = {
            products: [],
            loading: false,
            first: 0,
            rows: 5,
            totalRecords: 0,
            activeSearchTab: 0,
            searchFilter: {
                activo: true,
                puedeComprarse: true,
                conStock: true,
                idSucursal: LoginService.getUserDetails().sucursalId,
                sortFields: [{
                    fieldName: "descripcion",
                    ascending: true
                }]
            },
            selectedProduct: null
        }

        this.handleSuccess = this.handleSuccess.bind(this);
        this.renderContent = this.renderContent.bind(this);
        this.renderSearchTabs = this.renderSearchTabs.bind(this);
        this.renderColumns = this.renderColumns.bind(this);
        this.getDialogProps = this.getDialogProps.bind(this);
        this.onPageEvent = this.onPageEvent.bind(this);
        this.renderFooter = this.renderFooter.bind(this);
        this.handleAcceptButton = this.handleAcceptButton.bind(this);
        this.searchProducts = this.searchProducts.bind(this);
        this.handleEnterKeyPress = this.handleEnterKeyPress.bind(this);

    }

    render() {
        return (
            <Dialog {...this.getDialogProps()} >
                {this.renderContent()}
            </Dialog>
        )
    }

    renderContent() {
        return (
            <div>
                {this.renderSearchTabs()}
                {this.renderSearchResults()}
                <div className="SeparatorFull"/>
                {this.renderFooter()}
            </div>

        );
    }

    renderSearchTabs() {
        return (
            <TabView activeIndex={this.state.activeSearchTab}
                     onTabChange={(e) => this.setState({activeSearchTab: e.index})}>
                <TabPanel header="Básico" leftIcon="fa fa-fw fa-search">
                    <div className="p-card-body p-fluid p-grid">
                        <div className="p-col-12 p-lg-4">
                            <InputText id="searchText"
                                       autoFocus
                                       onChange={(e) => {
                                           this.handleSearchFilterPropertyChange('txt', e.target.value)
                                       }}
                                       value={this.state.searchFilter.txt}
                                       placeholder="Términos de búsqueda"
                                       onKeyPress={this.handleEnterKeyPress}
                            />
                        </div>
                        <div className="p-col-12 p-lg-4">
                            <Checkbox id="soloStock"
                                      onChange={(e) => {
                                          this.handleSearchFilterPropertyChange('conStock', e.checked)
                                      }}
                                      checked={this.state.searchFilter.conStock}
                            />
                            <label htmlFor="soloStock" className="p-checkbox-label">Solo con stock</label>
                        </div>

                    </div>
                </TabPanel>
                <TabPanel header="Avanzado" leftIcon="fa fa-fw fa-cog">
                    "Búsqueda avanzada"
                </TabPanel>
            </TabView>
        );
    }

    renderSearchResults() {
        return (
            <DataTable value={this.state.products}
                       paginator={true}
                       rows={this.state.rows}
                       totalRecords={this.state.totalRecords}
                       lazy={true}
                       first={this.state.first}
                       onPage={this.onPageEvent}
                       loading={this.state.loading}
                       selectionMode="single"
                       responsive={true}
                       selection={this.state.selectedProduct}
                       onSelectionChange={e => this.setState({selectedProduct: e.value})}
                       loadingIcon="fa fa-fw fa-spin fa-spinner">

                {this.renderColumns()}

            </DataTable>
        )
    }

    renderColumns() {
        let columns = productColumns.map((col, i) => {
            return <Column key={col.field} field={col.field} header={col.header} style={col.style}/>;
        });

        return columns;
    }

    renderFooter() {
        return (
            <div>
                <Button label="Aceptar" icon="fa fa-fw fa-check" className="p-button-success"
                        disabled={this.state.selectedProduct === null}
                        onClick={this.handleAcceptButton}/>
            </div>
        );
    }

    getDialogProps() {
        let props = this.props;
        let defaultProps = {
            header: "Búsqueda de productos",
            modal: true
        }

        return {...defaultProps, ...props}
    }

    onPageEvent(event) {
        let searchOptions = {
            firstResult: event.first,
            maxResults: this.state.rows,
            searchFilter: this.state.searchFilter
        }

        this.productsService.searchProducts(searchOptions, this.handleSuccess);

        this.setState({
            loading: true,
            first: event.first,
            selectedProduct: null
        });
    }

    handleSuccess(data) {
        const foundProducts = data.data;
        const totalRows = data.totalResults;

        this.setState({
            products: foundProducts,
            totalRecords: totalRows,
            loading: false
        })
    }

    handleAcceptButton() {
        this.props.onHide();

        if (this.props.acceptCallback) {
            this.props.acceptCallback(this.state.selectedProduct);
        }
    }

    handleSearchFilterPropertyChange(property, value) {
        let searchFilter = this.state.searchFilter;

        searchFilter[property] = value;

        this.setState({searchFilter: searchFilter});
    }

    handleEnterKeyPress(event) {
        if (event.key === 'Enter' && !this.state.loading) {
            this.searchProducts();
        }
    }

    searchProducts() {
        const state = this.state;
        let searchOptions = {
            firstResult: 0,
            maxResults: state.rows,
            searchFilter: state.searchFilter
        }

        this.productsService.searchProducts(searchOptions, this.handleSuccess);

        this.setState({
            loading: true,
            first: 0,
            selectedProduct: null
        });
    }
}
