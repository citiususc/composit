/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC) http://citius.usc.es.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.composit.core.matcher.logic;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchFunction;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class LogicMatcher implements MatchFunction<Concept, LogicMatchType> {
    private HierarchicalKnowledgeBase kb;

    public LogicMatcher(HierarchicalKnowledgeBase kb){
        this.kb = kb;
    }

    @Override
    public LogicMatchType match(Concept source, Concept target) {
        if (kb.equivalent(source, target)){
            return LogicMatchType.EXACT;
        } else if (kb.isSubclass(source, target)){
            return LogicMatchType.PLUGIN;
        } else if (kb.isSuperclass(source, target)){
            return LogicMatchType.SUBSUMES;
        }
        return null;
    }
}
