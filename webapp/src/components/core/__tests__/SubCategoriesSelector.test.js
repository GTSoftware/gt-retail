import React from "react"
import { cleanup, render } from "@testing-library/react"
import { SubCategoriesSelector } from "../SubCategoriesSelector"
import { Dropdown } from "primereact/dropdown"
import { CategoriesService } from "../../../service/CategoriesService"

jest.mock("../../../service/CategoriesService")
jest.mock("primereact/dropdown")

describe("SubCategoriesSelector", () => {
  const mockOnSubCategorySelect = jest.fn()

  const mockProps = {
    onSubCategorySelect: mockOnSubCategorySelect,
  }

  afterEach(() => {
    cleanup()
  })

  it("should render", () => {
    const { debug } = render(<SubCategoriesSelector {...mockProps} />, Dropdown)

    const props = {
      id: "subCategory",
      placeholder: "Sub-Rubro",
      filter: true,
      dataKey: "subCategoryId",
      options: [],
      showClear: true,
      value: null,
      optionLabel: "displayName",
    }

    expect(Dropdown).toHaveBeenCalledTimes(1)
    const call = Dropdown.mock.calls[0][0]

    expect(call).toMatchObject(props)
    expect(call.onChange).toBeTruthy()
    debug()
  })

  it("should call service to get subcategories", () => {
    render(<SubCategoriesSelector {...mockProps} />)

    expect(CategoriesService).toHaveBeenCalledTimes(1)
  })

  it("should pass selectedSubCategory prop", () => {
    const mockSubCategory = { subCategoryId: 1, displayName: "Test SubCategory" }
    const props = { ...mockProps, ...{ selectedSubCategory: mockSubCategory } }

    render(<SubCategoriesSelector {...props} />, Dropdown)

    expect(Dropdown).toHaveBeenCalledTimes(1)
    const call = Dropdown.mock.calls[0][0]

    expect(call.value).toEqual(mockSubCategory)
  })
})
