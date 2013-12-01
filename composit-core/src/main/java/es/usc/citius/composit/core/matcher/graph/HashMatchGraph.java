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

package es.usc.citius.composit.core.matcher.graph;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashMatchGraph<E, T extends Comparable<T>> extends AbstractMatchGraph<E,T>{
    private final static Logger logger = LoggerFactory.getLogger(HashMatchGraph.class);

    private MatchTable<E,T> matchTable;
    private Set<E> elements;

    public HashMatchGraph(SetMatchFunction<E,T> setMatcher, Set<E> elements) {
        Stopwatch w = Stopwatch.createStarted();
        logger.debug("Processing full match of {} elements...", elements.size());
        this.matchTable = setMatcher.fullMatch(elements, elements);
        this.elements = elements;
        logger.debug("Match processing finished in {}.", w.stop().toString());
    }

    public HashMatchGraph(MatchTable<E,T> matchResult) {
        this.matchTable = matchResult;
        this.elements = Sets.union(matchResult.getMatchTable().columnKeySet(), matchResult.getMatchTable().rowKeySet());
    }

    @Override
    public Set<E> getElements() {
        return ImmutableSet.copyOf(elements);
    }

    @Override
    public Map<E,T> getTargetElementsMatchedBy(E source) {
        // Target elements are the column keys
        return ImmutableMap.copyOf(this.matchTable.getMatchTable().row(source));
    }

    @Override
    public Map<E,T> getSourceElementsThatMatch(E target) {
        // Source elements are the row keys
        return ImmutableMap.copyOf(this.matchTable.getMatchTable().column(target));
    }

    @Override
    public MatchTable<E, T> partialMatch(Set<E> source, Set<E> target) {
        return fullMatch(source, target);
    }

    @Override
    public MatchTable<E, T> fullMatch(Set<E> source, Set<E> target) {
        MatchTable<E,T> matchResult = new MatchTable<E, T>();
        for(E x : source){
            for(E y : target){
                T match = matchTable.getMatchTable().get(x, y);
                if (match != null){
                    matchResult.addMatch(x, y, match);
                }
            }
        }
        return matchResult;
    }

    @Override
    public T match(E source, E target) {
        return matchTable.getMatchTable().get(source, target);
    }
}
