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
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.graph.AbstractMatchGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WSCKBMatchGraph extends AbstractMatchGraph<Concept, Boolean> {
    private HierarchicalKnowledgeBase kb;

    public WSCKBMatchGraph(HierarchicalKnowledgeBase kb) {
        this.kb = kb;
    }

    @Override
    public Set<Concept> getElements() {
        return kb.getConcepts();
    }

    @Override
    public Map<Concept, Boolean> getTargetElementsMatchedBy(Concept source) {
        Map<Concept,Boolean> result = new HashMap<Concept, Boolean>();
        for(Concept target : kb.getSuperclasses(source)){
            // Plugin match source matches target if source equals(exact)/subclass(plugin) target
            // (Target is equal/superclass of source)
            result.put(target, true);
        }
        // exact match, source->source
        result.put(source, true);
        return result;
    }

    @Override
    public Map<Concept, Boolean> getSourceElementsThatMatch(Concept target) {
        Map<Concept,Boolean> result = new HashMap<Concept, Boolean>();
        for(Concept source : kb.getSubclasses(target)){
            // Plugin match
            result.put(source, true);
        }
        // exact match, target->target
        result.put(target, true);
        return result;
    }

    @Override
    public MatchTable<Concept, Boolean> partialMatch(Set<Concept> source, Set<Concept> target) {
        // Compute first match between source and target elements
        MatchTable<Concept, Boolean> matchTable = new MatchTable<Concept, Boolean>();
        for(Concept tg : target){
            for(Concept src : source){
                if (kb.isSubclass(src, tg) || kb.equivalent(src, tg)){
                    matchTable.addMatch(src, tg, true);
                    break; // skip to the next target
                }
            }
        }
        return matchTable;
    }

    @Override
    public MatchTable<Concept, Boolean> fullMatch(Set<Concept> source, Set<Concept> target) {
        // Compute all matches between source and target elements
        MatchTable<Concept, Boolean> matchTable = new MatchTable<Concept, Boolean>();
        for(Concept src : source){
            Set<Concept> validTargets = new HashSet<Concept>(kb.getSuperclasses(src));
            validTargets.add(src);
            validTargets.retainAll(target);
            for(Concept tg : validTargets){
                matchTable.addMatch(src, tg, true);
            }
        }
        return matchTable;
    }

    @Override
    public Boolean match(Concept source, Concept target) {
        return (kb.equivalent(source,target) || kb.isSubclass(source, target));
    }
}
