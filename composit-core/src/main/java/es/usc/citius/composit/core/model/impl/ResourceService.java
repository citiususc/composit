/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.composit.core.model.impl;

import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of a Service.
 *
 * @param <E> input/output data type.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ResourceService<E> extends ResourceComponent implements Service<E> {
    private Set<Operation<E>> operations = new HashSet<Operation<E>>();

    public ResourceService(String id, Set<ResourceOperation<E>> operations) {
        super(id);
        for(ResourceOperation<E> op : operations){
            addOperation(op);
        }
    }

    public ResourceService(String id) {
        super(id);
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
