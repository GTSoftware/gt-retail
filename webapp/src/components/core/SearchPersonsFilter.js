import React, {useState} from "react"
import {InputText} from "primereact/inputtext";
import PropTypes from "prop-types"


export const SearchPersonsFilter = ({searchCustomersCallback}) => {

  const [text, setText] = useState("")

  const renderTextSearch = () => {
    return (
      <InputText
        id="searchText"
        autoFocus
        onChange={(e) => {
          setText(e.target.value)
        }}
        value={text}
        placeholder="Términos de búsqueda"
        onKeyPress={handleEnterKeyPress}
      />
    )
  }

  const handleEnterKeyPress = (event) => {
    if (event.key === "Enter") {
      searchCustomersCallback(text)
    }
  }

  return(
    <div className="p-card-body p-fluid p-grid">
      <div className="p-col-12 p-lg-3">{renderTextSearch()}</div>
    </div>
  )
}

SearchPersonsFilter.propTypes = {
  searchCustomersCallback: PropTypes.func.isRequired
}