import React, {Component} from "react";
import {ProductsService} from "../../service/ProductsService";
import {AutoComplete} from "primereact/autocomplete";
import {Button} from "primereact/button";
import {SearchProductsDialog} from "./SearchProductsDialog";
import PropTypes from "prop-types";


export class AutocompleteProductFilter extends Component {

    static propTypes = {
        onProductSelect: PropTypes.func.isRequired,
        selectedProduct: PropTypes.object
    }

    constructor(props, context) {
        super(props, context);

        this.state = {
            showSearchProductsDialog: false,
            filteredProducts: []
        }

        this.productsService = new ProductsService();
    }


    render() {
        const {filteredProducts} = this.state;

        return (
            <div className="p-inputgroup">

                <AutoComplete minLength={2}
                              placeholder="Comience a escribir para buscar un producto"
                              autoFocus={true}
                              delay={500}
                              id="producto"
                              completeMethod={(event) => this.filterProducts(event.query)}
                              suggestions={filteredProducts}
                              field="descripcion"
                              required={true}
                              onChange={(event) => this.props.onProductSelect(event.value)}
                              value={this.props.selectedProduct}

                />

                <Button type="button"
                        icon="fa fa-fw fa-search"
                        tooltip={'Buscar productos'}
                        onClick={() => this.setState({showSearchProductsDialog: true})}
                />
                {this.renderSearchProductsDialog()}
            </div>
        )
    }

    renderSearchProductsDialog = () => {
        return (
            <SearchProductsDialog visible={this.state.showSearchProductsDialog}
                                  modal={true}
                                  acceptCallback={this.props.onProductSelect}
                                  onHide={() => this.setState({showSearchProductsDialog: false})}/>
        )
    }

    filterProducts = (query) => {
        let searchOptions = {
            searchFilter: {
                activo: true,
                puedeVenderse: true, //TODO get from props
                buscarEnTodosLados: true,
                txt: query,
                sortFields: [{
                    fieldName: "descripcion",
                    ascending: true
                }],
            },
            firstResult: 0,
            maxResults: 5
        }

        this.productsService.searchProducts(searchOptions,
            (response) => this.setState({filteredProducts: response.data}));
    }
}
