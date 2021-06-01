import React, { Component } from "react"
import classNames from "classnames"
import { AppTopbar } from "./AppTopbar"
import { AppFooter } from "./AppFooter"
import { AppMenu } from "./AppMenu"
import { AppProfile } from "./AppProfile"
import { Redirect, Route, Switch } from "react-router-dom"
import "primereact/resources/themes/nova/theme.css"
import "primereact/resources/primereact.min.css"
import "primeicons/primeicons.css"
import "primeflex/primeflex.css"
import "@fullcalendar/core/main.css"
import "@fullcalendar/daygrid/main.css"
import "@fullcalendar/timegrid/main.css"
import "./layout/layout.scss"
import "./App.scss"
import { Login } from "./components/Login"
import LoginService from "./service/LoginService.js"
import { UsersList } from "./components/UsersList"
import { ShopCart } from "./components/shop-cart/ShopCart"
import { PageNotFound } from "./components/PageNotFound"
import { GTDashboard } from "./components/dashboard/GTDashboard"
import { ManualDeliveryNote } from "./components/delivery-note/ManualDeliveryNote"
import { BatchPricing } from "./components/pricing/BatchPricing"
import { ProductHistory } from "./components/stock/ProductHistory"
import { SearchDeliveryNotes } from "./components/delivery-note/SearchDeliveryNotes"
import { SearchSales } from "./components/sale/SearchSales"
import { ViewSale } from "./components/sale/ViewSale"
import { SalesByProductReport } from "./components/report/SalesByProductReport"
import { SearchPendingSales } from "./components/payment/SearchPendingSales"
import Helmet from "react-helmet"
import { FiscalBooks } from "./components/fiscal/FiscalBooks"
import { ProductsInventory } from "./components/products/ProductsInventory"
import { ProductDetails } from "./components/products/ProductDetails"

const ProtectedRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={(props) =>
      LoginService.isUserLoggedIn() ? (
        <Component {...props} />
      ) : (
        <Redirect to="/login" />
      )
    }
  />
)

class App extends Component {
  constructor() {
    super()
    this.state = {
      layoutMode: "overlay",
      layoutColorMode: "dark",
      staticMenuInactive: false,
      overlayMenuActive: false,
      mobileMenuActive: false,
      userLoggedIn: false,
      menu: this.getDefaultMenuItems(),
    }

    this.onWrapperClick = this.onWrapperClick.bind(this)
    this.onToggleMenu = this.onToggleMenu.bind(this)
    this.onSidebarClick = this.onSidebarClick.bind(this)
    this.onMenuItemClick = this.onMenuItemClick.bind(this)
  }

  componentDidMount() {
    if (LoginService.isUserLoggedIn()) {
      this.loadLoginStatus()
    }
  }

  onWrapperClick(event) {
    if (!this.menuClick) {
      this.setState({
        overlayMenuActive: false,
        mobileMenuActive: false,
      })
    }

    this.menuClick = false
  }

  onToggleMenu(event) {
    this.menuClick = true

    if (this.isDesktop()) {
      if (this.state.layoutMode === "overlay") {
        this.setState({
          overlayMenuActive: !this.state.overlayMenuActive,
        })
      } else if (this.state.layoutMode === "static") {
        this.setState({
          staticMenuInactive: !this.state.staticMenuInactive,
        })
      }
    } else {
      const mobileMenuActive = this.state.mobileMenuActive
      this.setState({
        mobileMenuActive: !mobileMenuActive,
      })
    }

    event.preventDefault()
  }

  onSidebarClick(event) {
    this.menuClick = true
  }

  onMenuItemClick(event) {
    if (!event.item.items) {
      this.setState({
        overlayMenuActive: false,
        mobileMenuActive: false,
      })
    }
  }

  getDefaultMenuItems = () => {
    return [
      {
        label: "Dashboard",
        icon: "fa fa-fw fa-home",
        command: () => {
          window.location = "#/"
        },
      },
    ]
  }

