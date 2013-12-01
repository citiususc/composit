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

package es.usc.citius.composit.wsc08.data.matcher;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchFunction;

/**
 * Simple boolean semantic mather for the Web Service Challenge
 * concepts. Concept A matches Concept B if A is equivalent or subclass of B
 * (plugin and exact match).
 *
 */
public class WSCMatcher implements MatchFunction<Concept, Boolean> {
    private HierarchicalKnowledgeBase kb;

    public WSCMatcher(HierarchicalKnowledgeBase kb) {
        this.kb = kb;
    }

    @Override
    public Boolean match(Concept source, Concept target) {
        return (kb.equivalent(source, target) || kb.isSubclass(source, target));
    }
}
