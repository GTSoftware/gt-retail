import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { CategoriesService } from "../../service/CategoriesService"

export const SubCategoriesSelector = ({
  onSubCategorySelect,
  categoryId,
  selectedSubCategory,
}) => {
  const [currentSubCategory, setCurrentSubCategory] = useState(selectedSubCategory)
  const [subCategories, setSubCategories] = useState([])
  const service = new CategoriesService()

  useEffect(() => {
    if (categoryId) {
      service.getSubCategories(categoryId, handleSubCategories)
      setCurrentSubCategory(null)
    }
  }, [categoryId])
  useEffect(() => setCurrentSubCategory(selectedSubCategory), [selectedSubCategory])

  const handleSubCategories = (values) => {
    setSubCategories(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentSubCategory(value)

    if (onSubCategorySelect) {
      onSubCategorySelect(value)
    }
  }

  return (
    <Dropdown
      id="subCategory"
      placeholder={"Sub-Rubro"}
      filter={true}
      dataKey="subCategoryId"
      options={subCategories}
      showClear={true}
      value={currentSubCategory}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
