import React, { Component } from "react"
import { Button } from "primereact/button"
import { InputText } from "primereact/inputtext"
import { ShopCartStore } from "../../stores/ShopCartStore"
import FileOutputsService from "../../service/FileOutputsService"

export class ShopCartConfirmation extends Component {
  constructor(props) {
    super(props)

    this.shopCartStore = new ShopCartStore()

    this.state = {
      saleInformation: this.shopCartStore.getData(),
    }
  }

  render() {
    const { saleId } = this.state.saleInformation

    return (
      <div className="card card-w-title">
        <div className="p-card-body p-fluid ">
          <h1>{"¡Comprobante generado con éxito!"}</h1>
          <div className="p-grid">
            <div className="p-col-12 p-md-4">
              <label htmlFor="saleId">{"Número de comprobante:"}</label>
              <InputText id="saleId" readOnly={true} value={saleId} />
            </div>
            {this.renderDeliveryNote()}
          </div>
        </div>

        <div className="p-card-footer">
          <div className="p-col p-justify-end p-align-end p-grid">
            <Button
              label={"Iniciar nueva venta"}
              className="shop-cart--footer-actions-button"
              icon="fa fa-fw fa-plus"
              onClick={() => this.handleStartNewSale()}
            />
          </div>
          <Button
            label="Imprimir Presupuesto"
            icon="fa fa-fw fa-print"
            className="p-button-secondary"
            onClick={() => {
              FileOutputsService.getSaleBudget(saleId)
            }}
          />
          {this.renderPrintDeliveryNoteButton()}
        </div>
      </div>
    )
  }

  renderDeliveryNote = () => {
    const { deliveryNoteId } = this.state.saleInformation

    return (
      deliveryNoteId && (
        <div className="p-col-12 p-md-4">
          <label htmlFor="remito">{"Remito: "}</label>
          <InputText id="remito" readOnly={true} value={deliveryNoteId} />
        </div>
      )
    )
  }

  renderPrintDeliveryNoteButton = () => {
    let button = null

    if (this.state.saleInformation.deliveryNoteId) {
      button = (
        <Button
          label="Imprimir Remito"
          icon="fa fa-fw fa-print"
          className="p-button-secondary"
          onClick={() => {
            FileOutputsService.getDeliveryNote(
              this.state.saleInformation.deliveryNoteId
            )
          }}
        />
      )
    }

    return button
  }

  handleStartNewSale = () => {
    this.props.startNewSaleCallback()
  }
}
