import React, { Component } from "react"
import { DashboardService } from "../../service/DashboardService"
import LoginService from "../../service/LoginService"
import { Chart } from "primereact/chart"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"

export class GTDashboard extends Component {
  constructor(props, context) {
    super(props, context)

    this.state = {
      loadingSalesQuantity: true,
      loadingNewCustomersQuantity: true,
      loadingYearSalesReport: true,
      loadingStockBreakReport: true,
      yearSalesReportData: null,
    }
    this.isUserAdmin = LoginService.hasUserRole("ADMINISTRADORES")
    this.dashboardService = new DashboardService()
  }

  componentDidMount() {
    const {
      loadingSalesQuantity,
      loadingNewCustomersQuantity,
      loadingYearSalesReport,
      loadingStockBreakReport,
    } = this.state

    if (loadingSalesQuantity) {
      this.dashboardService.getSalesQuantity(this.handleSalesQuantity)
    }
    if (loadingNewCustomersQuantity) {
      this.dashboardService.getNewCustomersQuantity(this.handleNewCustomersQuantity)
    }
    if (this.isUserAdmin && loadingYearSalesReport) {
      this.dashboardService.getYearSalesReport(this.handleYearSalesReport)
    }
    if (this.isUserAdmin && loadingStockBreakReport) {
      this.dashboardService.getStockBreakReport(this.handleStockBreakReport)
    }
  }

  render() {
    return (
      <div className="p-grid p-fluid dashboard">
        {this.renderSalesQuantitySection()}
        {this.renderNewCustomersSection()}

        {this.renderYearSalesReportSection()}
        {this.renderStockBreakReport()}
      </div>
    )
  }

  handleSalesQuantity = (salesQuantity) => {
    this.setState({
      loadingSalesQuantity: false,
      salesQuantity: salesQuantity,
    })
  }

  renderSalesQuantitySection = () => {
    const { salesQuantity, loadingSalesQuantity } = this.state
    let salesQuantitySection = this.getPlaceholder()

    if (!loadingSalesQuantity) {
      salesQuantitySection = salesQuantity
    }

    return (
      <div className="p-col-12 p-lg-4">
        <div className="card summary">
          <span className="title">Ventas</span>
          <span className="detail">Número de ventas</span>
          <span className="count purchases">{salesQuantitySection}</span>
        </div>
      </div>
    )
  }

  renderNewCustomersSection = () => {
    const { newCustomers, loadingNewCustomersQuantity } = this.state
    let customersQuantity = this.getPlaceholder()

    if (!loadingNewCustomersQuantity) {
      customersQuantity = newCustomers
    }

    return (
      <div className="p-col-12 p-lg-4">
        <div className="card summary">
          <span className="title">Clientes</span>
          <span className="detail">Clientes Nuevos</span>
          <span className="count visitors">{customersQuantity}</span>
        </div>
      </div>
    )
  }

  renderStockBreakReport = () => {
    let reportSection = null
    const { loadingStockBreakReport, productsWithBreaks } = this.state
    var header = (
      <div style={{ textAlign: "left" }}>
        <Button
          type="button"
          icon="fa fa-fw fa-download"
          iconPos="left"
          label="CSV"
          onClick={this.exportStockBreak}
        />
      </div>
    )

    if (this.isUserAdmin) {
      reportSection = (
        <div className="p-col-12 p-lg-6">
          <div className="card">
            <h1 style={{ fontSize: "16px" }}>Quiebres de stock</h1>
            <DataTable
              value={productsWithBreaks}
              style={{ marginBottom: "20px" }}
              loadingIcon="fa fa-spin fa-spinner"
              loading={loadingStockBreakReport}
              rows={5}
              paginator={true}
              header={header}
              emptyMessage={"Genial! No hay productos sin stock éste mes."}
              ref={(el) => {
                this.stockBreakTable = el
              }}
            >
              <Column field="productCode" header="Código" sortable={true} />
              <Column
                field="description"
                style={{ width: "40%" }}
                header="Descripción"
                sortable={true}
              />
              <Column field="saleUnit" header="Unidad" sortable={true} />
              <Column field="minimumStock" header="Mínimo" sortable={true} />
              <Column field="branchStock" header="Actual" sortable={true} />
            </DataTable>
          </div>
        </div>
      )
    }

    return reportSection
  }

  renderYearSalesReportSection = () => {
    let reportSection = null
    const { loadingYearSalesReport, yearSalesReportData } = this.state

    if (this.isUserAdmin) {
      reportSection = (
        <div className="p-col-12 p-lg-6">
          <div className="card">
            {loadingYearSalesReport ? (
              this.getPlaceholder()
            ) : (
              <Chart type="line" data={yearSalesReportData} />
            )}
          </div>
        </div>
      )
    }

    return reportSection
  }

  exportStockBreak = () => {
    this.stockBreakTable.exportCSV()
  }

  handleNewCustomersQuantity = (newCustomers) => {
    this.setState({
      loadingNewCustomersQuantity: false,
      newCustomers: newCustomers,
    })
  }

  getPlaceholder = () => {
    return <span className="fa fa-spinner fa-spin" />
  }

  handleYearSalesReport = (yearSales) => {
    let monthlySalesLineData = {
      labels: yearSales.map((period) => {
        return period.period
      }),
      datasets: [
        {
          label: "Totales de ventas mensuales",
          data: yearSales.map((period) => {
            return period.amount
          }),
          fill: false,
          borderColor: "#007be5",
        },
      ],
    }

    this.setState({
      loadingYearSalesReport: false,
      yearSalesReportData: monthlySalesLineData,
    })
  }

  handleStockBreakReport = (productsWithBreaks) => {
    this.setState({
      loadingStockBreakReport: false,
      productsWithBreaks: productsWithBreaks,
    })
  }
}
