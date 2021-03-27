import React from "react"
import { cleanup, render } from "@testing-library/react"
import { CategoriesSelector } from "../CategoriesSelector"
import { Dropdown } from "primereact/dropdown"
import { CategoriesService } from "../../../service/CategoriesService"

jest.mock("../../../service/CategoriesService")
jest.mock("primereact/dropdown")

describe("CategoriesSelector", () => {
  const mockOnCategorySelect = jest.fn()

  const mockProps = {
    onCategorySelect: mockOnCategorySelect,
  }

  afterEach(() => {
    cleanup()
  })

  it("should render", () => {
    const { debug } = render(<CategoriesSelector {...mockProps} />, Dropdown)

    const props = {
      id: "category",
      placeholder: "Rubro",
      filter: true,
      dataKey: "categoryId",
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

  it("should call service to get categories", () => {
    render(<CategoriesSelector {...mockProps} />)

    expect(CategoriesService).toHaveBeenCalledTimes(1)
  })

  it("should pass selectedCategory prop", () => {
    const mockCategory = { categoryId: 1, displayName: "Test Category" }
    const props = { ...mockProps, ...{ selectedCategory: mockCategory } }

    render(<CategoriesSelector {...props} />, Dropdown)

    expect(Dropdown).toHaveBeenCalledTimes(1)
    const call = Dropdown.mock.calls[0][0]

    expect(call.value).toEqual(mockCategory)
  })
})
