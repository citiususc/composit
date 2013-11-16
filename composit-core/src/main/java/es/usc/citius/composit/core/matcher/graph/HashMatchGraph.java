package es.usc.citius.composit.core.matcher.graph;

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.MatchTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashMatchGraph<E, T extends Comparable<T>> implements MatchGraph<E,T>{
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

    private Map<E,T> filter(final Map<E,T> map, final T type, final TypeSelector selector){
        // First get all from matchTable and then filter using the selector
        return
                Maps.filterEntries(map, new Predicate<Map.Entry<E, T>>() {
                    @Override
                    public boolean apply(Map.Entry<E, T> input) {
                        switch(selector){
                            case AT_LEAST:
                                // The match type is at least as good as the provided one.
                                // Example: Match at least subsumes: accepts exact, plugin and subsumes
                                return input.getValue().compareTo(type)<=0;
                            case AT_MOST:
                                return input.getValue().compareTo(type)>=0;
                            default:
                                return input.getValue().equals(type);
                        }
                    }
                });
    }

    @Override
    public Map<E,T> getTargetElementsMatchedBy(final E source, final T type, final TypeSelector selector) {
        // First get all from matchTable and then filter using the selector
        return filter(getTargetElementsMatchedBy(source), type, selector);
    }

    @Override
    public Map<E,T> getSourceElementsThatMatch(final E target, final T type, final TypeSelector selector) {
        return filter(getSourceElementsThatMatch(target), type, selector);
    }

    @Override
    public MatchTable<E, T> partialMatch(Set<E> source, Set<E> target) {
        throw new UnsupportedOperationException("Partial match unsupported.");
    }

    @Override
    public MatchTable<E, T> fullMatch(Set<E> source, Set<E> target) {
        MatchTable<E,T> matchResult = new MatchTable<E, T>();
        for(E x : source){
            for(E y : target){
                T match = matchTable.getMatchTable().get(x,y);
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
