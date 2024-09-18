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
import ar.com.gtsoftware.dao.FiscalAlicuotasIvaFacade;
import ar.com.gtsoftware.dao.FiscalLibroIvaComprasFacade;
import ar.com.gtsoftware.dao.FiscalPeriodosFiscalesFacade;
import ar.com.gtsoftware.dao.FiscalTiposComprobanteFacade;
import ar.com.gtsoftware.dao.NegocioTiposComprobanteFacade;
import ar.com.gtsoftware.dao.PersonasFacade;
import ar.com.gtsoftware.dao.SucursalesFacade;
import ar.com.gtsoftware.dao.UsuariosFacade;
import ar.com.gtsoftware.dto.domain.ProveedoresComprobantesDto;
import ar.com.gtsoftware.entity.FiscalAlicuotasIva;
import ar.com.gtsoftware.entity.FiscalLibroIvaCompras;
import ar.com.gtsoftware.entity.FiscalLibroIvaComprasLineas;
import ar.com.gtsoftware.entity.FiscalPeriodosFiscales;
import ar.com.gtsoftware.entity.FiscalTiposComprobante;
import ar.com.gtsoftware.entity.NegocioTiposComprobante;
import ar.com.gtsoftware.entity.Personas;
import ar.com.gtsoftware.entity.ProveedoresComprobantes;
import ar.com.gtsoftware.mappers.ProveedoresComprobantesMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import ar.com.gtsoftware.search.FiscalTiposComprobanteSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ComprobantesProveedorService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComprobantesProveedorServiceImpl
    extends BaseEntityService<
        ProveedoresComprobantesDto, ComprobantesProveedorSearchFilter, ProveedoresComprobantes>
    implements ComprobantesProveedorService {

  private static final BigDecimal CIEN = new BigDecimal(100);

  private final ComprobantesProveedorFacade facade;
  private final FiscalTiposComprobanteFacade tiposComprobanteFacade;
  private final FiscalLibroIvaComprasFacade ivaComprasFacade;
  private final FiscalPeriodosFiscalesFacade periodosFiscalesFacade;
  private final FiscalAlicuotasIvaFacade alicuotasIvaFacade;
  private final PersonasFacade personasFacade;
  private final NegocioTiposComprobanteFacade negocioTiposComprobanteFacade;
  private final SucursalesFacade sucursalesFacade;
  private final UsuariosFacade usuariosFacade;

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
  @Transactional
  public ProveedoresComprobantesDto guardarYFiscalizar(ProveedoresComprobantesDto comprobanteDto)
      throws ServiceException {

    FiscalTiposComprobanteSearchFilter ftcsf =
        FiscalTiposComprobanteSearchFilter.builder()
            .letra(comprobanteDto.getLetra())
            .idTipoComprobante(comprobanteDto.getTipoComprobante().getId())
            .build();
    FiscalTiposComprobante tipoCompFiscal =
        Optional.ofNullable(tiposComprobanteFacade.findFirstBySearchFilter(ftcsf))
            .orElseThrow(
                () -> new ServiceException("Este tipo de comprobante no puede ser fiscalizado"));

    FiscalPeriodosFiscales periodoFiscal =
        Optional.ofNullable(
                periodosFiscalesFacade.find(comprobanteDto.getIdRegistro().getIdPeriodoFiscal().getId()))
            .orElseThrow(() -> new ServiceException("El periodo fiscal no existe"));
    if (periodoFiscal.isPeriodoCerrado()){
      throw new ServiceException("El periodo fiscal esta cerrado");
    }

    Personas proveedor =
        Optional.ofNullable(personasFacade.find(comprobanteDto.getIdProveedor().getId()))
            .orElseThrow(() -> new ServiceException("El proveedor no existe"));

    NegocioTiposComprobante negocioTiposComprobante =
        Optional.ofNullable(
                negocioTiposComprobanteFacade.find(comprobanteDto.getTipoComprobante().getId()))
            .orElseThrow(() -> new ServiceException("El tipo de comprobante no existe"));

    ProveedoresComprobantes comprobante =
        mapper.dtoToEntity(comprobanteDto, new CycleAvoidingMappingContext());

    comprobante.setIdProveedor(proveedor);
    comprobante.setIdSucursal(Optional.ofNullable(
                    sucursalesFacade.find(comprobanteDto.getIdSucursal().getId()))
            .orElseThrow(() -> new ServiceException("Sucursal inexistente")));
    comprobante.setIdUsuario(Optional.ofNullable(
                    usuariosFacade.find(comprobanteDto.getIdUsuario().getId()))
            .orElseThrow(() -> new ServiceException("Usuario inexistente")));

    comprobante.setTipoComprobante(negocioTiposComprobante);

    FiscalLibroIvaCompras registro = comprobante.getIdRegistro();
    registro.setCodigoTipoComprobante(tipoCompFiscal);
    registro.setIdPeriodoFiscal(periodoFiscal);
    registro.setFechaFactura(comprobante.getFechaComprobante());
    registro.setDocumento(comprobante.getIdProveedor().getDocumento());
    registro.setLetraFactura(comprobante.getLetra());

    registro.setIdPersona(comprobante.getIdProveedor());
    registro.setIdResponsabilidadIva(comprobante.getIdProveedor().getIdResponsabilidadIva());
    registro.setPuntoVentaFactura(StringUtils.leftPad(registro.getPuntoVentaFactura(), 4, "0"));
    registro.setNumeroFactura(StringUtils.leftPad(registro.getNumeroFactura(), 8, "0"));

    BigDecimal signo = negocioTiposComprobante.getSigno();
    registro.setImportePercepcionIva(registro.getImportePercepcionIva().multiply(signo));
    registro.setImportePercepcionIngresosBrutos(
        registro.getImportePercepcionIngresosBrutos().multiply(signo));

    calcularTotalesLibro(registro, signo);
    comprobante.setTotal(registro.getTotalFactura().abs());
    comprobante.setTotalConSigno(registro.getTotalFactura());

    ivaComprasFacade.create(registro);
    comprobante.setIdRegistro(registro);
    ProveedoresComprobantes compEditado = facade.createOrEdit(comprobante);

    return mapper.entityToDto(compEditado, new CycleAvoidingMappingContext());
  }

  private void calcularTotalesLibro(FiscalLibroIvaCompras registro, BigDecimal signo)
      throws ServiceException {
    // TODO ver el tema de los exentos
    BigDecimal totalIVA = BigDecimal.ZERO;
    BigDecimal totalNG = BigDecimal.ZERO;
    BigDecimal totalNoGravado = BigDecimal.ZERO;
    for (FiscalLibroIvaComprasLineas linea : registro.getFiscalLibroIvaComprasLineasList()) {
      final FiscalAlicuotasIva alicuotasIva =
          Optional.ofNullable(alicuotasIvaFacade.find(linea.getIdAlicuotaIva().getId()))
              .orElseThrow(() -> new ServiceException("Alícuota de IVA inexistente"));

      linea.setIdAlicuotaIva(alicuotasIva);

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
}
