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


import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.graph.HashMatchGraph;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WSCMatchGraph implements MatchGraph<Concept, Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(WSCMatchGraph.class);

    private HierarchicalKnowledgeBase kb;
    private HashMatchGraph<Concept, Boolean> matchGraph;


    public WSCMatchGraph(HierarchicalKnowledgeBase kb){
        this.kb = kb;
        // Build a table using the kb and using exact/plugin match.
        Stopwatch w = Stopwatch.createStarted();
        Table<Concept, Concept, Boolean> table = HashBasedTable.create();
        for(Concept source : kb.getConcepts()){
            Set<Concept> set = new HashSet<Concept>(kb.getSuperclasses(source));
            set.add(source);
            for(Concept target : set){
                table.put(source, target, true);
            }
        }
        this.matchGraph = new HashMatchGraph<Concept, Boolean>(new MatchTable<Concept, Boolean>(table));
        logger.debug("MatchGraph computed in {}", w.stop().toString());
    }

    @Override
    public Set<Concept> getElements() {
        return matchGraph.getElements();
    }

    @Override
    public Map<Concept,Boolean> getTargetElementsMatchedBy(Concept source) {
        return matchGraph.getTargetElementsMatchedBy(source);
    }

    @Override
    public Map<Concept,Boolean> getSourceElementsThatMatch(Concept target) {
        return matchGraph.getSourceElementsThatMatch(target);
    }

    @Override
    public Map<Concept,Boolean> getTargetElementsMatchedBy(Concept source, Boolean type, TypeSelector selector) {
        return matchGraph.getTargetElementsMatchedBy(source, type, selector);
    }

    @Override
    public Map<Concept,Boolean> getSourceElementsThatMatch(Concept target, Boolean type, TypeSelector selector) {
        return matchGraph.getSourceElementsThatMatch(target, type, selector);
    }

    @Override
    public MatchTable<Concept,Boolean> partialMatch(Set<Concept> source, Set<Concept> target) {
        return matchGraph.partialMatch(source, target);
    }

    @Override
    public MatchTable<Concept,Boolean> fullMatch(Set<Concept> source, Set<Concept> target) {
        return matchGraph.fullMatch(source, target);
    }

    @Override
    public Boolean match(Concept source, Concept target) {
        return matchGraph.match(source, target);
    }
}
