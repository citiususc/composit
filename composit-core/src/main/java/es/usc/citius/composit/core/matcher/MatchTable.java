package es.usc.citius.composit.core.matcher;


import com.google.common.base.Functions;
import com.google.common.collect.*;

import java.util.HashSet;
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

