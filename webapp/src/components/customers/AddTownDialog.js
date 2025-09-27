import React, { useState } from "react"
import { Dialog } from "primereact/dialog"
import { InputText } from "primereact/inputtext"
import { Button } from "primereact/button"
import { LocationService } from "../../service/LocationService"

/**
 * AddTownDialog component encapsulates the UI and state for creating a new town (localidad).
 * Props:
 *  - visible: boolean -> whether the dialog is visible
 *  - onHide: function -> called to close the dialog
 *  - provinceId: number -> current selected province id (required to create a town)
 *  - onCreated: function(createdTown) -> called when the town is created successfully
 *  - notify: function({severity, summary, detail}) -> optional notifier (e.g., Toast.show)
 */
export const AddTownDialog = ({
  visible,
  onHide,
  provinceId,
  onCreated,
  notify,
}) => {
  const locationService = new LocationService()

  const [postalCode, setPostalCode] = useState("")
  const [name, setName] = useState("")
  const [loading, setLoading] = useState(false)

  const show = (severity, summary, detail) => {
    if (notify) notify({ severity, summary, detail })
  }

  const reset = () => {
    setPostalCode("")
    setName("")
  }

  const handleSave = async () => {
    if (!provinceId) {
      show(
        "warn",
        "Provincia requerida",
        "Seleccione una provincia antes de agregar una localidad"
      )
      return
    }
    if (!postalCode || !name) {
      show(
        "warn",
        "Datos incompletos",
        "Ingrese código postal y nombre de la ciudad"
      )
      return
    }

    try {
      setLoading(true)
      const created = await locationService.addTown({
        postalCode: postalCode,
        name: name,
        provinceId: provinceId,
      })

      if (onCreated) onCreated(created)
      show(
        "success",
        "Localidad agregada",
        `Se agregó ${created?.displayName || created?.nombreLocalidad}`
      )
      reset()
      if (onHide) onHide()
    } catch (err) {
      const detail = err?.message || err?.error || "No se pudo crear la localidad"
      show("error", "Error", detail)
    } finally {
      setLoading(false)
    }
  }

  const footer = (
    <div className="p-d-flex p-jc-end p-mt-3">
      <Button
        type="button"
        label="Cancelar"
        className="p-button-text p-mr-2"
        onClick={() => {
          reset()
          if (onHide) onHide()
        }}
      />
      <Button
        type="button"
        label="Guardar"
        icon="pi pi-check"
        onClick={handleSave}
        loading={loading}
      />
    </div>
  )

  return (
    <Dialog
      header="Agregar localidad"
      visible={visible}
      onHide={() => {
        reset()
        if (onHide) onHide()
      }}
      modal
      footer={footer}
    >
      <div className="p-fluid">
        <div className="p-field">
          <label htmlFor="newTownPostalCode">Código postal</label>
          <InputText
            id="newTownPostalCode"
            value={postalCode}
            onChange={(e) => setPostalCode(e.target.value)}
          />
        </div>
        <div className="p-field">
          <label htmlFor="newTownName">Nombre de la ciudad</label>
          <InputText
            id="newTownName"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
      </div>
    </Dialog>
  )
}

export default AddTownDialog
