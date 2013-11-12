package es.usc.citius.composit.core.matcher;

import com.google.common.base.Functions;
import com.google.common.collect.*;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * This interface defines the main methods for matching sets of elements.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface SetMatchFunction<E,T extends Comparable<T>> extends MatchFunction<E,T> {

    static class SetMatchResult<E, T extends Comparable<T>> {
        // Row: source, Column: target, Value matchType
        private Table<E,E,T> matchTable = HashBasedTable.create();

        public void addMatch(E x, E y, T type){
            matchTable.put(x,y,type);
        }

        public Table<E,E,T> getMatchTable() {
            return ImmutableTable.copyOf(matchTable);
        }

        public Set<E> getSourceElements(){
            return getMatchTable().rowKeySet();
        }

        public Set<E> getTargetElements(){
            return getMatchTable().columnKeySet();
        }

        public SortedSet<E> getSourceElementsThatMatch(E targetElement){
            // Get the map with matcher -> type
            Map<E,T> sourceElements = this.getMatchTable().column(targetElement);
            // Order elements by the match type values
            Ordering<E> matchTypeComparator = Ordering.natural().onResultOf(Functions.forMap(sourceElements));
            return ImmutableSortedMap.copyOf(sourceElements, matchTypeComparator).keySet();
        }

    }
    /**
     * Find at most one match from source to target. The first
     * element from source that matches an element from target is recorded.
     * The result does not have all matches between source and target.
     * This method can be used to fast check if each element from target
     * has at least one element from source that match it.
     *
     * @param source source elements (elements to match).
     * @param target target elements (elements to be matched).
     * @return a {@link Map} with keys = matcher elements and values = {@link Match} instances.
     */
    SetMatchResult<E,T> partialMatch(Set<E> source, Set<E> target);


    /**
     * Compute all matches between set x (source) and set y (target).
     * @param source {@link Set} of elements to match.
     * @param target {@link Set} elements to be matched.
     * @return
     */
    SetMatchResult<E,T> fullMatch(Set<E> source, Set<E> target);
}
