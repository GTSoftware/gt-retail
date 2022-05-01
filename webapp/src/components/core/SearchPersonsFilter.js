import React, { useState } from "react"
import { InputText } from "primereact/inputtext"
import PropTypes from "prop-types"
import { TriStateCheckbox } from "primereact/tristatecheckbox"

export const SearchPersonsFilter = ({ searchCustomersCallback }) => {
  const [text, setText] = useState("")
  const [active, setActive] = useState(true)

  const renderTextSearch = () => {
    return (
      <InputText
        id="searchText"
        autoFocus
        onChange={(e) => {
          setText(e.target.value)
        }}
        onFocus={(e) => e.target.select()}
        value={text}
        placeholder="Términos de búsqueda"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const renderActiveFilter = () => {
    return (
      <>
        <TriStateCheckbox
          id="activoCheck"
          onChange={(e) => {
            setActive(e.value)
          }}
          value={active}
        />
        <label htmlFor="activoCheck" className="p-checkbox-label">
          {"Activos"}
        </label>
      </>
    )
  }

  const buildSearchFilter = () => {
    return {
      txt: text,
      activo: active,
    }
  }

  const handleEnterKeyPress = (event) => {
    if (event.key === "Enter") {
      searchCustomersCallback(buildSearchFilter())
    }
  }

  return (
    <div className="p-card-body p-fluid p-grid">
      <div className="p-col-12 p-lg-3">{renderTextSearch()}</div>
      <div className="p-col-12 p-lg-1">{renderActiveFilter()}</div>
    </div>
  )
}

SearchPersonsFilter.propTypes = {
  searchCustomersCallback: PropTypes.func.isRequired,
}
