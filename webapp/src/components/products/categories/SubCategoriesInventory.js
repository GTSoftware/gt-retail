import _ from "lodash"
import React, { useEffect, useRef, useState } from "react"
import { CategoriesService } from "../../../service/CategoriesService"
import { DataTable } from "primereact/datatable"
import { Column } from "primereact/column"
import { formatDate } from "../../../utils/DateUtils"
import { Button } from "primereact/button"
import { LoadingButton } from "../../core/LoadingButton"
import { AddTaxDialog } from "../../suppliers/AddTaxDialog"
import { Toast } from "primereact/toast"
import { AddEditSubCategoryDialog } from "./AddEditSubCategoryDialog"

const categoriesColumns = [
  // Modify with your category property schema
  { field: "subCategoryId", header: "Id" },
  { field: "subCategoryName", header: "Nombre" },
]

export const SubCategoriesInventory = (props) => {
  const categoriesService = new CategoriesService()

  const [loading, setLoading] = useState(false)
  const categoryId = props.match.params.categoryId
  const [category, setCategory] = useState({})
  const [subCategories, setSubCategories] = useState([])
  const [selectedSubCategory, setSelectedSubCategory] = useState(null)
  const [showEditCategory, setShowEditSubCategory] = useState(false)
  const [showAddCategory, setShowAddCategory] = useState(false)
  const toast = useRef(null)

  useEffect(() => {
    categoriesService.getCategory(categoryId, (cate) => setCategory(cate))
    categoriesService.getSubCategories(categoryId, (cate) => setSubCategories(cate))
  }, [categoryId])

  const getColumnBody = (col, rowData) => {
    const field = _.get(rowData, col.field)

    if (col.format) {
      return col.format(field)
    }

    return field
  }

  const getLinkActions = (rowData) => {
    const { subCategoryId } = rowData

    return (
      <div>
        <Button
          type="button"
          icon="fa fa-fw fa-edit"
          tooltip={"Editar sub-rubro"}
          onClick={() => {
            setSelectedSubCategory(rowData)
            setShowEditSubCategory(true)
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

  const renderSearchResults = () => {
    return (
      <DataTable
        value={subCategories}
        dataKey={"subCategoryId"}
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
        <div className="p-col-12 p-lg-12">
          <Button
            type="button"
            className="p-button-success"
            icon="fa fa-fw fa-plus"
            loading={loading}
            label={"Crear sub-rubro"}
            onClick={() => setShowAddCategory(true)}
          />
        </div>
      </div>
    )
  }

  const handleEditSubCategory = (category) => {
    setLoading(true)
    categoriesService.updateSubCategory(
      category,
      () => {
        toast.current.show({
          severity: "success",
          summary: `Sub-Rubro edidado con éxito: ${category.subCategoryId}`,
        })
        categoriesService.getSubCategories(categoryId, (cate) =>
          setSubCategories(cate)
        )
        setLoading(false)
      },
      () => {
        toast.current.show({
          severity: "error",
          summary: `Error al editar el sub-rubro: ${category.subCategoryId}`,
        })
        setLoading(false)
      }
    )
  }

  const renderShowEditSubCategoryDialog = () => {
    return (
      <AddEditSubCategoryDialog
        visible={showEditCategory}
        subCategory={selectedSubCategory}
        modal={true}
        acceptCallback={handleEditSubCategory}
        onHide={() => setShowEditSubCategory(false)}
      />
    )
  }

  const handleAddSubCategory = (subCategory) => {
    setLoading(true)
    categoriesService.createSubCategory(
      categoryId,
      subCategory,
      (newCategory) => {
        toast.current.show({
          severity: "success",
          summary: `Sub-Rubro creado con éxito: ${newCategory.subCategoryId}`,
        })
        categoriesService.getSubCategories(categoryId, (cate) =>
          setSubCategories(cate)
        )
        setLoading(false)
      },
      () => {
        toast.current.show({
          severity: "error",
          summary: `Error al crear el sub-rubro: ${subCategory.subCategoryId}`,
        })
        setLoading(false)
      }
    )
  }

  const renderShowAddSubCategoryDialog = () => {
    return (
      <AddEditSubCategoryDialog
        visible={showAddCategory}
        modal={true}
        acceptCallback={handleAddSubCategory}
        onHide={() => setShowAddCategory(false)}
      />
    )
  }

  return (
    <div className="card card-w-title">
      <Toast ref={toast} />
      <h1>Sub-rubros del rubro: {category.displayName}</h1>
      {showEditCategory && renderShowEditSubCategoryDialog()}
      {showAddCategory && renderShowAddSubCategoryDialog()}
      {renderSearchButton()}
      {renderSearchResults()}
    </div>
  )
}
