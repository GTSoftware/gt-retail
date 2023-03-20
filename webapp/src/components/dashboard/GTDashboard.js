import React, { useEffect, useRef, useState } from "react"
import { DashboardService } from "../../service/DashboardService"
import LoginService from "../../service/LoginService"
import { Chart } from "primereact/chart"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { Button } from "primereact/button"

export const GTDashboard = () => {
  const [loadingSalesQuantity, setLoadingSalesQuantity] = useState(true)
  const [salesQuantity, setSalesQuantity] = useState(null)
  const [loadingNewCustomersQuantity, setLoadingNewCustomersQuantity] =
    useState(true)
  const [newCustomers, setNewCustomers] = useState(null)
  const [loadingYearSalesReport, setLoadingYearSalesReport] = useState(true)
  const [yearSalesReportData, setYearSalesReportData] = useState(null)
  const [loadingStockBreakReport, setLoadingStockBreakReport] = useState(true)
  const [productsWithBreaks, setProductsWithBreaks] = useState([])
  const isUserAdmin = LoginService.hasUserRole("ADMINISTRADORES")
  const dashboardService = new DashboardService()
  const stockBreakTable = useRef(null)

  const loadDashboard = () => {
    dashboardService.getSalesQuantity(handleSalesQuantity)
    dashboardService.getNewCustomersQuantity(handleNewCustomersQuantity)
    if (isUserAdmin) {
      dashboardService.getYearSalesReport(handleYearSalesReport)
      dashboardService.getStockBreakReport(handleStockBreakReport)
    }
  }

  useEffect(loadDashboard, [])

  const handleSalesQuantity = (salesQuantity) => {
    setLoadingSalesQuantity(false)
    setSalesQuantity(salesQuantity)
  }

  const renderSalesQuantitySection = () => {
    let salesQuantitySection = getPlaceholder()

    if (!loadingSalesQuantity) {
      salesQuantitySection = salesQuantity
    }

    return (
      <div className="p-col-12 p-lg-4">
        <div className="card summary">
          <span className="title">{"Ventas"}</span>
          <span className="detail">{"Número de ventas"}</span>
          <span className="count purchases">{salesQuantitySection}</span>
        </div>
      </div>
    )
  }

  const renderNewCustomersSection = () => {
    let customersQuantity = getPlaceholder()

    if (!loadingNewCustomersQuantity) {
      customersQuantity = newCustomers
    }

    return (
      <div className="p-col-12 p-lg-4">
        <div className="card summary">
          <span className="title">{"Clientes"}</span>
          <span className="detail">{"Clientes Nuevos"}</span>
          <span className="count visitors">{customersQuantity}</span>
        </div>
      </div>
    )
  }

  const renderStockBreakReport = () => {
    let reportSection = null
    const header = (
      <div style={{ textAlign: "left" }}>
        <Button
          type="button"
          icon="fa fa-fw fa-download"
          iconPos="left"
          label="CSV"
          onClick={exportStockBreak}
        />
      </div>
    )

    if (isUserAdmin) {
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
              ref={stockBreakTable}
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

  const renderYearSalesReportSection = () => {
    let reportSection = null

    if (isUserAdmin) {
      reportSection = (
        <div className="p-col-12 p-lg-6">
          <div className="card">
            {loadingYearSalesReport ? (
              getPlaceholder()
            ) : (
              <Chart type="line" data={yearSalesReportData} />
            )}
          </div>
        </div>
      )
    }

    return reportSection
  }

  const exportStockBreak = () => {
    stockBreakTable.current.exportCSV({ selectionOnly: false })
  }

  const handleNewCustomersQuantity = (newCustomers) => {
    setLoadingNewCustomersQuantity(false)
    setNewCustomers(newCustomers)
  }

  const getPlaceholder = () => {
    return <span className="fa fa-spinner fa-spin" />
  }

  const handleYearSalesReport = (yearSales) => {
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

    setLoadingYearSalesReport(false)
    setYearSalesReportData(monthlySalesLineData)
  }

  const handleStockBreakReport = (productsWithBreaks) => {
    setLoadingStockBreakReport(false)
    setProductsWithBreaks(productsWithBreaks)
  }

  return (
    <div className="p-grid p-fluid dashboard">
      {renderSalesQuantitySection()}
      {renderNewCustomersSection()}

      {renderYearSalesReportSection()}
      {renderStockBreakReport()}
    </div>
  )
}
