package ar.com.gtsoftware.service;

import ar.com.gtsoftware.api.request.ProductPercent;
import ar.com.gtsoftware.dao.ProductosFacade;
import ar.com.gtsoftware.dao.ProductosTiposPorcentajesFacade;
import ar.com.gtsoftware.entity.Productos;
import ar.com.gtsoftware.entity.ProductosPorcentajes;
import ar.com.gtsoftware.entity.ProductosPrecios;
import ar.com.gtsoftware.service.prices.UpdateProductPriceDto;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class PricesUpdateHandler {

  private static final BigDecimal CIEN = new BigDecimal(100);

  private final ProductosFacade productosFacade;
  private final ProductosTiposPorcentajesFacade tiposPorcentajesFacade;
  private final BusinessDateUtils dateUtils;

  @JmsListener(destination = "updatePriceQueue")
  @Transactional
  public void processMessage(UpdateProductPriceDto updateProductPriceDto) {
    Productos producto = productosFacade.find(updateProductPriceDto.productId());
    LocalDateTime today = dateUtils.getCurrentDateTime();

    BigDecimal costUpdatePercent = updateProductPriceDto.costUpdatePercent();
    updateProductCost(producto, costUpdatePercent);

    final List<ProductosPorcentajes> porcentajes = producto.getPorcentajes();
    deletePercents(updateProductPriceDto, porcentajes);
    addPercents(updateProductPriceDto, producto, today, porcentajes);

    updateSalePrices(producto, today);
    producto.setFechaUltimaModificacion(today);

    productosFacade.edit(producto);

    log.info(
        "Updated price of product: {} by user: {}", producto.getId(), updateProductPriceDto.user());
  }

  private void addPercents(
      UpdateProductPriceDto updateProductPriceDto,
      Productos producto,
      LocalDateTime today,
      List<ProductosPorcentajes> porcentajes) {
    if (CollectionUtils.isNotEmpty(updateProductPriceDto.percentsToAdd())) {
      for (ProductPercent toAdd : updateProductPriceDto.percentsToAdd()) {
        ProductosPorcentajes pp = new ProductosPorcentajes();
        pp.setIdProducto(producto);
        pp.setIdTipoPorcentaje(tiposPorcentajesFacade.find(toAdd.percentTypeId()));
        pp.setValor(toAdd.percentValue());
        pp.setFechaModificacion(today);
        porcentajes.add(pp);
        productosFacade.edit(producto);
      }
    }
  }

  private static void deletePercents(
      UpdateProductPriceDto updateProductPriceDto, List<ProductosPorcentajes> porcentajes) {
    if (CollectionUtils.isNotEmpty(updateProductPriceDto.percentsToDelete())) {
      for (ProductPercent toRemove : updateProductPriceDto.percentsToDelete()) {
        porcentajes.removeIf(
            p ->
                p.getIdTipoPorcentaje().getId().equals(toRemove.percentTypeId())
                    && p.getValor().compareTo(toRemove.percentValue()) == 0);
      }
    }
  }

  private static void updateProductCost(Productos producto, BigDecimal costUpdatePercent) {
    if (Objects.nonNull(costUpdatePercent)) {
      BigDecimal costoAdquisicionNeto = producto.getCostoAdquisicionNeto();
      costoAdquisicionNeto =
          costoAdquisicionNeto.add(costoAdquisicionNeto.multiply(costUpdatePercent.divide(CIEN)));
      producto.setCostoAdquisicionNeto(costoAdquisicionNeto);
    }
  }

  private void updateSalePrices(Productos producto, LocalDateTime today) {
    BigDecimal costoAdquisicionNeto = producto.getCostoAdquisicionNeto();
    List<ProductosPorcentajes> porcentajes = producto.getPorcentajes();
    BigDecimal costoFinal = costoAdquisicionNeto;
    for (ProductosPorcentajes pp : porcentajes) {
      if (pp.getIdTipoPorcentaje().isPorcentaje()) {
        costoFinal = costoFinal.add(costoFinal.multiply(pp.getValor().divide(CIEN)));
      } else {
        costoFinal = costoFinal.add(pp.getValor());
      }
    }
    producto.setCostoFinal(costoFinal);
    if (producto.getPrecios() != null) {
      BigDecimal coeficienteIVA =
          producto.getIdAlicuotaIva().getValorAlicuota().divide(CIEN).add(BigDecimal.ONE);
      for (ProductosPrecios pp : producto.getPrecios()) {
        BigDecimal utilidad = pp.getUtilidad().divide(CIEN);
        pp.setNeto(costoFinal.add(costoFinal.multiply(utilidad)));
        pp.setFechaModificacion(today);
        pp.setPrecio(pp.getNeto().multiply(coeficienteIVA).setScale(2, RoundingMode.HALF_UP));
      }
    }
  }
}
