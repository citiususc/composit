package es.usc.citius.composit.core.matcher;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface MatchFunction<E,T extends Comparable<T>> {
    Match<E,T> match(E source, E target);
}
