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
