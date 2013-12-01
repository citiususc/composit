/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
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

package es.usc.citius.composit.core.composition.network;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import es.usc.citius.composit.core.model.Operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public abstract class AbstractServiceMatchNetwork<E, T extends Comparable<T>> implements ServiceMatchNetwork<E,T> {

    @Override
    public Map<Operation<E>, Map<E, T>> getSourceOperationsThatMatch(E target) {
        Map<E, T> sources = getSourceElementsThatMatch(target);
        Map<Operation<E>, Map<E,T>> matchMap = new HashMap<Operation<E>, Map<E, T>>();
        for(Map.Entry<E,T> sourceEntry : sources.entrySet()){
            for(Operation<E> sourceOp : getOperationsWithOutput(sourceEntry.getKey())){
                Map<E,T> operationMatchMap = matchMap.get(sourceOp);
                if (operationMatchMap == null){
                    operationMatchMap = new HashMap<E, T>();
                    matchMap.put(sourceOp, operationMatchMap);
                }
                operationMatchMap.put(sourceEntry.getKey(), sourceEntry.getValue());
            }
        }
        return matchMap;
    }

    @Override
    public Map<Operation<E>, Map<E, T>> getTargetOperationsMatchedBy(E source) {
        Map<E, T> targets = getTargetElementsMatchedBy(source);
        Map<Operation<E>, Map<E,T>> matchMap = new HashMap<Operation<E>, Map<E, T>>();
        for(Map.Entry<E,T> targetEntry : targets.entrySet()){
            for(Operation<E> targetOp : getOperationsWithInput(targetEntry.getKey())){
                Map<E,T> operationMatchMap = matchMap.get(targetOp);
                if (operationMatchMap == null){
                    operationMatchMap = new HashMap<E, T>();
                    matchMap.put(targetOp, operationMatchMap);
                }
                operationMatchMap.put(targetEntry.getKey(), targetEntry.getValue());
            }
        }
        return matchMap;
    }

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
