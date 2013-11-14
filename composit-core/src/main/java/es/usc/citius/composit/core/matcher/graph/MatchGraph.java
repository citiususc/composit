package es.usc.citius.composit.core.matcher.graph;

import es.usc.citius.composit.core.matcher.SetMatchFunction;

import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface MatchGraph<E,T extends Comparable<T>> extends SetMatchFunction<E,T> {

    enum TypeSelector {
        AT_LEAST, AT_MOST, EXACT
    }

    Set<E> getElements();
    /**
     * from source to target
     * @param source
     * @return
     */
    Set<E> getTargetElementsMatchedBy(E source);

    /**
     * from target to source
     * @param target
     * @return
     */
    Set<E> getSourceElementsThatMatch(E target);

    /**
     * Get all target elements in the graph that are matched by source with match type t.
     * @param source
     * @return
     */
    Set<E> getTargetElementsMatchedBy(E source, T type, TypeSelector selector);

    /**
     *
     * @param target
     * @param type
     * @return
     */
    Set<E> getSourceElementsThatMatch(E target, T type, TypeSelector selector);
}
