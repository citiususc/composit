package es.usc.citius.composit.core.matcher;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface MatchFunction<E,T extends Comparable<T>> {
    /**
     * Return the match type between source and target elements.
     * @param source source element to match
     * @param target target element to be matched
     * @return match type or null if the match does not exist or cannot be computed.
     */
    T match(E source, E target);
}
