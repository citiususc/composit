package es.usc.citius.composit.core.composition;

import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.List;
import java.util.Set;

/**
 * Contains a source node at level 0, and a sink node at the last level. The source node has no inputs
 * and provides as outputs the inputs of the composition request. The sink node has no outputs and requires
 * as inputs the outputs of the composition request.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface LeveledServices<E> {

    /**
     * TODO; Method from ServiceDataProvider. Remove
     * Get all services in the graph.
     * @return Set of services.
     */
    Set<Service<E>> getServices();

    /**
     * TODO; Method from ServiceDataProvider. Remove
     * Get all operations in the graph.
     * @return Set of operations.
     */
    Set<Operation<E>> getOperations();

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
     * TODO; Method from ServiceDataProvider. Remove
     * Get all operations in the graph that have the provided input within their inputs.
     * @param input input.
     * @return Set of operations with the input. Each retrieved operation holds {@code operation.getInputs().contains(input)}.
     */
    Set<Operation<E>> getOperationsWithInput(E input);

    /**
     * TODO; Method from ServiceDataProvider. Remove
     * Get all operations in the graph that have the provided output within their outputs.
     * @param output output.
     * @return Set of operations with the output. Each retrieved operation holds {@code operation.getOutputs().contains(output)}.
     */
    Set<Operation<E>> getOperationsWithOutput(E output);

    /**
     * Get a list with the set of operations in each level of the list.
     * @return list with set of operations organized in levels.
     */
    List<Set<Operation<E>>> getLeveledList();
}
