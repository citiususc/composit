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

package es.usc.citius.composit.core.composition.optimization;


import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.network.DirectedAcyclicSMN;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BackwardMinimizationOptimizer<E, T extends Comparable<T>> implements NetworkOptimizer<E,T> {
    private static final Logger log = LoggerFactory.getLogger(BackwardMinimizationOptimizer.class);

    @Override
    public ServiceMatchNetwork<E, T> optimize(ServiceMatchNetwork<E, T> network) {
        Stopwatch globalWatch = Stopwatch.createStarted();
        Stopwatch localWatch = Stopwatch.createUnstarted();
        Set<E> newInputs = new HashSet<E>();
        List<Set<Operation<E>>> optimized = new ArrayList<Set<Operation<E>>>(network.numberOfLevels());
        log.debug("Starting service-backward optimization...");
        localWatch.start();
        for(int i=network.numberOfLevels()-1;i>=0;i--){
            Set<Operation<E>> current = network.getOperationsAtLevel(i);
            log.debug(" > Analyzing network level {} : {}", i, current);
            Set<Operation<E>> optimizedSet = new HashSet<Operation<E>>();
            Set<E> futureInputs = new HashSet<E>();
            // Find all services that produces at least one of the required inputs. If new inputs is
            // empty, then select all
            for(Operation<E> op : current){
                log.debug("\t\tChecking operation {}", op.getID());
                if (newInputs.isEmpty()){
                    futureInputs.addAll(op.getSignature().getInputs());
                    optimizedSet.add(op);
                    log.debug("\t\t+ {} selected as a mandatory operation", op.getID());
                } else {
                    boolean used = false;
                    next:
                    for(E output : op.getSignature().getOutputs()){
                        for(E input : newInputs){
                            used = network.match(output, input) != null;
                            if (used){
                                log.debug("\t\t+ Operation {} marked as useful (match detected between output {} and input {})", op.getID(), output, input);
                                optimizedSet.add(op);
                                // Update new inputs
                                futureInputs.addAll(op.getSignature().getInputs());
                                break next;
                            }
                        }
                    }
                    if (!used) log.debug("\t\t- Operation {} marked as useless", op.getID());
                }
                //log.debug(" Inputs for the next iteration: {}", futureInputs);
            }
            newInputs.addAll(futureInputs);
            optimized.add(optimizedSet);
        }
        Collections.reverse(optimized);
        // Create a new match network
        localWatch.reset().start();
        ServiceMatchNetwork<E, T> optimizedNetwork = new DirectedAcyclicSMN<E, T>(new HashLeveledServices<E>(optimized), network);
        localWatch.stop();
        log.debug(" > Optimized match network created in {}", localWatch.toString());
        log.debug("Backward Optimization done in {}. Size before/after {}/{}", globalWatch.stop().toString(), network.listOperations().size(), optimizedNetwork.listOperations().size());
        // Create a new optimized service match network
        return optimizedNetwork;
    }
}
