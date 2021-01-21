package ar.com.gtsoftware.rules.helper;

import static ar.com.gtsoftware.rules.Operacion.CONTIENE;
import static ar.com.gtsoftware.rules.Operacion.MULTIPLO;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import ar.com.gtsoftware.rules.Condicion;
import ar.com.gtsoftware.rules.CondicionIlegalException;
import ar.com.gtsoftware.rules.Operacion;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;

public class RuleConditionsTransformer {

  public static String transformConditions(List<Condicion> condiciones)
      throws CondicionIlegalException {
    if (CollectionUtils.isEmpty(condiciones)) {
      throw new IllegalStateException("Se debe declarar al menos una condición para evaluar");
    }

    StringBuilder drl = new StringBuilder();
    // For each condition of this rule, we create its textual representation
    for (int i = 0; i < condiciones.size(); i++) {
      Condicion condition = condiciones.get(i);
      drl.append("(");
      drl.append(buildExpression(condition));
      drl.append(")");
      if ((i + 1) < condiciones.size()) {
        drl.append(" && ");
      }
    }

    return drl.toString();
  }

  private static String buildExpression(Condicion condition) throws CondicionIlegalException {
    validarCondicion(condition);

    if (MULTIPLO == condition.getOperacion()) {
      return "(linea." + condition.getCampo().getRuta() + buildOperador(condition);
    }

    return "linea." + condition.getCampo().getRuta() + buildOperador(condition);
  }

  private static void validarCondicion(Condicion condition) throws CondicionIlegalException {
    if (condition.getOperacion() == null
        || condition.getCampo() == null
        || isEmpty(condition.getValor())) {
      throw new CondicionIlegalException("Algunos de los elementos de la condición es nulo!");
    }
    if (!condition.getOperacion().soportaTipo(condition.getCampo().getClase())) {
      throw new CondicionIlegalException(
          "El campo:"
              + condition.getCampo()
              + " no soporta la operación: "
              + condition.getOperacion());
    }
  }

  private static String buildOperador(Condicion condition) {
    @NotNull final Operacion operacion = condition.getOperacion();
    @NotNull final String valor = condition.getValor();
    if (CONTIENE == operacion) {
      return ".toUpperCase().contains(\"" + condition.getValor().toUpperCase() + "\")";
    }
    if (MULTIPLO == operacion) {
      return SPACE + operacion.getOperador() + SPACE + valor + ") == 0";
    }

    return SPACE + operacion.getOperador() + SPACE + valor;
  }
}
