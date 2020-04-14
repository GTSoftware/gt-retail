import React, {Component} from "react";
import {Steps} from "primereact/steps";
import {ShopCartStore} from "../stores/ShopCartStore";
import {ShopCartItems} from "./ShopCartItems";
import {ShopCartCustomer} from "./ShopCartCustomer";
import {ShopCartPayment} from "./ShopCartPayment";
import './ShopCart.scss';
import {ShopCartConfirmation} from "./ShopCartConfirmation";

const saleSteps = [
    {number: 1, label: 'Productos'},
    {number: 2, label: 'Cliente'},
    {number: 3, label: 'Pago'},
    {number: 4, label: 'Confirmación'}
];

export class ShopCart extends Component {

    constructor() {
        super();

        this.shopCartStore = new ShopCartStore();

        this.state = {
            saleStep: this.shopCartStore.getSaleStep() || 0
        };

        this.renderSaleStepComponent = this.renderSaleStepComponent.bind(this);
        this.renderShopCartItems = this.renderShopCartItems.bind(this);
        this.renderShopCartClient = this.renderShopCartClient.bind(this);
        this.renderShopCartPayment = this.renderShopCartPayment.bind(this);
        this.renderSaleConfirmation = this.renderSaleConfirmation.bind(this);
        this.goBack = this.goBack.bind(this);
        this.goNext = this.goNext.bind(this);
        this.getChildProps = this.getChildProps.bind(this);
        this.startNewSale = this.startNewSale.bind(this);
    }

    render() {
        return (
            <div className="card card-w-title">
                <Steps model={saleSteps} activeIndex={this.state.saleStep}/>
                {this.renderSaleStepComponent()}
            </div>
        )
    }

    renderSaleStepComponent() {
        switch (this.state.saleStep) {
            case 0:
                return this.renderShopCartItems();
            case 1:
                return this.renderShopCartClient();
            case 2:
                return this.renderShopCartPayment();
            case 3:
                return this.renderSaleConfirmation();
            default:
                return this.renderShopCartItems();
        }
    }

    renderShopCartItems() {
        return (
            <ShopCartItems {...this.getChildProps()}/>
        );
    }

    renderShopCartClient() {
        return (
            <ShopCartCustomer {...this.getChildProps()}/>
        )
    }

    renderShopCartPayment() {
        return (
            <ShopCartPayment {...this.getChildProps()} />
        )
    }

    renderSaleConfirmation() {
        return <ShopCartConfirmation {...this.getChildProps()}/>
    }

    getChildProps() {
        return {
            goNextCallback: this.goNext,
            goBackCallback: this.goBack,
            startNewSaleCallback: this.startNewSale
        }
    }

    goNext() {
        let saleStep = this.state.saleStep;
        if (saleStep < 3) {
            this.setState({saleStep: saleStep + 1});
            this.shopCartStore.setSaleStep(saleStep + 1);
        }
    }

    goBack() {
        let saleStep = this.state.saleStep;
        if (saleStep > 0) {
            this.setState({saleStep: saleStep - 1});
            this.shopCartStore.setSaleStep(saleStep - 1);
        }
    }

    startNewSale() {
        this.shopCartStore.clearStore();
        this.setState({saleStep: 0});
    }

}
