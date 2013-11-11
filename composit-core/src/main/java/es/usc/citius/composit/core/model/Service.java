package es.usc.citius.composit.core.model;

import java.util.Set;

/**
 * Generic interface to define a simple service.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface Service<E> {
    Set<Operation<E>> getOperations();
}
