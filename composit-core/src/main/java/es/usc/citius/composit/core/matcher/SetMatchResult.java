package es.usc.citius.composit.core.matcher;


import com.google.common.base.Functions;
import com.google.common.collect.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class SetMatchResult<E, T extends Comparable<T>> {
    // Row: source, Column: target, Value matchType
    private Table<E, E, T> matchTable = HashBasedTable.create();
    private Set<E> elements = new HashSet<E>();

    public SetMatchResult(){}

    public SetMatchResult(Table<E,E,T> matchTable){
        this.matchTable = matchTable;
    }

    public void addMatch(E x, E y, T type) {
        matchTable.put(x, y, type);
        elements.add(x);
        elements.add(y);
    }

    public Table<E, E, T> getMatchTable() {
        return ImmutableTable.copyOf(matchTable);
    }

    public Set<E> getSourceElements() {
        return getMatchTable().rowKeySet();
    }

    public Set<E> getTargetElements() {
        return getMatchTable().columnKeySet();
    }

    public Set<E> getSourceElementsThatMatch(E targetElement){
        return getMatchTable().row(targetElement).keySet();
    }

    public Set<E> getTargetElementsMatchedBy(E sourceElement){
        return getMatchTable().column(sourceElement).keySet();
    }

    public SortedSet<E> getSortedSourceElemsThatMatch(E targetElement) {
        // Get the map with matcher -> type
        Map<E, T> sourceElements = this.getMatchTable().column(targetElement);
        // Order elements by the match type values
        Ordering<E> matchTypeComparator = Ordering.natural().onResultOf(Functions.forMap(sourceElements));
        return ImmutableSortedMap.copyOf(sourceElements, matchTypeComparator).keySet();
    }

    @Override
    public String toString() {
        return matchTable.toString();
    }
}

