import React, { useEffect, useState } from "react"
import { Dropdown } from "primereact/dropdown"
import { CategoriesService } from "../../service/CategoriesService"

export const CategoriesSelector = ({ onCategorySelect, selectedCategory }) => {
  const [currentCategory, setCurrentCategory] = useState(selectedCategory)
  const [categories, setCategories] = useState([])
  const service = new CategoriesService()

  useEffect(() => service.getCategories(handleCategories), [])
  useEffect(() => setCurrentCategory(selectedCategory), [selectedCategory])

  const handleCategories = (values) => {
    setCategories(values)
  }

  const handleSelectionChange = (value) => {
    setCurrentCategory(value)

    if (onCategorySelect) {
      onCategorySelect(value)
    }
  }

  return (
    <Dropdown
      id="category"
      placeholder={"Rubro"}
      filter={true}
      dataKey="categoryId"
      options={categories}
      showClear={true}
      value={currentCategory}
      optionLabel="displayName"
      onChange={(e) => handleSelectionChange(e.value)}
    />
  )
}
