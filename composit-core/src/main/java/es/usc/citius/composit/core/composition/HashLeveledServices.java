package es.usc.citius.composit.core.composition;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashLeveledServices<E> implements LeveledServices<E> {
    // Service operationLayers
    private List<Set<Operation<E>>> operationLayers;

    // Elements (inputs/outputs) in the graph
    private Set<E> elements = new HashSet<E>();
    // Level map to get the level of each operation
    private Map<Operation<E>, Integer> levelMap = new HashMap<Operation<E>, Integer>();
    // Operations provided to the constructor
    private Set<Operation<E>> operations = new HashSet<Operation<E>>();
    // Services extracted from the operations
    private Set<Service<E>> services = new HashSet<Service<E>>();
    // Output hash mmap (output -> operations with the output)
    private SetMultimap<E, Operation<E>> inputOperationMap = HashMultimap.create();
    // Input hash mmap (input -> operations with the input)
    private SetMultimap<E, Operation<E>> outputOperationMap = HashMultimap.create();

    public HashLeveledServices(List<Set<Operation<E>>> operationLayers) {
        this.operationLayers = operationLayers;
        int level=0;
        for(Set<Operation<E>> ops : operationLayers){
            for(Operation<E> op : ops){
                levelMap.put(op, level++);
                operations.add(op);
            }
            operations.addAll(ops);
        }
        // Index inputs, outputs and operations
        index(operations);
    }

    private void index(Set<Operation<E>> operations){
        for(Operation<E> op : operations){
            // Index service owners
            this.services.add(op.getServiceOwner());
            // Index inputs and outputs
            this.elements.addAll(op.getSignature().getInputs());
            this.elements.addAll(op.getSignature().getOutputs());

            for(E input : op.getSignature().getInputs()){
                inputOperationMap.get(input).add(op);
            }
            for(E output : op.getSignature().getOutputs()){
                outputOperationMap.get(output).add(op);
            }
        }
    }

    @Override
    public Operation<E> getSource() {
        if (operationLayers.get(0).size()!=1){
            throw new IllegalStateException("Malformed match network. Invalid source layer.");
        }
        return operationLayers.get(0).iterator().next();
    }

    @Override
    public Operation<E> getSink() {
        if (operationLayers.get(operationLayers.size()-1).size()!=1){
            throw new IllegalStateException("Malformed match network. Invalid sink layer.");
        }
        return operationLayers.get(0).iterator().next();
    }

    @Override
    public int numberOfLevels() {
        return operationLayers.size();
    }

    @Override
    public int levelOf(Operation<E> operation) {
        return levelMap.get(operation);
    }

    @Override
    public Set<Operation<E>> getOperationsBeforeLevel(int level) {
        Set<Operation<E>> set = new HashSet<Operation<E>>();
        for(int i =0; i < level; i++){
            set.addAll(operationLayers.get(i));
        }
        return set;
    }

    @Override
    public Set<Operation<E>> getOperationsAfterLevel(int level) {
        Set<Operation<E>> set = new HashSet<Operation<E>>();
        for(int i = level+1; i < operationLayers.size(); i++){
            set.addAll(operationLayers.get(i));
        }
        return set;
    }

    @Override
    public Set<Operation<E>> getOperationsAtLevel(int level) {
        if (level < 0 || level > operationLayers.size()-1){
            throw new IllegalArgumentException("Invalid level number. Max level supported: " + (operationLayers.size()-1));
        }
        return ImmutableSet.copyOf(operationLayers.get(level));
    }

    @Override
    public Set<Service<E>> getServices() {
        return ImmutableSet.copyOf(services);
    }

    @Override
    public Set<Operation<E>> getOperations() {
        return ImmutableSet.copyOf(operations);
    }

    @Override
    public Set<Operation<E>> getOperationsWithInput(E input) {
        return ImmutableSet.copyOf(inputOperationMap.get(input));
    }

    @Override
    public Set<Operation<E>> getOperationsWithOutput(E output) {
        return ImmutableSet.copyOf(outputOperationMap.get(output));
    }

    @Override
    public List<Set<Operation<E>>> getLeveledList() {
        return ImmutableList.copyOf(this.operationLayers);
    }


}
