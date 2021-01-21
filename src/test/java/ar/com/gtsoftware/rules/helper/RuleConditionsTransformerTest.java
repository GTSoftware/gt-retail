package ar.com.gtsoftware.rules.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import ar.com.gtsoftware.rules.Campo;
import ar.com.gtsoftware.rules.Condicion;
import ar.com.gtsoftware.rules.CondicionIlegalException;
import ar.com.gtsoftware.rules.Operacion;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RuleConditionsTransformerTest {

  @Test
  void shouldTransformConditions() throws CondicionIlegalException {
    List<Condicion> condiciones = new ArrayList<>(2);
    condiciones.add(
        Condicion.builder().campo(Campo.ID_MARCA).operacion(Operacion.IGUAL).valor("3").build());
    condiciones.add(
        Condicion.builder()
            .campo(Campo.DESCRIPCION)
            .operacion(Operacion.CONTIENE)
            .valor("TORNILLO")
            .build());
    final String conditions = RuleConditionsTransformer.transformConditions(condiciones);

    assertThat(
        conditions,
        is(
            "(linea.idProducto.idMarca.id == 3) && (linea.descripcion.toUpperCase().contains(\"TORNILLO\"))"));
  }
}
