package es.usc.citius.composit.core.model.impl;

import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ResourceService<E> extends Resource implements Service<E> {
    private Set<Operation<E>> operations;

    public ResourceService(String id, Set<ResourceOperation<E>> operations) {
        super(id);
        for(ResourceOperation<E> op : operations){
            addOperation(op);
        }
    }

    public ResourceService(String id) {
        super(id);
        this.operations = new HashSet<Operation<E>>();
    }

    @Override
    public Set<Operation<E>> getOperations() {
        return Collections.unmodifiableSet(this.operations);
    }

    public void addOperation(ResourceOperation<E> op){
        this.operations.add(op);
        op.setOwner(this);
    }
}
