import _ from "lodash"
import React, { useRef, useState } from "react"
import { CategoriesService } from "../../../service/CategoriesService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../../utils/DateUtils"
import { Button } from "primereact/button"
import { LoadingButton } from "../../core/LoadingButton"
import { AddTaxDialog } from "../../suppliers/AddTaxDialog"
import { AddEditCategoryDialog } from "./AddEditCategoryDialog"
import { Toast } from "primereact/toast"

const categoriesColumns = [
  // Modify with your category property schema
  { field: "categoryId", header: "Id" },
  { field: "categoryName", header: "Nombre" },
]

export const CategoriesInventory = () => {
  const categoriesService = new CategoriesService()

  const [loading, setLoading] = useState(false)
  const [categories, setCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState(null)
  const [showEditCategory, setShowEditCategory] = useState(false)
  const [showAddCategory, setShowAddCategory] = useState(false)
  const toast = useRef(null)

  const getColumnBody = (col, rowData) => {
    const field = _.get(rowData, col.field)

    if (col.format) {
      return col.format(field)
    }

    return field
  }

  const getLinkActions = (rowData) => {
    const { categoryId } = rowData

    return (
      <div>
        <Button
          type="button"
          icon="fa fa-fw fa-edit"
          tooltip={"Editar rubro"}
          onClick={() => {
            setSelectedCategory(rowData)
            setShowEditCategory(true)
          }}
        />
        <Button
          type="button"
          icon="fa fa-fw fa-briefcase"
          tooltip={"Ver sub rubros"}
          onClick={() => {
            console.log(categoryId)
          }}
        />
      </div>
    )
  }

  const renderColumns = () => {
    let columns = categoriesColumns.map((col, i) => {
      return (
        <Column
          key={col.field}
          field={col.field}
          header={col.header}
          style={col.style}
          body={(rowData) => getColumnBody(col, rowData)}
        />
      )
    })

    columns.push(<Column body={getLinkActions} />)

    return columns
  }

  const searchCategories = () => {
    if (!loading) {
      setLoading(true)

      categoriesService.getCategories((categories) => {
        setCategories(categories)
      })

      setLoading(false)
    }
  }

  const renderSearchResults = () => {
    return (
      <DataTable
        value={categories}
        dataKey={"categoryId"}
        loading={loading}
        loadingIcon="fa fa-fw fa-spin fa-spinner"
        resizableColumns
      >
        {renderColumns()}
      </DataTable>
    )
  }

  const renderSearchButton = () => {
    return (
      <div className="p-grid p-fluid">
        <div className="p-col-6 p-lg-6">
          <LoadingButton
            type="button"
            icon="fa fa-fw fa-filter"
            loading={loading}
            label={"Buscar"}
            onClick={() => searchCategories()}
          />
        </div>
        <div className="p-col-6 p-lg-6">
          <Button
            type="button"
            className="p-button-success"
            icon="fa fa-fw fa-plus"
            loading={loading}
            label={"Crear rubro"}
            onClick={() => setShowAddCategory(true)}
          />
        </div>
      </div>
    )
  }

  const handleEditCategory = (category) => {
    setLoading(true)
    categoriesService.updateCategory(
      category.categoryId,
      category,
      () => {
        toast.current.show({
          severity: "success",
          summary: `Rubro edidado con éxito: ${category.categoryId}`,
        })
        setLoading(false)
        searchCategories()
      },
      () => {
        toast.current.show({
          severity: "error",
          summary: `Error al editar el rubro: ${category.categoryId}`,
        })
        setLoading(false)
      }
    )
  }

  const renderShowEditCategoryDialog = () => {
    return (
      <AddEditCategoryDialog
        visible={showEditCategory}
        category={selectedCategory}
        modal={true}
        acceptCallback={handleEditCategory}
        onHide={() => setShowEditCategory(false)}
      />
    )
  }

  const handleAddCategory = (category) => {
    setLoading(true)
    categoriesService.createCategory(
      category,
      (newCategory) => {
        toast.current.show({
          severity: "success",
          summary: `Rubro creado con éxito: ${newCategory.categoryId}`,
        })
        setLoading(false)
        searchCategories()
      },
      () => {
        toast.current.show({
          severity: "error",
          summary: `Error al crear el rubro: ${category.categoryId}`,
        })
        setLoading(false)
      }
    )
  }

  const renderShowAddCategoryDialog = () => {
    return (
      <AddEditCategoryDialog
        visible={showAddCategory}
        modal={true}
        acceptCallback={handleAddCategory}
        onHide={() => setShowAddCategory(false)}
      />
    )
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Mayor de Rubros</h1>
      {showEditCategory && renderShowEditCategoryDialog()}
      {showAddCategory && renderShowAddCategoryDialog()}
      {renderSearchButton()}
      {renderSearchResults()}
    </div>
  )
}
