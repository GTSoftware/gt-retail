import React, { useState } from "react"
import { Steps } from "primereact/steps"
import { ShopCartStore } from "../../stores/ShopCartStore"
import { ShopCartItems } from "./ShopCartItems"
import { ShopCartCustomer } from "./ShopCartCustomer"
import { ShopCartPayment } from "./ShopCartPayment"
import "./ShopCart.scss"
import { ShopCartConfirmation } from "./ShopCartConfirmation"

const saleSteps = [
  { number: 1, label: "Productos" },
  { number: 2, label: "Cliente" },
  { number: 3, label: "Pago" },
  { number: 4, label: "Confirmación" },
]

export const ShopCart = () => {
  const shopCartStore = new ShopCartStore()
  const [saleStep, setSaleStep] = useState(shopCartStore.getSaleStep() || 0)

  const renderSaleStepComponent = () => {
    switch (saleStep) {
      case 0:
        return renderShopCartItems()
      case 1:
        return renderShopCartClient()
      case 2:
        return renderShopCartPayment()
      case 3:
        return renderSaleConfirmation()
      default:
        return renderShopCartItems()
    }
  }

  const renderShopCartItems = () => {
    return <ShopCartItems {...getChildProps()} />
  }

  const renderShopCartClient = () => {
    return <ShopCartCustomer {...getChildProps()} />
  }

  const renderShopCartPayment = () => {
    return <ShopCartPayment {...getChildProps()} />
  }

  const renderSaleConfirmation = () => {
    return <ShopCartConfirmation {...getChildProps()} />
  }

  const getChildProps = () => {
    return {
      goNextCallback: goNext,
      goBackCallback: goBack,
      startNewSaleCallback: startNewSale,
    }
  }

  const goNext = () => {
    if (saleStep < 3) {
      const nextStep = saleStep + 1
      setSaleStep(nextStep)
      shopCartStore.setSaleStep(nextStep)
    }
  }

  const goBack = () => {
    if (saleStep > 0) {
      const previousStep = saleStep - 1
      setSaleStep(previousStep)
      shopCartStore.setSaleStep(previousStep)
    }
  }

  const startNewSale = () => {
    shopCartStore.clearStore()
    setSaleStep(0)
  }

  return (
    <div className="card card-w-title">
      <Steps model={saleSteps} activeIndex={saleStep} />
      {renderSaleStepComponent()}
    </div>
  )
}
