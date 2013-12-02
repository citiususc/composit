package es.usc.citius.composit.blueprints;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import es.usc.citius.composit.core.matcher.graph.AbstractMatchGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BlueprintsBasedMatchGraph<E, T extends Comparable<T>> extends AbstractMatchGraph<E, T> {

    private Graph graph;

    public BlueprintsBasedMatchGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Set<E> getElements() {
        Set<E> elements = new HashSet<E>();
        for(Vertex v : graph.query().has("type", "io").vertices()){
            elements.add((E)v.getId());
        }
        return elements;
    }

    @Override
    public Map<E, T> getTargetElementsMatchedBy(E source) {
        Map<E, T> targets = new HashMap<E, T>();
        for(Edge out : graph.getVertex(source).getEdges(Direction.OUT)){
            targets.put((E)out.getProperty("target"), (T)out.getProperty("match_type"));
        }
        return targets;
    }

    @Override
    public Map<E, T> getSourceElementsThatMatch(E target) {
        Map<E, T> sources = new HashMap<E, T>();
        for(Edge in : graph.getVertex(target).getEdges(Direction.IN)){
            sources.put((E)in.getProperty("source"), (T)in.getProperty("match_type"));
        }
        return sources;
    }


}
