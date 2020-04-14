/*
 * Copyright 2017 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.service;

import ar.com.gtsoftware.dto.domain.CajasDto;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.search.CajasSearchFilter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Capa de servicio para la gestión de cajas
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */

public interface CajasService
        extends EntityService<CajasDto, CajasSearchFilter> {

    /**
     * Obtiene la caja abierta actual del usuario para la sucursal en la que se encuentre configurado. Devuelve null en
     * caso de no tener ninguna caja abierta.
     *
     * @param usuario
     * @return la caja abierta del usuario.
     */
    CajasDto obtenerCajaActual(UsuariosDto usuario);

    /**
     * Realiza la apertura de caja en función de los saldos del último arqueo del usuario. Si la caja ya estaba abierta
     * retorna la caja abierta.
     *
     * @param usuario
     * @return la nueva caja abierta
     */
    CajasDto abrirCaja(UsuariosDto usuario);

    /**
     * Marca la caja pasada como parámetro como cerrada y devuelve true si se pudo realizar el guardado en la base de
     * datos.
     *
     * @param caja
     * @param fechaCierre
     * @return
     */
    boolean cerrarCaja(@NotNull CajasDto caja, @NotNull Date fechaCierre);

    /**
     * Retorna el total que hay en la caja segùn el filtro. El paràmetro de idCaja en el filtro es requerido.
     *
     * @param csf
     * @return
     */
    BigDecimal obtenerTotalEnCaja(@NotNull CajasSearchFilter csf);

}
