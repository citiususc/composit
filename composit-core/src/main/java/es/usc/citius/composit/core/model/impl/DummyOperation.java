package es.usc.citius.composit.core.model.impl;

import es.usc.citius.composit.core.model.Signature;

/**
 * Dummy operations are not linked with real operations and do not have
 * service owners. Dummy ops are used in some contexts, for example, to
 * generate the service match graph, which has two dummies (source and sink
 * operations).
 *
 * @param <E>
 */
public class DummyOperation<E> extends ResourceOperation<E> {
    public DummyOperation(String id, Signature<E> signature) {
        super(id, null, signature);
    }
}
