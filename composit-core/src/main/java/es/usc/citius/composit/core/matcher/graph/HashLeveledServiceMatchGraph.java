package es.usc.citius.composit.core.matcher.graph;

import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.SetMatchResult;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashLeveledServiceMatchGraph<E,T extends Comparable<T>> implements MatchGraph<E,T>, LeveledServices<E> {

    private LeveledServices<E> layers;
    private SetMatchFunction<E,T> setMatcher;
    private SetMatchResult<E,T> matchTable = new SetMatchResult<E, T>();
    private HashMatchGraph<E,T> matchGraph;


    public HashLeveledServiceMatchGraph(LeveledServices<E> layers, SetMatchFunction<E,T> setMatcher) {
        this.layers = layers;
        this.matchTable = computeLeveledMatch();
        this.matchGraph = new HashMatchGraph<E, T>(matchTable);
        this.setMatcher = setMatcher;
    }

    private SetMatchResult<E,T> computeLeveledMatch(){
        SetMatchResult<E,T> matchTable = new SetMatchResult<E, T>();
        for(int level = 0; level < layers.numberOfLevels(); level++){
            Set<Operation<E>> ops = layers.getOperationsAtLevel(level);
            for(Operation<E> op : ops){
                for(Operation<E> dest : layers.getOperationsAfterLevel(level)){
                    // Compute full match between these operations
                    // matcher -> matched
                    SetMatchResult<E,T> matchResult =
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
    public Set<Service<E>> getServices() {
        return layers.getServices();
    }

    @Override
    public Set<Operation<E>> getOperations() {
        return layers.getOperations();
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
    public Set<Operation<E>> getOperationsWithInput(E input) {
        return layers.getOperationsWithInput(input);
    }

    @Override
    public Set<Operation<E>> getOperationsWithOutput(E output) {
        return layers.getOperationsWithOutput(output);
    }

    @Override
    public List<Set<Operation<E>>> getLeveledList() {
        return layers.getLeveledList();
    }

    @Override
    public Set<E> getElements() {
        return matchGraph.getElements();
    }

    @Override
    public Set<E> getTargetElementsMatchedBy(E source) {
        return matchGraph.getTargetElementsMatchedBy(source);
    }

    @Override
    public Set<E> getSourceElementsThatMatch(E target) {
        return matchGraph.getSourceElementsThatMatch(target);
    }

    @Override
    public Set<E> getTargetElementsMatchedBy(E source, T type, TypeSelector selector) {
        return matchGraph.getTargetElementsMatchedBy(source, type, selector);
    }

    @Override
    public Set<E> getSourceElementsThatMatch(E target, T type, TypeSelector selector) {
        return matchGraph.getSourceElementsThatMatch(target, type, selector);
    }

    @Override
    public SetMatchResult<E,T> partialMatch(Set<E> source, Set<E> target) {
        return matchGraph.partialMatch(source, target);
    }

    @Override
    public SetMatchResult<E,T> fullMatch(Set<E> source, Set<E> target) {
        return matchGraph.fullMatch(source, target);
    }

    @Override
    public T match(E source, E target) {
        return matchGraph.match(source, target);
    }
}
