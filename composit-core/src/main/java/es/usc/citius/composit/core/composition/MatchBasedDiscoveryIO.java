package es.usc.citius.composit.core.composition;

import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.provider.ServiceDataProvider;

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
    private MatchGraph<E,T> matchGraph;
    private ServiceDataProvider<E> provider;

    public MatchBasedDiscoveryIO(MatchGraph<E, T> matchGraph, ServiceDataProvider<E> provider) {
        this.matchGraph = matchGraph;
        this.provider = provider;
    }

    @Override
    public Set<Operation<E>> discoverOperationsForInput(E input) {
        Map<E,T> targets = matchGraph.getTargetElementsMatchedBy(input);
        // For each target, get the services that uses the input
        Set<Operation<E>> relevantOps = new HashSet<Operation<E>>();
        for(E target : targets.keySet()){
            Iterable<Operation<E>> result = provider.getOperationsWithInput(target);
            if (result != null){
                relevantOps.addAll(Sets.newHashSet(provider.getOperationsWithInput(target)));
            }
        }
        return relevantOps;
    }

    @Override
    public Set<Operation<E>> discoverOperationsForOutput(E output) {
        Map<E,T> sources = matchGraph.getSourceElementsThatMatch(output);
        // For each target, get the services that uses the input
        Set<Operation<E>> relevantOps = new HashSet<Operation<E>>();
        for(E source : sources.keySet()){
            Iterable<Operation<E>> result = provider.getOperationsWithOutput(source);
            if (result != null){
                relevantOps.addAll(Sets.newHashSet(result));
            }
        }
        return relevantOps;
    }
}
