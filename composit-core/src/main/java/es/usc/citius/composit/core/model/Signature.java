package es.usc.citius.composit.core.model;

import java.util.Set;

/**
 * Generic I/O Service Signature.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface Signature<E> {
    Set<E> getInputs();
    Set<E> getOutputs();
}

