package es.usc.citius.composit.core.composition.optimization;


import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;

public interface NetworkOptimizer<E, T extends Comparable<T>> {
    ServiceMatchNetwork<E, T> optimize(ServiceMatchNetwork<E,T> network);
}
