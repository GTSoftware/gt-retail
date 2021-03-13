import React, { Component } from "react"
import { Growl } from "primereact/growl"
import { Dropdown } from "primereact/dropdown"
import { LoadingButton } from "../core/LoadingButton"
import { FiscalBookService } from "../../service/FiscalBookService"

const BOOK_KINDS = [
  { label: "Compras y Ventas", key: "BOTH" },
  { label: "Compras", key: "PURCHASES" },
  {
    label: "Ventas",
    key: "SALES",
  },
]
const EXPORT_OPTIONS = [
  { label: "Régimen informativo", key: "REGINFO" },
  {
    label: "Libro en hoja de cálculo",
    key: "SPREADSHEET",
  },
]

export class FiscalBooks extends Component {
  constructor(props, context) {
    super(props, context)

    this.state = {
      fiscalPeriods: [],
      fiscalPeriod: null,
      kind: BOOK_KINDS[0],
      fileType: EXPORT_OPTIONS[1],
      loading: false,
    }

    this.fiscalBookService = new FiscalBookService()
  }

  componentDidMount() {
    const { fiscalPeriods } = this.state

    if (fiscalPeriods.length === 0) {
      this.fiscalBookService.getFiscalPeriods((fiscalPeriods) =>
        this.setState({
          fiscalPeriods: fiscalPeriods,
          fiscalPeriod: fiscalPeriods[0],
        })
      )
    }
  }

  render() {
    const { fiscalPeriods, fiscalPeriod, kind, fileType } = this.state

    return (
      <div className="card card-w-title">
        <Growl ref={(el) => (this.growl = el)} />

        <h1>Libros de IVA</h1>

        <div className="p-grid p-fluid">
          <div className="p-col-6">
            <label htmlFor="fiscalPeriod">Periodo fiscal:</label>
            <Dropdown
              id="fiscalPeriod"
              optionLabel="nombrePeriodo"
              placeholder="Seleccione un periodo"
              options={fiscalPeriods}
              value={fiscalPeriod}
              onChange={(e) => this.setState({ fiscalPeriod: e.value })}
            />
          </div>

          <div className="p-col-6">
            <label htmlFor="kindOfBook">Tipo de libro:</label>
            <Dropdown
              id="kindOfBook"
              optionLabel="label"
              placeholder="Seleccione un tipo de libro"
              options={BOOK_KINDS}
              value={kind}
              onChange={(e) => this.setState({ kind: e.value })}
            />
          </div>

          <div className="p-col-6">
            <label htmlFor="fileType">Tipo de archivo:</label>
            <Dropdown
              id="fileType"
              optionLabel="label"
              placeholder="Seleccione un tipo de archivo"
              options={EXPORT_OPTIONS}
              value={fileType}
              onChange={(e) => this.setState({ fileType: e.value })}
            />
          </div>

          <LoadingButton
            type="button"
            icon="fa fa-fw fa-download"
            label={"Descargar"}
            className="p-button-success"
            onClick={this.downloadFiscalBook}
            loading={this.state.loading}
            tooltip={"Descargar"}
          />
        </div>
      </div>
    )
  }

  downloadFiscalBook = () => {
    const { fiscalPeriod, kind, fileType } = this.state
    let filter = {
      fiscalPeriodId: fiscalPeriod.id,
      kind: kind.key,
    }
    if (fileType.key === "REGINFO") {
      this.fiscalBookService.getRegInfoFile(
        filter,
        this.handleSuccess,
        this.handleError
      )
    } else {
      this.fiscalBookService.getFiscalBookSpreadsheetFile(
        filter,
        this.handleSuccess,
        this.handleError
      )
    }
    this.setState({ loading: true })
  }

  handleSuccess = () => {
    this.setState({ loading: false })
  }

  handleError = (error) => {
    this.setState({ loading: false })

    this.growl.show({
      severity: "error",
      summary: "No se pudo descargar el archivo",
      detail: error.response.data.message,
    })
  }
}
