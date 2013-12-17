/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC) http://citius.usc.es.
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

package es.usc.citius.composit.core.composition;

import es.usc.citius.composit.core.model.Operation;

import java.util.Collection;
import java.util.Set;

public interface InputDiscoverer<E> {
    /**
     * Discover operations that can consume the input.
     * @param input Provided input.
     * @return Relevant operations for the input.
     */
    Set<Operation<E>> findOperationsConsuming(E input);

    /**
     * Discover candidate operations that can consume some of the
     * inputs provided.
     * @param inputs
     * @return
     */
    Set<Operation<E>> findOperationsConsumingSome(Collection<E> inputs);
}
