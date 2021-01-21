/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.ComprobantesProveedorFacade;
import ar.com.gtsoftware.dao.FiscalLibroIvaComprasFacade;
import ar.com.gtsoftware.dao.FiscalTiposComprobanteFacade;
import ar.com.gtsoftware.domain.FiscalLibroIvaCompras;
import ar.com.gtsoftware.domain.FiscalLibroIvaComprasLineas;
import ar.com.gtsoftware.domain.FiscalTiposComprobante;
import ar.com.gtsoftware.domain.ProveedoresComprobantes;
import ar.com.gtsoftware.dto.domain.ProveedoresComprobantesDto;
import ar.com.gtsoftware.mappers.ProveedoresComprobantesMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import ar.com.gtsoftware.search.FiscalTiposComprobanteSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ComprobantesProveedorService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComprobantesProveedorServiceImpl
    extends BaseEntityService<
        ProveedoresComprobantesDto, ComprobantesProveedorSearchFilter, ProveedoresComprobantes>
    implements ComprobantesProveedorService {

  private final ComprobantesProveedorFacade facade;
  private final FiscalTiposComprobanteFacade tiposComprobanteFacade;
  private final FiscalLibroIvaComprasFacade ivaComprasFacade;

  private final ProveedoresComprobantesMapper mapper;

  @Override
  protected ComprobantesProveedorFacade getFacade() {
    return facade;
  }

  @Override
  protected ProveedoresComprobantesMapper getMapper() {
    return mapper;
  }

  @Override
  public ProveedoresComprobantesDto guardarYFiscalizar(ProveedoresComprobantesDto comprobanteDto)
      throws ServiceException {
    // TODO hacer las validaciones con annotations

    if (CollectionUtils.isEmpty(
        comprobanteDto.getIdRegistro().getFiscalLibroIvaComprasLineasList())) {
      throw new ServiceException("Falta agregar aglún alicuota al comprobante");
    }
    if (comprobanteDto.getIdRegistro().getIdPeriodoFiscal() == null) {
      throw new ServiceException("Se debe establacer un perìodo fiscal");
    }
    FiscalTiposComprobanteSearchFilter ftcsf =
        FiscalTiposComprobanteSearchFilter.builder()
            .letra(comprobanteDto.getLetra())
            .idTipoComprobante(comprobanteDto.getTipoComprobante().getId())
            .build();
    FiscalTiposComprobante tipoCompFiscal = tiposComprobanteFacade.findFirstBySearchFilter(ftcsf);

    if (tipoCompFiscal == null) {
      throw new ServiceException("Este tipo de comprobante no puede ser fiscalizado");
    }
    ProveedoresComprobantes comprobante =
        mapper.dtoToEntity(comprobanteDto, new CycleAvoidingMappingContext());

    FiscalLibroIvaCompras registro = comprobante.getIdRegistro();
    registro.setFechaFactura(comprobante.getFechaComprobante());
    registro.setDocumento(comprobante.getIdProveedor().getDocumento());
    registro.setLetraFactura(comprobante.getLetra());
    registro.setTotalFactura(comprobante.getTotal());
    registro.setIdPersona(comprobante.getIdProveedor());
    registro.setIdResponsabilidadIva(comprobante.getIdProveedor().getIdResponsabilidadIva());
    registro.setPuntoVentaFactura(StringUtils.leftPad(registro.getPuntoVentaFactura(), 4, "0"));
    registro.setNumeroFactura(StringUtils.leftPad(registro.getNumeroFactura(), 8, "0"));

    BigDecimal signo = comprobante.getTipoComprobante().getSigno();
    registro.setImportePercepcionIva(registro.getImportePercepcionIva().multiply(signo));
    registro.setImportePercepcionIngresosBrutos(
        registro.getImportePercepcionIngresosBrutos().multiply(signo));

    calcularTotalesLibro(registro, signo);

    registro.setCodigoTipoComprobante(tipoCompFiscal);
    ivaComprasFacade.createOrEdit(registro);
    ProveedoresComprobantes compEditado = facade.createOrEdit(comprobante);

    return mapper.entityToDto(compEditado, new CycleAvoidingMappingContext());
  }

  @Override
  public void eliminarComprobante(ProveedoresComprobantesDto comprobante) throws ServiceException {
    boolean shouldDeleteFiscalRecord = false;
    ProveedoresComprobantes comprobanteEntity = facade.find(comprobante.getId());
    if (comprobanteEntity.getIdRegistro() != null) {
      if (comprobanteEntity.getIdRegistro().getIdPeriodoFiscal().isPeriodoCerrado()) {
        throw new ServiceException("No se puede eliminar un comprobante de un periodo cerrado");
      }
      shouldDeleteFiscalRecord = true;
    }
    facade.remove(comprobanteEntity);
    if (shouldDeleteFiscalRecord) {
      ivaComprasFacade.remove(comprobanteEntity.getIdRegistro());
    }
  }

  private void calcularTotalesLibro(FiscalLibroIvaCompras registro, BigDecimal signo) {
    // TODO ver el tema de los exentos
    BigDecimal totalIVA = BigDecimal.ZERO;
    BigDecimal totalNG = BigDecimal.ZERO;
    BigDecimal totalNoGravado = BigDecimal.ZERO;
    for (FiscalLibroIvaComprasLineas linea : registro.getFiscalLibroIvaComprasLineasList()) {
      linea.setNoGravado(linea.getNoGravado().multiply(signo));
      linea.setNetoGravado(linea.getNetoGravado().multiply(signo));
      linea.setImporteIva(linea.getImporteIva().multiply(signo));

      totalIVA = totalIVA.add(linea.getImporteIva());
      totalNG = totalNG.add(linea.getNetoGravado());
      totalNoGravado = totalNoGravado.add(linea.getNoGravado());
    }
    registro.setImporteIva(totalIVA);
    registro.setImporteNetoGravado(totalNG);
    registro.setImporteNetoNoGravado(totalNoGravado);

    registro.setImporteTributos(BigDecimal.ZERO);
    registro.setImporteExento(BigDecimal.ZERO);
    BigDecimal totalComprobante = totalIVA.add(totalNG).add(totalNoGravado);
    totalComprobante = totalComprobante.add(registro.getImportePercepcionIngresosBrutos());
    totalComprobante = totalComprobante.add(registro.getImportePercepcionIva());
    registro.setTotalFactura(totalComprobante);
  }
}
