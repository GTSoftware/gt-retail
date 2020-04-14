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

package ar.com.gtsoftware.rules.helper;


import ar.com.gtsoftware.rules.OfertaDto;
import lombok.RequiredArgsConstructor;
import org.drools.core.spi.KnowledgeHelper;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility to operate Drools on runtime.
 *
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
class DroolsUtility {

    private static final Logger logger = Logger.getLogger(DroolsUtility.class.getName());
    private static final String COMPILED_RULES_PATH = "src/main/resources/drools/templates/rule.drl";

    private final DRLCompiler drlCompiler;

    /**
     * Debug tool to show what is happening over each triggered execution.<br>
     * Name of rule trigger as well the object inspected are printed.
     *
     * @param helper Injected when a consequence is fired.
     */
    public void debug(final KnowledgeHelper helper) {
        logger.log(Level.INFO, "Triggered rule: " + helper.getRule().getName());

        if (helper.getMatch() != null && helper.getMatch().getObjects() != null) {
            for (Object object : helper.getMatch().getObjects()) {
                logger.log(Level.INFO, "Data object: " + object);
            }
        }
    }

    public StatelessKieSession loadSession(List<OfertaDto> ofertas) throws Exception {

        String rulesAsDRL = drlCompiler.getRulesAsDRL(ofertas);
        KieHelper kieHelper = new KieHelper();
        kieHelper.kfs.write(COMPILED_RULES_PATH, rulesAsDRL);

        return kieHelper.build().newStatelessKieSession();
    }

}
