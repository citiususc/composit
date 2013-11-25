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
import es.usc.citius.composit.core.model.Signature;


/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ResourceOperation<E> extends ResourceComponent implements Operation<E> {
    private Service<E> owner;
    private final Signature<E> signature;

    public ResourceOperation(String id, Service<E> owner, Signature<E> signature) {
        super(id);
        this.owner = owner;
        this.signature = signature;
    }

    public ResourceOperation(String id, Signature<E> signature) {
        super(id);
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

    public void setOwner(Service<E> owner) {
        this.owner = owner;
    }
}
