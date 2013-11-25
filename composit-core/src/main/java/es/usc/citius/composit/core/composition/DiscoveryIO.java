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
