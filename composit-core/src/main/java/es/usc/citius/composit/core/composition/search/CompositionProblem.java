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

package es.usc.citius.composit.core.composition.search;

import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;

/**
 * Defines the components of a composition search problem. A problem is defined by two components:
 * <ol>
 *     <li>a {@link MatchGraph} that represents the matching between inputs / outputs.</li>
 *     <li>a {@link DiscoveryIO} to discover candidate services using the input/output information.</li>
 * </ol>
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface CompositionProblem<E, T extends Comparable<T>> {
    MatchGraph<E, T> getMatchGraph();
    DiscoveryIO<E> getDiscoveryIO();
}
