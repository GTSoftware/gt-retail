package ar.com.gtsoftware.rules.helper;

import ar.com.gtsoftware.rules.Condicion;
import ar.com.gtsoftware.rules.CondicionIlegalException;
import ar.com.gtsoftware.rules.OfertaDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.template.ObjectDataCompiler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class DRLCompiler {

    private static final String DROOLS_TEMPLATES_PRODUCT_DRT_PATH = "ar/com/gtsoftware/drools/templates/Product.drt";
    private static final Logger logger = LogManager.getLogger(DRLCompiler.class);


    @Cacheable("drlRules")
    public String getRulesAsDRL(List<OfertaDto> rules) {
        InputStream templateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DROOLS_TEMPLATES_PRODUCT_DRT_PATH);

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        List<Map<String, Object>> rulesAsMap = getRulesAsMap(rules);
        String rulesAsDRL = compiler.compile(rulesAsMap, templateStream);
        logger.debug("drl:\n" + rulesAsDRL);

        return rulesAsDRL;
    }

    private List<Map<String, Object>> getRulesAsMap(List<OfertaDto> ofertas) {
        List<Map<String, Object>> mappedRules = new ArrayList<Map<String, Object>>(ofertas.size());

        for (OfertaDto oferta : ofertas) {
            try {
                mappedRules.add(toMap(oferta));
            } catch (CondicionIlegalException e) {
                logger.error("La oferta ID: " + oferta.getId() + " no pudo ser compilada");
            }
        }

        return mappedRules;
    }

    private Map<String, Object> toMap(OfertaDto oferta) throws CondicionIlegalException {

        Map<String, Object> attributes = new HashMap<>(4);
        attributes.put("nombre", oferta.getTextoOferta());
        attributes.put("condiciones", condicionesAsDrl(oferta.getCondiciones()));
        attributes.put("tipoAccion", oferta.getTipoAccion());
        attributes.put("descuento", oferta.getDescuento().toPlainString());

        return attributes;
    }

    private String condicionesAsDrl(List<Condicion> condiciones) throws CondicionIlegalException {
        if (CollectionUtils.isEmpty(condiciones)) {
            throw new IllegalStateException("Se debe declarar al menos una condición para evaluar");
        }

        StringBuilder drl = new StringBuilder();
        //For each condition of this rule, we create its textual representation
        for (int i = 0; i < condiciones.size(); i++) {
            Condicion condition = condiciones.get(i);
            drl.append("(");
            drl.append(condition.buildExpression());
            drl.append(")");
            if ((i + 1) < condiciones.size()) {
                drl.append(" && ");
            }
        }

        return drl.toString();
    }
}
