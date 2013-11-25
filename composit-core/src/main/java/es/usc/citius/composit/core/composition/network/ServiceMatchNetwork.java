package es.usc.citius.composit.core.composition.network;


import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;

public interface ServiceMatchNetwork<E,T extends Comparable<T>> extends LeveledServices<E>, MatchGraph<E,T> {
    // TODO; Add methods to retrieve source/target operation match
}
