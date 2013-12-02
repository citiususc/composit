package es.usc.citius.composit.blueprints;

import com.tinkerpop.blueprints.Graph;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface NetworkToGraphFactory<E, T extends Comparable<T>> {
    Graph createGraph(ServiceMatchNetwork<E, T> network);
}
