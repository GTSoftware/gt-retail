import React from "react"
import { cleanup, render, screen } from "@testing-library/react"
import { LoadingButton } from "./LoadingButton"

describe("LoadingButton", () => {
  const mockProps = {
    loading: false,
    label: "ClickMe",
  }

  afterEach(() => {
    cleanup()
  })

  it("should not be disabled if loading prop is false", () => {
    render(<LoadingButton {...mockProps} />)
    const button = screen.getByRole("button")

    expect(button).not.toBeDisabled()
  })

  it("should be disabled if loading prop is false", () => {
    const disabledProps = { ...mockProps, ...{ loading: true } }

    render(<LoadingButton {...disabledProps} />)

    const button = screen.getByRole("button")

    expect(button).toBeDisabled()
  })

  it("should display loading icon if loading is true", () => {
    const disabledProps = { ...mockProps, ...{ loading: true } }

    render(<LoadingButton {...disabledProps} />)

    const button = screen.getByRole("button")

    const elementsByClassName = button.getElementsByClassName(
      "fa fa-spinner fa-spin"
    )

    expect(elementsByClassName[0]).toBeDefined()
  })

  it("should not display loading icon if not loading", () => {
    render(<LoadingButton {...mockProps} />)

    const button = screen.getByRole("button")

    const elementsByClassName = button.getElementsByClassName(
      "fa fa-spinner fa-spin"
    )

    expect(elementsByClassName[0]).toBeUndefined()
  })
})
