package es.usc.citius.composit.core.matcher.graph;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.SetMatchResult;

import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashMatchGraph<E, T extends Comparable<T>> implements MatchGraph<E,T>{

    private SetMatchResult<E,T> matchTable;
    private Set<E> elements;

    public HashMatchGraph(SetMatchFunction<E,T> setMatcher, Set<E> elements) {
        this.matchTable = setMatcher.fullMatch(elements, elements);
        this.elements = elements;
    }

    public HashMatchGraph(SetMatchResult<E,T> matchResult) {
        this.matchTable = matchResult;
        this.elements = Sets.union(matchResult.getMatchTable().columnKeySet(), matchResult.getMatchTable().rowKeySet());
    }

    @Override
    public Set<E> getElements() {
        return ImmutableSet.copyOf(elements);
    }

    @Override
    public Set<E> getTargetElementsMatchedBy(E source) {
        // Target elements are the column keys
        return ImmutableSet.copyOf(this.matchTable.getMatchTable().row(source).keySet());
    }

    @Override
    public Set<E> getSourceElementsThatMatch(E target) {
        // Source elements are the row keys
        return ImmutableSet.copyOf(this.matchTable.getMatchTable().column(target).keySet());
    }

    @Override
    public Set<E> getTargetElementsMatchedBy(E source, T type, TypeSelector selector) {
        // First get all from matchTable and then filter using the selector
        Set<E> target = getTargetElementsMatchedBy(source);
        // Filter all
        for(E resource : target){
            switch(selector){
                case AT_LEAST:
                    break;
                case AT_MOST:
                    break;
                case EXACT:
            }
        }
        return target;
    }

    @Override
    public Set<E> getSourceElementsThatMatch(E target, T type, TypeSelector selector) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SetMatchResult<E, T> partialMatch(Set<E> source, Set<E> target) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SetMatchResult<E, T> fullMatch(Set<E> source, Set<E> target) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T match(E source, E target) {
        return matchTable.getMatchTable().get(source, target);
    }
}
