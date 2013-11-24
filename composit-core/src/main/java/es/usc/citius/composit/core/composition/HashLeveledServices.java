package es.usc.citius.composit.core.composition;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;
import es.usc.citius.composit.core.provider.ServiceProvider;

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
    // Operations in the leveled structure
    private Map<String, Operation<E>> operations = new HashMap<String, Operation<E>>();
    //private Set<Operation<E>> operations = new HashSet<Operation<E>>();
    // Services extracted from the operations
    private Map<String, Service<E>> services = new HashMap<String, Service<E>>();
    //private Set<Service<E>> services = new HashSet<Service<E>>();
    // Output hash mmap (output -> operations with the output)
    private SetMultimap<E, Operation<E>> inputOperationMap = HashMultimap.create();
    // Input hash mmap (input -> operations with the input)
    private SetMultimap<E, Operation<E>> outputOperationMap = HashMultimap.create();

    public HashLeveledServices(List<Set<Operation<E>>> operationLayers) {
        if (operationLayers.get(0).size()!=1){
            throw new IllegalArgumentException("Source operation expected in the first layer.");
        }
        if (operationLayers.get(operationLayers.size()-1).size() != 1){
            throw new IllegalArgumentException("Sink operation expected in the last layer.");
        }
        this.operationLayers = operationLayers;
        int level=0;
        for(Set<Operation<E>> ops : operationLayers){
            for(Operation<E> op : ops){
                levelMap.put(op, level++);
                operations.put(op.getID(), op);
            }
            //operations.addAll(ops);
        }
        // Index inputs, outputs and operations
        index(operations.values());
    }

    private void index(Collection<Operation<E>> operations){
        for(Operation<E> op : operations){
            // Index service owners
            Service<E> service = op.getServiceOwner();
            this.services.put(service.getID(), service);
            //this.services.add(op.getServiceOwner());
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
        if (level < 0 || level > operationLayers.size()-1){
            throw new IllegalArgumentException("Invalid level number. Max level supported: " + (operationLayers.size()-1));
        }
        Set<Operation<E>> set = new HashSet<Operation<E>>();
        for(int i =0; i < level; i++){
            set.addAll(operationLayers.get(i));
        }
        return set;
    }

    @Override
    public Set<Operation<E>> getOperationsAfterLevel(int level) {
        if (level < 0 || level > operationLayers.size()-1){
            throw new IllegalArgumentException("Invalid level number. Max level supported: " + (operationLayers.size()-1));
        }
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
        return ImmutableSet.copyOf(services.values());
    }

    @Override
    public Set<String> listOperations() {
        return ImmutableSet.copyOf(operations.keySet());
    }

    @Override
    public Set<String> listServices() {
        return ImmutableSet.copyOf(services.keySet());
    }

    @Override
    public Set<Operation<E>> getOperations() {
        return ImmutableSet.copyOf(operations.values());
    }

    @Override
    public Service<E> getService(String serviceID) {
        return services.get(serviceID);
    }

    @Override
    public Operation<E> getOperation(String operationID) {
        return operations.get(operationID);
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
