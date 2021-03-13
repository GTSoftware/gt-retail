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

    this.handleStartNewSale = this.handleStartNewSale.bind(this)
    this.renderDeliveryNote = this.renderDeliveryNote.bind(this)
    this.renderPrintDeliveryNoteButton = this.renderPrintDeliveryNoteButton.bind(
      this
    )
  }

  render() {
    const saleInformation = this.state.saleInformation

    return (
      <div className="card card-w-title">
        <div className="p-card-body p-fluid ">
          <h1>¡Comprobante generado con éxito!</h1>
          <div className="p-grid">
            <div className="p-col-12 p-md-4">
              <label htmlFor="saleId">Número de comprobante: </label>
              <InputText
                id="saleId"
                readOnly={true}
                value={saleInformation.saleId}
              />
            </div>
            {this.renderDeliveryNote()}
          </div>
        </div>

        <div className="p-card-footer">
          <div className="p-col p-justify-end p-align-end p-grid">
            <Button
              label="Iniciar nueva venta"
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
              FileOutputsService.getSaleBudget(this.state.saleInformation.saleId)
            }}
          />
          {this.renderPrintDeliveryNoteButton()}
        </div>
      </div>
    )
  }

  renderDeliveryNote() {
    const saleInformation = this.state.saleInformation

    return (
      saleInformation.deliveryNoteId && (
        <div className="p-col-12 p-md-4">
          <label htmlFor="remito">Remito: </label>
          <InputText
            id="remito"
            readOnly={true}
            value={saleInformation.deliveryNoteId}
          />
        </div>
      )
    )
  }

  renderPrintDeliveryNoteButton() {
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

  handleStartNewSale() {
    this.props.startNewSaleCallback()
  }
}
