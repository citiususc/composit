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

package es.usc.citius.composit.core.composition.search;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;
import es.usc.citius.composit.core.model.impl.DummyOperation;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.algorithm.combinatorial.SetCoverIterator;
import es.usc.citius.lab.hipster.algorithm.problem.DefaultSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class CompositSearch {
    private static final Logger log = LoggerFactory.getLogger(CompositSearch.class);

    private CompositSearch(){}

    private static <E, T extends Comparable<T>> SetMultimap<Operation<E>, E> calculateProviders(Set<E> inputs, int currentLayer, ServiceMatchNetwork<E,T> network){
        //Provider -> inputs
        SetMultimap<Operation<E>, E> map = HashMultimap.create();
        for(E input : inputs){
            Set<Operation<E>> providers = network.getSourceOperationsThatMatch(input).keySet();

            providers.retainAll(Sets.newHashSet(network.getOperations()));
            if (providers.isEmpty()){
                throw new RuntimeException("No candidates for input " + input + ". Unsolvable request");
            }
            for(Operation<E> provider : providers){
                // If provider is not in the immediately previous layer, it cannot be selected.
                // Replace it with a noInputOp
                if (network.levelOf(provider) < currentLayer-1){
                    DummyOperation noInputOp = new DummyOperation("no-"+input.toString(), new SignatureIO<E>(Collections.singleton(input), Collections.singleton(input)));
                    map.get(noInputOp).add(input);
                } else {
                    map.get(provider).add(input);
                }
            }
        }
        return map;
    }


    private static <E> Set<Set<Operation<E>>> combine(Multimap<Set<E>, Operation<E>> matchMap){
        // Get the groups
        Set<Set<E>> sets = matchMap.asMap().keySet();
        log.debug("\t\t> Performing set input cover of {}", sets);
        SetCoverIterator<E> sc = new SetCoverIterator<E>(sets);
        //sc.useParallelization(true);
        Set<Set<Operation<E>>> coveringSets = new HashSet<Set<Operation<E>>>();
        while(sc.hasNext()){
            Set<Set<E>> group = sc.next();
            log.debug("\t\t\t+ Selected cover input group: {}", group);
            // Get the services associated to each E group
            List<Set<Operation<E>>> solution = new ArrayList<Set<Operation<E>>>();
            for(Set<E> inputsMatched : group){
                Collection<Operation<E>> ops = matchMap.get(inputsMatched);
                solution.add(new HashSet<Operation<E>>(ops));
            }
            log.debug("\t\t\t\t- Operation groups: {}", solution);
            // Generate cartesian product to decompose equivalent functional services
            Set<List<Operation<E>>> cartesian = Sets.cartesianProduct(solution);;
            for(List<Operation<E>> cartesianResult : cartesian){
                coveringSets.add(new HashSet<Operation<E>>(cartesianResult));
            }
            log.debug("\t\t\t\t- Cartesian product decomposition: {}", cartesian);
        }
        log.debug("\t\t\t- Cover sets before cartesian product decomposition {}", coveringSets);
        return coveringSets;
    }

    private static <E> SetMultimap<Set<E>, Operation<E>> group(SetMultimap<Operation<E>, E> conceptMap){
        // Generate groups
        SetMultimap<Set<E>, Operation<E>> matchMap = HashMultimap.create();
        for(Operation<E> op : conceptMap.keys()){
            matchMap.get(conceptMap.get(op)).add(op);
        }
        return matchMap;
    }

    private static <E, T extends Comparable<T>> Set<Set<Operation<E>>> nodeEquivalence(Set<Set<Operation<E>>> successors, ServiceMatchNetwork<E, T> network){
        // Group candidates
        if (successors.size() < 2) return successors;
        Multimap<Set<Set<Operation<E>>>, Set<Operation<E>>> mmap = HashMultimap.create();
        for(Set<Operation<E>> group : successors){
            Set<E> inputs =Operations.inputs(group);
            // Find providers
            Set<Set<Operation<E>>> successorGroup = new HashSet<Set<Operation<E>>>();
            for(E input : inputs){
                for(E source : network.getSourceElementsThatMatch(input).keySet()){
                    successorGroup.add(Sets.newHashSet(network.getOperationsWithOutput(source)));
                }
            }
            mmap.get(successorGroup).add(group);
        }
        // Select one from each equivalent group
        Set<Set<Operation<E>>> nonEquivalentGroups = new HashSet<Set<Operation<E>>>();
        for(Collection<Set<Operation<E>>> group : mmap.asMap().values()){
            nonEquivalentGroups.add(group.iterator().next());
        }
        return nonEquivalentGroups;
    }

    public static <E, T extends Comparable<T>> Algorithms.Search<State<E>, HeuristicNode<State<E>, Double>> create(final ServiceMatchNetwork<E, T> network){

        TransitionFunction<State<E>> tf = new TransitionFunction<State<E>>() {
            @Override
            public Iterable<? extends Transition<State<E>>> from(State<E> current) {
                log.debug("> Computing successors of the state at level {}, with {} services: {}", current.level, current.countServices(), current.getStateOperations());
                // Get all required inputs (inputs from all services in this state)
                Set<E> inputs = Operations.inputs(current.getStateOperations());
                log.debug("\t- {} unsolved inputs in current state: {}", inputs.size(), inputs);
                SetMultimap<Operation<E>, E> providers = calculateProviders(inputs, current.level, network);
                log.debug("\t- {} relevant operations found for the unsolved inputs: (service => input) {}", providers.size(), providers);
                SetMultimap<Set<E>, Operation<E>> groups = group(providers);
                log.debug("\t- {} groups generated based on the inputs matched: {}", groups.size(), groups);
                Set<Set<Operation<E>>> result = combine(groups);
                log.debug("\t- {} optimal successor states found that isExecutable the unsolved inputs: {}", result.size(), result);
                // Generate new states.
                //result = nodeEquivalence(result, network);
                //log.debug("\t- {} non-equivalent unique successor after node equivalence: {}", result.size(), result);
                Set<State<E>> neighbors = new HashSet<State<E>>();
                for(Set<Operation<E>> combination : result){
                    // Create the new state
                    if (!combination.isEmpty()){
                        neighbors.add(new State<E>(combination, current.level-1));
                    }
                }
                return Transition.map(current, neighbors);
            }
        };
        // Define cost function
        CostFunction<State<E>, Double> cf = new CostFunction<State<E>, Double>() {
            @Override
            public Double evaluate(Transition<State<E>> transition) {
                return (double)transition.to().countServices();
            }
        };
        // Define heuristic function
        HeuristicFunction<State<E>, Double> hf = new HeuristicFunction<State<E>, Double>() {
            @Override
            public Double estimate(State<E> state) {
                // Get layer
                return (double)state.level;
            }
        };
        State<E> goal = new State<E>(Collections.singleton(network.getSource()), network.levelOf(network.getSource()));
        State<E> initial = new State<E>(Collections.singleton(network.getSink()), network.levelOf(network.getSink()));
        DefaultSearchProblem<State<E>> problem = new DefaultSearchProblem<State<E>>(initial, goal, tf, cf);
        problem.setHeuristicFunction(hf);
        return Algorithms.createAStar(problem);
    }
}
