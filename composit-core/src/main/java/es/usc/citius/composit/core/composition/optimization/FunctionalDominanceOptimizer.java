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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.network.HashServiceMatchNetwork;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class FunctionalDominanceOptimizer<E, T extends Comparable<T>> implements NetworkOptimizer<E, T> {
    private static final Logger log = LoggerFactory.getLogger(FunctionalDominanceOptimizer.class);


    public Collection<Collection<Operation<E>>> functionalInputEquivalence(ServiceMatchNetwork<E, T> network, int level){
        // Identify providers for each service (all those services that provides one or more
        // inputs to the current service.
        Multimap<Set<Set<Operation<E>>>, Operation<E>> equivalentGroups = HashMultimap.create();
        for(Operation<E> o : network.getOperationsAtLevel(level)){
            // Calculate service providers for each input of this service.
            Set<Set<Operation<E>>> groupedInputProviders = new HashSet<Set<Operation<E>>>();
            for(E input : o.getSignature().getInputs()){
                // For each input, find compatible source elements that match that input
                Set<E> targets = network.getSourceElementsThatMatch(input).keySet();
                Set<Operation<E>> inputProviders = new HashSet<Operation<E>>();
                for(E target : targets){
                    inputProviders.addAll(Sets.newHashSet(network.getOperationsWithOutput(target)));
                }
                // Remove services that are not in the previous layers
                inputProviders.retainAll(network.getOperationsBeforeLevel(level));
                groupedInputProviders.add(inputProviders);
            }
            equivalentGroups.get(groupedInputProviders).add(o);
        }
        return equivalentGroups.asMap().values();
    }

    public Collection<Collection<Operation<E>>> functionalOutputDominance(Collection<Operation<E>> operations, ServiceMatchNetwork<E, T> network, int level){
        Multimap<Set<E>, Operation<E>> providers = HashMultimap.create();
        Set<Operation<E>> consumers = new HashSet<Operation<E>>();
        for(Operation<E> o : operations){
            // Check operations that consumes the outputs of this op.
            for(E output : o.getSignature().getOutputs()){
                for(E target : network.getTargetElementsMatchedBy(output).keySet()){
                    consumers.addAll(Sets.newHashSet(network.getOperationsWithInput(target)));
                }
            }
            // Remove all the consumers that are not in the subsequent layers
            consumers.retainAll(network.getOperationsAfterLevel(level));
            // Compute all the inputs matched by operation o
            Set<E> inputsMatched = new HashSet<E>();
            for(Operation<E> consumer : consumers){
                inputsMatched.addAll(network.partialMatch(o.getSignature().getOutputs(), consumer.getSignature().getInputs()).getTargetElements());
            }
            providers.get(inputsMatched).add(o);
        }
        // Now, build a new multi map moving all dominated elements
        Multimap<Set<E>, Operation<E>> nonDominatedProviders = HashMultimap.create();

        // Inefficient subset elimination algorithm O(n^2)
        for(Set<E> m1 : providers.keys()){
            // Check whether m1 is dominated or not
            boolean dominated = false;
            for(Set<E> m2 : providers.keys()){
                if (m2.size() > m1.size()){
                    // m1 could be dominated by m2
                    if (m2.containsAll(m1)){
                        // m2 dominates m1
                        dominated = true;
                        break;
                    }
                }
            }
            if (!dominated){
                // Copy
                nonDominatedProviders.get(m1).addAll(providers.get(m1));
            }
        }
        return nonDominatedProviders.asMap().values();
    }

    public ServiceMatchNetwork<E, T> optimize(ServiceMatchNetwork<E, T> network){
        // Analyze functional dominance between services. This optimization
        // identifies all dominant services using the semantic inputs and outputs
        // and the existing matches between the concepts in the graph.
        Stopwatch globalWatch = Stopwatch.createStarted();
        Stopwatch localWatch = Stopwatch.createUnstarted();
        List<Set<Operation<E>>> optimized = new ArrayList<Set<Operation<E>>>(network.numberOfLevels());
        log.debug("Starting functional dominance optimization...");
        for(int i=0; i < network.numberOfLevels(); i++){
            // Analyze input dominance
            log.debug(" > Analyzing functional dominance on {} (network level {})", network.getOperationsAtLevel(i), i);
            localWatch.start();
            Collection<Collection<Operation<E>>> groups = functionalInputEquivalence(network, i);
            localWatch.stop();
            log.debug("\t\tInput equivalence groups: {} (computed in {})", groups, localWatch.toString());
            localWatch.reset();
            // For each equivalent group in this level, check the output dominance
            Set<Operation<E>> nonDominatedServices = new HashSet<Operation<E>>();
            for(Collection<Operation<E>> group : groups){
                log.debug("\t\tAnalyzing output dominance for group {}", group);
                localWatch.start();
                Collection<Collection<Operation<E>>> nonDominatedGroups = functionalOutputDominance(group, network, i);
                localWatch.stop();
                log.debug("\t\t\t+ Non-dominated groups detected: {} (computed in {})", nonDominatedGroups, localWatch.toString());
                log.debug("\t\t\t+ Size before / after output dominance {}/{}", group.size(), nonDominatedGroups.size());
                // Pick one non dominated service for each group randomly.
                for(Collection<Operation<E>> ndGroup : nonDominatedGroups){
                    Operation<E> representant = ndGroup.iterator().next();
                    log.debug("\t\t\t\t- {} has been selected as the representative service of the group {}", representant, ndGroup);
                    nonDominatedServices.add(representant);
                }
            }
            optimized.add(nonDominatedServices);
        }
        localWatch.reset().start();
        HashServiceMatchNetwork<E, T> optimizedNetwork = new HashServiceMatchNetwork<E, T>(new HashLeveledServices<E>(optimized), network);
        localWatch.stop();
        log.debug(" > Functional optimized match network computed in {}", localWatch.toString());
        log.info("Functional Dominance Optimization done in {}. Size before/after {}/{}.", globalWatch.stop().toString(),  network.listOperations().size(), optimizedNetwork.listOperations().size());
        return optimizedNetwork;
    }
}
