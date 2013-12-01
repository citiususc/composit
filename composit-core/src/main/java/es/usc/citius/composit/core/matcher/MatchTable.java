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

package es.usc.citius.composit.core.matcher;


import com.google.common.base.Functions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class MatchTable<E, T extends Comparable<T>> {
    // Row: source, Column: target, Value matchType
    private Table<E, E, T> matchTable = HashBasedTable.create();

    public MatchTable(){}

    /**
     * Interprets a {@link Table} as a match table of elements.
     * @param matchTable
     */
    public MatchTable(Table<E, E, T> matchTable){
        this.matchTable = matchTable;
    }

    public void addMatch(E x, E y, T type) {
        matchTable.put(x, y, type);
    }

    public Table<E, E, T> getMatchTable() {
        return matchTable;//ImmutableTable.copyOf(matchTable);
    }

    public Set<E> getSourceElements() {
        return matchTable.rowKeySet();
    }

    public Set<E> getTargetElements() {
        return matchTable.columnKeySet();
    }

    public Set<E> getSourceElementsThatMatch(E targetElement){
        return matchTable.row(targetElement).keySet();
    }

    public Set<E> getTargetElementsMatchedBy(E sourceElement){
        return matchTable.column(sourceElement).keySet();
    }

    public SortedSet<E> getSortedSourceElemsThatMatch(E targetElement) {
        // Get the map with matcher -> type
        Map<E, T> sourceElements = matchTable.column(targetElement);
        // Order elements by the match type values
        Ordering<E> matchTypeComparator = Ordering.natural().onResultOf(Functions.forMap(sourceElements));
        return ImmutableSortedMap.copyOf(sourceElements, matchTypeComparator).keySet();
    }

    @Override
    public String toString() {
        return matchTable.toString();
    }
}

