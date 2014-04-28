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

package es.usc.citius.composit.core.composition.search;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.InputDiscoverer;
import es.usc.citius.composit.core.composition.network.DirectedAcyclicSMN;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.NetworkOptimizer;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class ComposIT<E, T extends Comparable<T>> {
    private static final Logger log = LoggerFactory.getLogger(ComposIT.class);

    // Optimizations will be applied in order
    private List<NetworkOptimizer<E,T>> optimizations = new LinkedList<NetworkOptimizer<E, T>>();
    private CompositionProblem<E,T> problem;
    private MatchGraph<E, T> matchGraph;
    private InputDiscoverer<E> discoveryIO;
    private ForwardServiceDiscoverer<E, T> discoverer;

    // Use a builder?
    public ComposIT(CompositionProblem<E,T> compositionProblem){
        this.problem = compositionProblem;
        this.matchGraph = problem.getMatchGraph();
        this.discoveryIO = problem.getInputDiscoverer();
        this.discoverer = new ForwardServiceDiscoverer<E, T>(discoveryIO, matchGraph);
    }

    public ComposIT addOptimization(NetworkOptimizer<E,T> opt){
        this.optimizations.add(opt);
        return this;
    }

    public ServiceMatchNetwork<E, T> generateNetwork(Signature<E> request){
        return discoverer.search(request);
    }

    public ServiceMatchNetwork<E, T> searchComposition(Signature<E> request){
        // Build the initial match graph network (first pass)
        ServiceMatchNetwork<E, T> network = discoverer.search(request);
        // Apply optimizations
        for(NetworkOptimizer<E,T> opt : optimizations){
            network = opt.optimize(network);
        }
        Algorithms.Search<State<E>,HeuristicNode<State<E>,Double>>.Result searchResult = CompositSearch.create(network).search();
        // Take the service operations from the optimal state path. We reverse the list because
        // the search was done backwards.
        List<State<E>> states = Lists.reverse(searchResult.getOptimalPath());
        List<Set<Operation<E>>> optimalServiceOps = Lists.transform(states, new Function<State<E>, Set<Operation<E>>>() {
            @Override
            public Set<Operation<E>> apply(State<E> state) {
                return state.getStateOperations();
            }
        });
        // Generate a new ServiceMatchNetwork composition with the optimal services.
        // To build the composition, we use the information of the previous network to build the match relations
        // between services among the optimal services.
        ServiceMatchNetwork<E, T> composition = new DirectedAcyclicSMN<E, T>(new HashLeveledServices<E>(optimalServiceOps), network);
        return composition;
    }

    public Algorithms.Search<State<E>,HeuristicNode<State<E>,Double>>.Result search(Signature<E> request){
        // Create the 3-pass service match network.
        log.info("Initializing composition search problem...");
        // Composition starts with the request:
        Stopwatch compositionWatch = Stopwatch.createStarted();
        // Build the initial match graph network (first pass)
        ServiceMatchNetwork<E, T> network = discoverer.search(request);
        // Apply optimizations
        Stopwatch optWatch = Stopwatch.createStarted();
        for(NetworkOptimizer<E,T> opt : optimizations){
            network = opt.optimize(network);
        }
        optWatch.stop();
        log.info("Graph optimizations done in {}", optWatch);
        log.info("Starting search over a network with {} levels and {} operations", network.numberOfLevels(), network.listOperations().size());
        // Run search over network
        Algorithms.Search<State<E>,HeuristicNode<State<E>,Double>>.Result searchResult = CompositSearch.create(network).search();
        log.info("Optimal composition search finished in {}", searchResult.getStopwatch().toString());
        log.debug("   Composition         : {}", searchResult.getOptimalPath());
        log.debug("   Total iterations    : {}", searchResult.getIterations());
        log.info("   Composition runpath : {}", searchResult.getOptimalPath().size()-2);
        log.info("   Composition services: {}", searchResult.getGoalNode().getScore());
        log.info("Total composition time : {}", compositionWatch.stop().toString());

        List<State<E>> states = searchResult.getOptimalPath();
        List<Set<Operation<E>>> ops = Lists.transform(states, new Function<State<E>, Set<Operation<E>>>() {
            @Override
            public Set<Operation<E>> apply(State<E> state) {
                return state.getStateOperations();
            }
        });
        return searchResult;
    }
}
