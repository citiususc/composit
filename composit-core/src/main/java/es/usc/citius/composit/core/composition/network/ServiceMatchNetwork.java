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


import com.google.common.collect.Table;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;

import java.util.Map;

/**
 * <p>A Service Match Network (SMN) represents a directed composition match graph model. This network
 * consists of two well-differentiated parts: a leveled structure of service operations with multiple layers
 * that correspond with the levels (or layers) of the directed graph ({@link LeveledServices}),
 * and the match relations between concepts ({@link MatchGraph}). A Service Match Network contains all
 * the information needed to search for optimal compositions.</p>
 *
 * <center>
 *     <img src="../../../../../../../../assets/images/javadoc/ServiceMatchNetwork.png"
 *          alt="Service Match Network representation." border="0" />
 * </center>
 *
 * <p>There is a default implementation called {@link DirectedAcyclicSMN} that holds all the information
 * in memory using hash maps.</p>
 *
 * @param <E> type of the inputs/outputs of the services.
 * @param <T> type of a match between inputs and outputs.
 */
public interface ServiceMatchNetwork<E,T extends Comparable<T>> extends LeveledServices<E>, MatchGraph<E,T> {

    Map<Operation<E>, Map<E,T>> getSourceOperationsThatMatch(E target);

    Map<Operation<E>, Map<E,T>> getTargetOperationsMatchedBy(E source);

    Map<Operation<E>, Table<E,E,T>> getSourceOperationsThatMatch(Operation<E> target);

    Map<Operation<E>, Table<E,E,T>> getTargetOperationsMatchedBy(Operation<E> source);

}
