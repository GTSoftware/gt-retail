/*
 * Copyright 2014 GT Software.
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
package ar.com.gtsoftware.service.impl;


import ar.com.gtsoftware.dao.PersonasCuentaCorrienteFacade;
import ar.com.gtsoftware.domain.PersonasCuentaCorriente;
import ar.com.gtsoftware.dto.domain.PersonasCuentaCorrienteDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.mappers.GenericMapper;
import ar.com.gtsoftware.mappers.PersonasCuentaCorrienteMapper;
import ar.com.gtsoftware.mappers.PersonasMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.PersonasCuentaCorrienteSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class PersonasCuentaCorrienteServiceImpl
        extends BaseEntityService<PersonasCuentaCorrienteDto, PersonasCuentaCorrienteSearchFilter, PersonasCuentaCorriente>
        implements PersonasCuentaCorrienteService {


    private final PersonasCuentaCorrienteFacade cuentaCorrienteFacade;

    private final PersonasCuentaCorrienteMapper mapper;

    private final PersonasMapper personasMapper;

    @Override
    public void registrarMovimientoCuenta(PersonasDto personaDto, BigDecimal importe, String descripcion) {
        PersonasCuentaCorriente cc = new PersonasCuentaCorriente();
        cc.setDescripcionMovimiento(descripcion);
        cc.setFechaMovimiento(new Date());
        cc.setImporteMovimiento(importe);
        cc.setIdPersona(personasMapper.dtoToEntity(personaDto, new CycleAvoidingMappingContext()));
        //cc.setIdRegistroContable(null);
        cuentaCorrienteFacade.create(cc);
    }

    @Override
    public BigDecimal getSaldoPersona(long idPersona) {
        return cuentaCorrienteFacade.getSaldoPersona(idPersona);
    }

    @Override
    protected PersonasCuentaCorrienteFacade getFacade() {
        return cuentaCorrienteFacade;
    }

    @Override
    protected GenericMapper<PersonasCuentaCorriente, PersonasCuentaCorrienteDto> getMapper() {
        return mapper;
    }
}
