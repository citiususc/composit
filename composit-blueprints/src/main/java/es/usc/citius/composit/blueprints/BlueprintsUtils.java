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

package es.usc.citius.composit.blueprints;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import java.util.Collection;
import java.util.Map;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class BlueprintsUtils {

    public static <E> Graph mapToGraph(Map<E, Collection<E>> map){
        Graph graph = new TinkerGraph();
        for(E source : map.keySet()){
            // Add vertex
            Vertex sourceVertex = graph.getVertex(source);
            if (sourceVertex == null){
                sourceVertex = graph.addVertex(source);
            }
            for(E target : map.get(source)){
                Vertex targetVertex = graph.getVertex(target);
                if (targetVertex == null){
                    targetVertex = graph.addVertex(target);
                }
                graph.addEdge(null, sourceVertex, targetVertex, source.toString() + "->" + target.toString());
            }
        }
        return graph;
    }
}
