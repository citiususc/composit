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

package es.usc.citius.composit.core.composition;

import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.provider.ServiceProvider;
import org.javasimon.SimonManager;
import org.javasimon.Split;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic implementation of DiscoveryIO using a match graph and a service data provider to discover
 * relevant services based on their inputs outputs.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class MatchBasedDiscoveryIO<E, T extends Comparable<T>> implements DiscoveryIO<E> {
    public static final String STOPWATCH_INPUT_DISCOVER = MatchBasedDiscoveryIO.class.getName() + ".discoverOperationsForInput";
    public static final String STOPWATCH_OUTPUT_DISCOVER = MatchBasedDiscoveryIO.class.getName() + ".discoverOperationsForOutput";

    private MatchGraph<E,T> matchGraph;
    private ServiceProvider<E> provider;

    public MatchBasedDiscoveryIO(MatchGraph<E, T> matchGraph, ServiceProvider<E> provider) {
        this.matchGraph = matchGraph;
        this.provider = provider;
    }

    @Override
    public Set<Operation<E>> discoverOperationsForInput(E input) {
        Split split = SimonManager.getStopwatch(STOPWATCH_INPUT_DISCOVER).start();
        Map<E,T> targets = matchGraph.getTargetElementsMatchedBy(input);
        // For each target, get the services that uses the input
        Set<Operation<E>> relevantOps = new HashSet<Operation<E>>();
        for(E target : targets.keySet()){
            Iterable<Operation<E>> result = provider.getOperationsWithInput(target);
            if (result != null){
                relevantOps.addAll(Sets.newHashSet(result));
            }
        }
        split.stop();
        return relevantOps;
    }

    @Override
    public Set<Operation<E>> discoverOperationsForOutput(E output) {
        Split split = SimonManager.getStopwatch(STOPWATCH_OUTPUT_DISCOVER).start();
        Map<E,T> sources = matchGraph.getSourceElementsThatMatch(output);
        // For each target, get the services that uses the input
        Set<Operation<E>> relevantOps = new HashSet<Operation<E>>();
        for(E source : sources.keySet()){
            Iterable<Operation<E>> result = provider.getOperationsWithOutput(source);
            if (result != null){
                relevantOps.addAll(Sets.newHashSet(result));
            }
        }
        split.stop();
        return relevantOps;
    }
}
