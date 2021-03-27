import React from "react"
import { cleanup, render } from "@testing-library/react"
import { BrandsSelector } from "../BrandsSelector"
import { Dropdown } from "primereact/dropdown"
import { BrandsService } from "../../../service/BrandsService"

jest.mock("../../../service/BrandsService")
jest.mock("primereact/dropdown")

describe("BrandsSelector", () => {
  const mockOnBrandSelectCallback = jest.fn()

  const mockProps = {
    onBrandSelect: mockOnBrandSelectCallback,
  }

  afterEach(() => {
    cleanup()
  })

  it("should render", () => {
    render(<BrandsSelector {...mockProps} />, Dropdown)

    const props = {
      id: "brand",
      placeholder: "Marca",
      filter: true,
      dataKey: "brandId",
      options: [],
      showClear: true,
      value: null,
      optionLabel: "displayName",
    }

    expect(Dropdown).toHaveBeenCalledTimes(1)
    const call = Dropdown.mock.calls[0][0]

    expect(call).toMatchObject(props)
    expect(call.onChange).toBeTruthy()
  })

  it("should call service to get brands", () => {
    const { debug } = render(<BrandsSelector {...mockProps} />)

    expect(BrandsService).toHaveBeenCalledTimes(1)

    debug()
  })

  it("should pass selectedBrand prop", () => {
    const mockBrand = { brandId: 1, displayName: "Test Brand" }
    const props = { ...mockProps, ...{ selectedBrand: mockBrand } }

    render(<BrandsSelector {...props} />, Dropdown)

    expect(Dropdown).toHaveBeenCalledTimes(1)
    const call = Dropdown.mock.calls[0][0]

    expect(call.value).toEqual(mockBrand)
  })
})
