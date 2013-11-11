package es.usc.citius.composit.core.model.impl;

import com.google.common.collect.ImmutableSet;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ResourceService<E> extends Resource implements Service<E> {
    private final Set<Operation<E>> operations;

    public ResourceService(String id, Set<Operation<E>> operations) {
        super(id);
        this.operations = ImmutableSet.copyOf(operations);
    }

    @Override
    public Set<Operation<E>> getOperations() {
        return this.operations;
    }
}
