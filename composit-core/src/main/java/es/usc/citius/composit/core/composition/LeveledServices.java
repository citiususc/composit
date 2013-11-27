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
import es.usc.citius.composit.core.provider.ServiceProvider;

import java.util.List;
import java.util.Set;

/**
 * Represents a leveled service operation graph without edges (without input/output matching). This structure
 * is generated during the discovery phase of the composition, when a request (inputs/outputs) is provided
 * to the engine.
 *
 * <center><img src="../../../../../../../assets/images/javadoc/LeveledServices.png"></img></center>
 *
 * The structure is organized in levels or layers. The first level (level 0) and the last level contain just one dummy
 * operation. The dummy operation Source is located in level 0 and provides the inputs of the request, whereas
 * the Sink operations is in the last layer and receives the outputs of the request. The example shows a leveled
 * structure for a request with {@code inputs={o1,o2}} and {@code outputs={i9,i10,i11}}.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface LeveledServices<E> extends ServiceProvider<E> {


    /**
     * Returns the source operation with no inputs and outputs = request.getInputs
     * @return Source operation.
     */
    Operation<E> getSource();

    /**
     * Returns the sink operations.
     * @return Sink operation.
     */
    Operation<E> getSink();

    /**
     * Number of levels in this network. This method
     * is equivalent to {@code getLeveledList().size()}.
     *
     * @return integer number of levels.
     */
    int numberOfLevels();

    /**
     * Get the level where the operation is located.
     * @param operation Operation to find.
     * @return Level [0,numberOfLevels) where the operation is, or -1 if not found.
     */
    int levelOf(Operation<E> operation);

    /**
     * Get all operations preceding the indicated level.
     * @param level Level of the layer.
     * @return Operations in levels from 0 to level-1
     */
    Set<Operation<E>> getOperationsBeforeLevel(int level);

    /**
     * Get all operations after the indicated level.
     * @param level Level of the layer.
     * @return Operations in levels from level+1 to last level.
     */
    Set<Operation<E>> getOperationsAfterLevel(int level);

    /**
     * Get the operations in the indicated level.
     * @param level Level of the layer.
     * @return Operations in the provided level.
     */
    Set<Operation<E>> getOperationsAtLevel(int level);

    /**
     * Get a list with the set of operations in each level of the list.
     * @return list with set of operations organized in levels.
     */
    List<Set<Operation<E>>> getLeveledList();
}
