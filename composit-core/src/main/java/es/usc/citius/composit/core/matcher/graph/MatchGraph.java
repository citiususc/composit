package es.usc.citius.composit.core.matcher.graph;

import es.usc.citius.composit.core.matcher.SetMatchFunction;

import java.util.Map;
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
    Map<E,T> getTargetElementsMatchedBy(E source);

    /**
     * from target to source
     * @param target
     * @return
     */
    Map<E,T> getSourceElementsThatMatch(E target);

    /**
     * Get all target elements in the graph that are matched by source with match type t.
     * @param source
     * @return
     */
    Map<E,T> getTargetElementsMatchedBy(E source, T type, TypeSelector selector);

    /**
     *
     * @param target
     * @param type
     * @return
     */
    Map<E,T> getSourceElementsThatMatch(E target, T type, TypeSelector selector);
}
