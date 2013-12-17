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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MatchBasedInputDiscoverer<E> implements InputDiscoverer<E> {
    private MatchGraph<E,?> matchGraph;
    private ServiceProvider<E> provider;

    public MatchBasedInputDiscoverer(MatchGraph<E, ?> matchGraph, ServiceProvider<E> provider) {
        this.matchGraph = matchGraph;
        this.provider = provider;
    }

    @Override
    public Set<Operation<E>> findOperationsConsuming(E input) {
        Map<E,?> targets = matchGraph.getTargetElementsMatchedBy(input);
        // For each target, get the services that uses the input
        Set<Operation<E>> relevantOps = new HashSet<Operation<E>>();
        for(E target : targets.keySet()){
            Iterable<Operation<E>> result = provider.getOperationsWithInput(target);
            if (result != null){
                relevantOps.addAll(Sets.newHashSet(result));
            }
        }
        return relevantOps;
    }

    @Override
    public Set<Operation<E>> findOperationsConsumingSome(Collection<E> inputs) {
        Set<Operation<E>> candidates = new HashSet<Operation<E>>();
        for(E input : inputs){
            candidates.addAll(findOperationsConsuming(input));
        }
        return candidates;
    }
}
