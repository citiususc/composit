package es.usc.citius.composit.core.composition;


import es.usc.citius.composit.core.model.Operation;

import java.util.Set;

/**
 * Interface to define input/output operation discovery strategies.
 * @param <E>
 */
public interface DiscoveryIO<E> {
    /**
     * Discover relevant operations for an input. In general, these
     * operations, depending on the matching strategies of the composition engine,
     * are the operations that can consume the provided input.
     * @param input Provided input.
     * @return Relevant operations for the input.
     */
    Set<Operation<E>> discoverOperationsForInput(E input);

    /**
     * Discover relevant operation for an output. These operations
     * are those that can produce an output that is compatible with the
     * provided one.
     * @param output
     * @return
     */
    Set<Operation<E>> discoverOperationsForOutput(E output);
}
