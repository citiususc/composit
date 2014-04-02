/*
 * Copyright 2014 Centro de Investigación en Tecnoloxías da Información (CITIUS),
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

package es.usc.citius.composit.core.composition.search;


import es.usc.citius.composit.core.composition.InputDiscoverer;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;

public class DefaultCompositionProblem<E,T extends Comparable<T>> implements CompositionProblem<E,T> {

    private MatchGraph<E,T> graph;
    private InputDiscoverer<E> inputDiscoverer;

    public DefaultCompositionProblem(MatchGraph<E, T> graph, InputDiscoverer<E> inputDiscoverer) {
        this.graph = graph;
        this.inputDiscoverer = inputDiscoverer;
    }

    @Override
    public MatchGraph<E, T> getMatchGraph() {
        return null;
    }

    @Override
    public InputDiscoverer<E> getInputDiscoverer() {
        return null;
    }
}
