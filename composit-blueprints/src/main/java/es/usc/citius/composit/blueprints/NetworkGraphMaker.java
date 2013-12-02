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

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.model.Operation;

import java.util.HashMap;
import java.util.Map;

public class NetworkGraphMaker {

    public static <E, T extends Comparable<T>> Graph create(ServiceMatchNetwork<E,T> network){
        Map<Operation<E>, Vertex> vertexOps = new HashMap<Operation<E>, Vertex>();
        Map<E, Vertex> vertexElems = new HashMap<E, Vertex>();

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
            for(E e : op.getSignature().getInputs()){
                Vertex ev = vertexElems.get(e);
                if (ev == null){
                    ev = graph.addVertex(e);
                    vertexElems.put(e, ev);
                }
                ev.setProperty("type","io");
                ev.setProperty("name", e.toString());
                // Connect input with operation
                String edgeId = "input_" + e.toString() + "_" + op.getID();
                graph.addEdge(edgeId, ev, opv, "is_input_of");
            }
            for(E e : op.getSignature().getOutputs()){
                // Try to get the elem vertex from the map
                Vertex ev = vertexElems.get(e);
                if (ev == null){
                    ev = graph.addVertex(e);
                    vertexElems.put(e, ev);
                }
                ev.setProperty("type", "io");
                // Connect output with operation
                String edgeId = "output_" + e.toString() + "_" + op.getID();
                graph.addEdge(edgeId, opv, ev, "has_output");
            }
        }

        // Make connections between elements (inputs/outputs)
        for(E source : vertexElems.keySet()){
            for(Map.Entry<E,T> target : network.getTargetElementsMatchedBy(source).entrySet()){
                Vertex srcVertex = vertexElems.get(source);
                Vertex targetVertex = vertexElems.get(target.getKey());
                if (targetVertex == null){
                    throw new RuntimeException(source + " match " + target + " but target is not in the graph");
                }
                String key = source.toString() + "-[" + target.getValue() + "]->"+ target.getKey();
                Edge matchEdge = graph.addEdge(key, srcVertex, targetVertex, "match");
                matchEdge.setProperty("match_type", target.getValue());
                matchEdge.setProperty("source", source);
                matchEdge.setProperty("target", target.getKey());
            }
        }
        graph.shutdown();
        return graph;
    }
}
