package es.usc.citius.composit.core.composition;


import es.usc.citius.composit.core.model.Operation;

import java.util.Set;

public interface DiscoveryIO<E> {
    Set<Operation<E>> discoverOperationsForInput(E input);
    Set<Operation<E>> discoverOperationForOutput(E output);
}
