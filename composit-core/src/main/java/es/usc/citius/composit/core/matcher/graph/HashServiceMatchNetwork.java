package es.usc.citius.composit.core.matcher.graph;

import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashServiceMatchNetwork<E,T extends Comparable<T>> implements ServiceMatchNetwork<E,T> {

    private LeveledServices<E> layers;
    private SetMatchFunction<E,T> setMatcher;
    private MatchTable<E,T> matchTable = new MatchTable<E, T>();
    private HashMatchGraph<E,T> matchGraph;

    public HashServiceMatchNetwork(LeveledServices<E> layers, SetMatchFunction<E, T> setMatcher) {
        this.layers = layers;
        this.matchTable = computeLeveledMatch();
        this.matchGraph = new HashMatchGraph<E, T>(matchTable);
        this.setMatcher = setMatcher;
    }

    private MatchTable<E,T> computeLeveledMatch(){
        MatchTable<E,T> matchTable = new MatchTable<E, T>();
        for(int level = 0; level < layers.numberOfLevels(); level++){
            Set<Operation<E>> ops = layers.getOperationsAtLevel(level);
            for(Operation<E> op : ops){
                for(Operation<E> dest : layers.getOperationsAfterLevel(level)){
                    // Compute full match between these operations
                    // matcher -> matched
                    MatchTable<E,T> matchResult =
                            setMatcher.fullMatch(op.getSignature().getOutputs(),
                                    dest.getSignature().getInputs());

                    for(E source : matchResult.getSourceElements()){
                        for(E target : matchResult.getTargetElements()){
                            matchTable.addMatch(source, target, matchResult.getMatchTable().get(source, target));
                        }
                    }
                }
            }
        }
        return matchTable;
    }



    @Override
    public Operation<E> getSource() {
        return layers.getSource();
    }

    @Override
    public Operation<E> getSink() {
        return layers.getSink();
    }

    @Override
    public int numberOfLevels() {
        return layers.numberOfLevels();
    }

    @Override
    public int levelOf(Operation<E> operation) {
        return layers.levelOf(operation);
    }

    @Override
    public Set<Operation<E>> getOperationsBeforeLevel(int level) {
        return layers.getOperationsBeforeLevel(level);
    }

    @Override
    public Set<Operation<E>> getOperationsAfterLevel(int level) {
        return layers.getOperationsAfterLevel(level);
    }

    @Override
    public Set<Operation<E>> getOperationsAtLevel(int level) {
        return layers.getOperationsAtLevel(level);
    }

    @Override
    public List<Set<Operation<E>>> getLeveledList() {
        return layers.getLeveledList();
    }

    @Override
    public Service<E> getService(String serviceID) {
        return layers.getService(serviceID);
    }

    @Override
    public Operation<E> getOperation(String operationID) {
        return layers.getOperation(operationID);
    }

    public Iterable<Operation<E>> getOperationsWithInput(E input) {
        return layers.getOperationsWithInput(input);
    }

    public Iterable<Operation<E>> getOperationsWithOutput(E output) {
        return layers.getOperationsWithOutput(output);
    }

    @Override
    public Iterable<Operation<E>> getOperations() {
        return layers.getOperations();
    }

    @Override
    public Iterable<Service<E>> getServices() {
        return layers.getServices();
    }

    @Override
    public Set<String> listOperations() {
        return layers.listOperations();
    }

    @Override
    public Set<String> listServices() {
        return layers.listServices();
    }

    @Override
    public Set<E> getElements() {
        return matchGraph.getElements();
    }

    @Override
    public Map<E,T> getTargetElementsMatchedBy(E source) {
        return matchGraph.getTargetElementsMatchedBy(source);
    }

    @Override
    public Map<E,T> getSourceElementsThatMatch(E target) {
        return matchGraph.getSourceElementsThatMatch(target);
    }

    @Override
    public Map<E,T> getTargetElementsMatchedBy(E source, T type, TypeSelector selector) {
        return matchGraph.getTargetElementsMatchedBy(source, type, selector);
    }

    @Override
    public Map<E,T> getSourceElementsThatMatch(E target, T type, TypeSelector selector) {
        return matchGraph.getSourceElementsThatMatch(target, type, selector);
    }

    @Override
    public MatchTable<E,T> partialMatch(Set<E> source, Set<E> target) {
        return matchGraph.partialMatch(source, target);
    }

    @Override
    public MatchTable<E,T> fullMatch(Set<E> source, Set<E> target) {
        return matchGraph.fullMatch(source, target);
    }

    @Override
    public T match(E source, E target) {
        return matchGraph.match(source, target);
    }
}
