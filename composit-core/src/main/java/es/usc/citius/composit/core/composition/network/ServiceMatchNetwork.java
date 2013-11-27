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

package es.usc.citius.composit.core.composition.network;


import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;

/**
 * <p>A Service Match Network (SMN) represents the full directed acyclic composition graph model. This network
 * consists of two well-differentiated parts: a leveled structure with multiple layers
 * that correspond with the levels (or layers) of the directed acyclic graph ({@link LeveledServices}),
 * and the match relations between concepts ({@link MatchGraph}). A Service Match Network contains all
 * the information needed to search for optimal compositions.</p>
 *
 * <center><img src="../../../../../../../../assets/images/javadoc/ServiceMatchNetwork.png"></img></center>
 *
 * <p>There is a default implementation called {@link HashServiceMatchNetwork} that holds all the information
 * in memory using hash maps.</p>
 *
 * @param <E> type of the inputs/outputs of the services.
 * @param <T> type of a match between inputs and outputs.
 */
public interface ServiceMatchNetwork<E,T extends Comparable<T>> extends LeveledServices<E>, MatchGraph<E,T> {
    // TODO; Add methods to retrieve source/target operation match
}