  getDeliveryNotesMenuItem = () => {
    return {
      label: "Remitos",
      icon: "fa fa-fw fa-truck-loading",
      items: [
        {
          label: "Listado de remitos",
          icon: "fa fa-fw fa-list",
          command: () => {
            window.location = "#/delivery-notes"
          },
        },
        {
          label: "Nuevo Remito",
          icon: "fa fa-fw fa-plus",
          command: () => {
            window.location = "#/delivery-note"
          },
        },
        {
          label: "Movimientos de un producto",
          icon: "fa fa-fw fa-history",
          command: () => {
            window.location = "#/product-history"
          },
        },
      ],
    }
  }

  getProductsMenuItem = () => {
    return {
      label: "Productos",
      icon: "fa fa-fw fa-boxes",
      items: [
        {
          label: "Mayor de produtos",
          icon: "fa fa-fw fa-boxes",
          command: () => {
            window.location = "#/products"
          },
        },
        {
          label: "Actualización masiva de precios",
          icon: "fa fa-fw fa-calculator",
          command: () => {
            window.location = "#/batch-pricing"
          },
        },
      ],
    }
  }

  getFiscalMenuItem = () => {
    return {
      label: "Fiscal",
      icon: "fa fa-fw fa-book",
      items: [
        {
          label: "Libros de IVA",
          icon: "fa fa-fw fa-book-open",
          command: () => {
            window.location = "#/fiscal-books"
          },
        },
      ],
    }
  }

  getSalesMenuItem = () => {
    return {
      label: "Ventas",
      icon: "fa fa-fw fa-shopping-cart",
      items: [
        {
          label: "Nueva",
          icon: "fa fa-fw fa-cart-plus",
          command: () => {
            window.location = "#/shop-cart"
          },
        },
        {
          label: "Listado de ventas",
          icon: "fa fa-fw fa-list",
          command: () => {
            window.location = "#/sales"
          },
        },
      ],
    }
  }

  getReportsMenuItem = () => {
    return {
      label: "Reportes",
      icon: "fa fa-fw fa-chart-line",
      items: [
        {
          label: "Ventas por producto",
          icon: "fa fa-fw fa-shopping-cart",
          command: () => {
            window.location = "#/sales-product"
          },
        },
      ],
    }
  }

  getPaymentsMenuItem = () => {
    return {
      label: "Caja",
      icon: "fa fa-fw fa-cash-register",
      items: [
        {
          label: "Comprobantes pendientes de cobro",
          icon: "fa fa-fw fa-file-invoice",
          command: () => {
            window.location = "#/payment-pending-sales"
          },
        },
      ],
    }
  }

  addClass(element, className) {
    if (element.classList) element.classList.add(className)
    else element.className += " " + className
  }

  removeClass(element, className) {
    if (element.classList) element.classList.remove(className)
    else
      element.className = element.className.replace(
        new RegExp("(^|\\b)" + className.split(" ").join("|") + "(\\b|$)", "gi"),
        " "
      )
  }

  isDesktop() {
    return window.innerWidth > 1024
  }

  componentDidUpdate() {
    if (this.state.mobileMenuActive) {
      this.addClass(document.body, "body-overflow-hidden")
    } else {
      this.removeClass(document.body, "body-overflow-hidden")
    }
  }

