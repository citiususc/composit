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

import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class ComposIT {
    private static final Logger log = LoggerFactory.getLogger(ComposIT.class);

    private ComposIT(){}

    public static <E, T extends Comparable<T>> void search(CompositionProblem<E,T> problem, Signature<E> request){
        // Create the 3-pass service match network.
        log.info("Initializing composition search problem...");
        // Get the match graph
        MatchGraph<E, T> matchGraph = problem.getMatchGraph();
        // Get the Discovery I/O algorithm
        DiscoveryIO<E> discovery = problem.getDiscoveryIO();
        // Composition starts with the request:
        Stopwatch compositionWatch = Stopwatch.createStarted();
        // Build the initial match graph network (first pass)
        ServiceMatchNetwork<E, T> network1 = new ForwardServiceDiscoverer<E, T>(discovery, matchGraph).search(request);
        // Second pass optimization
        ServiceMatchNetwork<E, T> network2 = new BackwardMinimizationOptimizer<E, T>().optimize(network1);
        // Third pass, functional dominance
        ServiceMatchNetwork<E, T> network3 = new FunctionalDominanceOptimizer<E, T>().optimize(network2);
        log.info("Starting search over a network with {} levels and {} operations", network3.numberOfLevels(), network3.listOperations().size());
        // Run search over network3
        Algorithms.Search<State,HeuristicNode<State,Double>>.Result searchResult = CompositSearch.create(network3).search();
        log.info("Optimal composition search finished in {}: {}", searchResult.getStopwatch().toString(), searchResult.getOptimalPath());
        log.info("   Total iterations    : {}", searchResult.getIterations());
        log.info("   Composition runpath : {}", searchResult.getOptimalPath().size()-2);
        log.info("   Composition services: {}", searchResult.getGoalNode().getScore());
        log.info("Total composition time : {}", compositionWatch.stop().toString());
    }
}
