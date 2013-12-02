package es.usc.citius.composit.blueprints;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class TinkerGraphSNFactory<E, T extends Comparable<T>> implements NetworkToGraphFactory<E,T> {

    public static <E, T extends Comparable<T>> Graph create(ServiceMatchNetwork<E,T> network){
        Map<Operation<E>, Vertex> vertexOps = new HashMap<Operation<E>, Vertex>();

        Graph graph = new TinkerGraph();
        for(Operation<E> op : network.getOperations()){
            // Add the operation as a graph vertex
            Vertex opv = graph.addVertex(op);
            opv.setProperty("name", op.getID());
            if (op.getServiceOwner()!=null){
                opv.setProperty("service", op.getServiceOwner().getID());
            }
            opv.setProperty("type", "operation");

            vertexOps.put(op,opv);
            // Add inputs and outputs if not added before

        }

        for(Operation<E> source : vertexOps.keySet()){
            Vertex sourceVertex = vertexOps.get(source);
            Set<Operation<E>> targets = new HashSet<Operation<E>>();
            for(E output : source.getSignature().getOutputs()){
                targets.addAll(network.getTargetOperationsMatchedBy(output).keySet());
            }
            for(Operation<E> target : targets){
                Vertex targetVertex = vertexOps.get(target);
                if (targetVertex == null){
                    throw new RuntimeException(target.getID() + " does not exist in network. Unexpected error");
                }

                Edge edge = graph.addEdge(null, sourceVertex, targetVertex, "match");
                edge.setProperty("source", source);
                edge.setProperty("target", target);
            }
        }
        graph.shutdown();
        return graph;
    }

    @Override
    public Graph createGraph(ServiceMatchNetwork<E, T> network) {
        return create(network);
    }
}