  render() {
    const isUserLoggedIn = this.state.userLoggedIn
    const logo =
      this.state.layoutColorMode === "dark"
        ? "assets/layout/images/logo-gt.svg"
        : "assets/layout/images/logo-gt-dark.svg"

    const wrapperClass = classNames("layout-wrapper", {
      "layout-overlay": this.state.layoutMode === "overlay",
      "layout-static": this.state.layoutMode === "static",
      "layout-static-sidebar-inactive":
        this.state.staticMenuInactive && this.state.layoutMode === "static",
      "layout-overlay-sidebar-active":
        this.state.overlayMenuActive && this.state.layoutMode === "overlay",
      "layout-mobile-sidebar-active": this.state.mobileMenuActive,
    })

    const sidebarClassName = classNames("layout-sidebar", {
      "layout-sidebar-dark": this.state.layoutColorMode === "dark",
      "layout-sidebar-light": this.state.layoutColorMode === "light",
    })

    return (
      <div className={wrapperClass} onClick={this.onWrapperClick}>
        <Helmet>
          <title>
            {isUserLoggedIn
              ? LoginService.getUserDetails().completeUserName + " | GT Retail"
              : "GT Retail"}
          </title>
        </Helmet>
        {isUserLoggedIn && <AppTopbar onToggleMenu={this.onToggleMenu} />}

        {isUserLoggedIn && (
          <div
            ref={(el) => (this.sidebar = el)}
            className={sidebarClassName}
            onClick={this.onSidebarClick}
          >
            <div className="layout-logo">
              <img alt="Logo" src={logo} style={{ height: "40px" }} />
            </div>
            <AppProfile onLogout={() => this.resetLoginStatus()} />
            <AppMenu
              model={this.state.menu}
              onMenuItemClick={this.onMenuItemClick}
            />
          </div>
        )}

        <div className="layout-main">
          <Switch>
            <Route
              path="/login"
              render={(props) => (
                <Login {...props} onLoginSuccess={() => this.loadLoginStatus()} />
              )}
            />
            <ProtectedRoute path="/" exact component={GTDashboard} />
            <ProtectedRoute path="/users" component={UsersList} />
            <ProtectedRoute path="/shop-cart" component={ShopCart} />
            <ProtectedRoute path="/delivery-note" component={ManualDeliveryNote} />
            <ProtectedRoute path="/batch-pricing" component={BatchPricing} />
            <ProtectedRoute path="/product-history" component={ProductHistory} />
            <ProtectedRoute path="/delivery-notes" component={SearchDeliveryNotes} />
            <ProtectedRoute path="/sales" component={SearchSales} />
            <ProtectedRoute exact path="/sale/:saleId" component={ViewSale} />
            <ProtectedRoute path="/sales-product" component={SalesByProductReport} />
            <ProtectedRoute
              path="/payment-pending-sales"
              component={SearchPendingSales}
            />
            <ProtectedRoute path="/fiscal-books" component={FiscalBooks} />
            <ProtectedRoute path="/products" component={ProductsInventory} />
            <ProtectedRoute path="/product/:productId" component={ProductDetails} />
            <Route component={PageNotFound} />
          </Switch>
        </div>

        {isUserLoggedIn && <AppFooter />}

        <div className="layout-mask" />
      </div>
    )
  }

  resetLoginStatus = () => {
    this.setState({
      userLoggedIn: false,
      adminUser: false,
      stockManUser: false,
      salesManUser: false,
      menu: this.getDefaultMenuItems(),
    })
  }

  loadLoginStatus = () => {
    const adminUser = LoginService.hasUserRole("ADMINISTRADORES")
    const stockManUser = LoginService.hasUserRole("STOCK_MEN")
    const salesManUser = LoginService.hasUserRole("VENDEDORES")
    const cashierUser = LoginService.hasUserRole("CAJEROS")
    let roleDependantMenu = this.getDefaultMenuItems()

    if (adminUser || salesManUser) {
      roleDependantMenu = roleDependantMenu.concat(this.getSalesMenuItem())
    }

    if (adminUser || stockManUser) {
      roleDependantMenu = roleDependantMenu.concat(this.getDeliveryNotesMenuItem())
    }

    if (adminUser) {
      roleDependantMenu = roleDependantMenu
        .concat(this.getProductsMenuItem())
        .concat(this.getReportsMenuItem())
        .concat(this.getFiscalMenuItem())
    }

    if (adminUser || cashierUser) {
      roleDependantMenu = roleDependantMenu.concat(this.getPaymentsMenuItem())
    }

    this.setState({
      userLoggedIn: true,
      adminUser: adminUser,
      stockManUser: stockManUser,
      salesManUser: salesManUser,
      menu: roleDependantMenu,
    })
  }
}

export default App
