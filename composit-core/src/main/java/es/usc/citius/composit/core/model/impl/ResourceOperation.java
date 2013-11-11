package es.usc.citius.composit.core.model.impl;

import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;
import es.usc.citius.composit.core.model.Signature;


/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ResourceOperation<E> extends Resource implements Operation<E> {
    private final Service<E> owner;
    private final Signature<E> signature;

    public ResourceOperation(String id, Service<E> owner, Signature<E> signature) {
        super(id);
        this.owner = owner;
        this.signature = signature;
    }

    @Override
    public Service<E> getServiceOwner() {
        return this.owner;
    }

    @Override
    public Signature<E> getSignature() {
        return this.signature;
    }
}
