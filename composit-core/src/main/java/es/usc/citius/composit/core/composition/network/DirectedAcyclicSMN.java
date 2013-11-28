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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.graph.HashMatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Directed acyclic implementation of a {@link ServiceMatchNetwork}. It builds a match network using a
 * leveled services structure and a set matcher to compute the matches. The I/O match is computed
 * forwards, starting from level 0 (i.e., outputs of operation at level 0 can match only inputs
 * of services from level 1 to N (there are no match cycles). The resultant match network like the
 * one represented in {@link ServiceMatchNetwork}.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class DirectedAcyclicSMN<E,T extends Comparable<T>> implements ServiceMatchNetwork<E,T> {

    private LeveledServices<E> layers;
    private SetMatchFunction<E,T> setMatcher;
    private MatchTable<E,T> matchTable = new MatchTable<E, T>();
    private HashMatchGraph<E,T> matchGraph;

    public DirectedAcyclicSMN(LeveledServices<E> layers, SetMatchFunction<E, T> setMatcher) {
        this.layers = layers;
        this.setMatcher = setMatcher;
        this.matchTable = computeLeveledMatch();
        this.matchGraph = new HashMatchGraph<E, T>(matchTable);
    }

    private final MatchTable<E,T> computeLeveledMatch(){
        Table<E,E,T> table = HashBasedTable.create();
        for(int level = 0; level < layers.numberOfLevels(); level++){
            Set<Operation<E>> ops = layers.getOperationsAtLevel(level);
            Set<Operation<E>> followingOps = layers.getOperationsAfterLevel(level);
            for(Operation<E> source : ops){
                for(Operation<E> target : followingOps){
                    // Compute full match between these operations
                    // matcher -> matched
                    MatchTable<E,T> matchResult =
                            setMatcher.fullMatch(source.getSignature().getOutputs(),
                                    target.getSignature().getInputs());
                    table.putAll(matchResult.getMatchTable());
                }
            }
        }

        return new MatchTable<E, T>(table);
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

    @Override
    public Map<Operation<E>, Table<E, E, T>> getSourceOperationsThatMatch(Operation<E> target) {
        // First, compute the source elements that match the op.inputs
        Map<Operation<E>, Table<E,E,T>> matchMap = new HashMap<Operation<E>, Table<E, E, T>>();
        for(E targetInput : target.getSignature().getInputs()){
            Map<E,T> sourceMatch = getSourceElementsThatMatch(targetInput);
            // Find the providers
            for(Map.Entry<E,T> sourceMatchEntry : sourceMatch.entrySet()){
                E sourceOutput = sourceMatchEntry.getKey();
                Set<Operation<E>> sourceOps = Sets.newHashSet(getOperationsWithOutput(sourceOutput));
                // Annotate source output->target input match
                for(Operation<E> op : sourceOps){
                    Table<E,E,T> matchTable = matchMap.get(op);
                    if (matchTable==null){
                        matchTable = HashBasedTable.create();
                        matchMap.put(op, matchTable);
                    }
                    // Add match entry
                    matchTable.put(sourceOutput, targetInput, sourceMatchEntry.getValue());
                }
            }
        }
        return matchMap;
    }

    @Override
    public Map<Operation<E>, Table<E, E, T>> getTargetOperationsMatchedBy(Operation<E> source) {
        // First, compute the target elements matched by op.outputs
        Map<Operation<E>, Table<E,E,T>> matchMap = new HashMap<Operation<E>, Table<E, E, T>>();
        for(E sourceOutput : source.getSignature().getOutputs()){
            Map<E,T> targetMatch = getTargetElementsMatchedBy(sourceOutput);
            // Find the providers
            for(Map.Entry<E,T> targetMatchEntry : targetMatch.entrySet()){
                E targetInput = targetMatchEntry.getKey();
                Set<Operation<E>> targetOps = Sets.newHashSet(getOperationsWithInput(targetInput));
                // Annotate output -> input
                for(Operation<E> op : targetOps){
                    Table<E,E,T> matchTable = matchMap.get(op);
                    if (matchTable==null){
                        matchTable = HashBasedTable.create();
                        matchMap.put(op, matchTable);
                    }
                    // Add match entry
                    matchTable.put(sourceOutput, targetInput, targetMatchEntry.getValue());
                }
            }
        }
        return matchMap;
    }
}
