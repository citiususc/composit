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

package es.usc.citius.composit.wsc08.data;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.core.knowledge.Instance;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;
import es.usc.citius.composit.core.model.impl.ResourceOperation;
import es.usc.citius.composit.core.model.impl.ResourceService;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.core.provider.ServiceProvider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCLazyServiceProvider implements ServiceProvider<Concept> {
    private HierarchicalKnowledgeBase kb;
    private ServiceProvider<String> delegatedProvider;

    public WSCLazyServiceProvider(ServiceProvider<String> delegatedProvider, HierarchicalKnowledgeBase kb) {
        this.delegatedProvider = delegatedProvider;
        this.kb = kb;
    }



    @Override
    public Service<Concept> getService(String serviceID) {
        Service<String> service = delegatedProvider.getService(serviceID);
        // Translate
        Set<ResourceOperation<Concept>> operations = new HashSet<ResourceOperation<Concept>>();
        for(Operation<String> op : service.getOperations()){
            operations.add(translate(op));
        }
        return new ResourceService<Concept>(serviceID, operations);
    }

    private ResourceOperation<Concept> translate(Operation<String> op){
        // Translate inputs / outputs to concepts.
        Set<Concept> inputs = new HashSet<Concept>();
        Set<Concept> outputs = new HashSet<Concept>();
        for(String input : op.getSignature().getInputs()){
            inputs.add(kb.resolveInstance(kb.getInstance(input)));
        }
        for(String output : op.getSignature().getOutputs()){
            outputs.add(kb.resolveInstance(kb.getInstance(output)));
        }
        return new ResourceOperation<Concept>(op.getID(), new SignatureIO<Concept>(inputs, outputs));
    }

    private Service<Concept> translate(Service<String> service){
        Set<ResourceOperation<Concept>> ops = new HashSet<ResourceOperation<Concept>>();
        for(Operation<String> operation : service.getOperations()){
            ops.add(translate(operation));
        }
        return new ResourceService<Concept>(service.getID(), ops);
    }

    @Override
    public Operation<Concept> getOperation(String operationID) {
        Operation<String> op = delegatedProvider.getOperation(operationID);
        return translate(op);
    }

    @Override
    public Iterable<Operation<Concept>> getOperationsWithInput(final Concept input) {
        final Set<Instance> instances = kb.getInstances(input);
        return new Iterable<Operation<Concept>>() {
            @Override
            public Iterator<Operation<Concept>> iterator() {
                Iterator<Operation<String>> iteratorResult = Iterators.emptyIterator();
                for(Instance instance : instances){
                    iteratorResult = Iterators.concat(iteratorResult, delegatedProvider.getOperationsWithInput(instance.getID()).iterator());
                }
                return Iterators.transform(iteratorResult,
                        new Function<Operation<String>, Operation<Concept>>() {
                            @Override
                            public Operation<Concept> apply(Operation<String> op) {
                                return translate(op);
                            }
                        });
            }
        };

    }

    @Override
    public Iterable<Operation<Concept>> getOperationsWithOutput(final Concept output) {
        final Set<Instance> instances = kb.getInstances(output);
        return new Iterable<Operation<Concept>>() {
            @Override
            public Iterator<Operation<Concept>> iterator() {
                Iterator<Operation<String>> iteratorResult = Iterators.emptyIterator();
                for(Instance instance : instances){
                    iteratorResult = Iterators.concat(iteratorResult, delegatedProvider.getOperationsWithOutput(instance.getID()).iterator());
                }
                return Iterators.transform(iteratorResult,
                        new Function<Operation<String>, Operation<Concept>>() {
                            @Override
                            public Operation<Concept> apply(Operation<String> op) {
                                return translate(op);
                            }
                        });
            }
        };
    }

    @Override
    public Iterable<Operation<Concept>> getOperations() {
        // Returns an iterable of transformed operations.
        // Operations are transformed dynamically and not beforehand.
        return new Iterable<Operation<Concept>>() {
            @Override
            public Iterator<Operation<Concept>> iterator() {
                return Iterators.transform(delegatedProvider.getOperations().iterator(), new Function<Operation<String>, Operation<Concept>>() {
                    @Override
                    public Operation<Concept> apply(Operation<String> op) {
                        return translate(op);
                    }
                });
            }
        };
    }

    @Override
    public Iterable<Service<Concept>> getServices() {
        return new Iterable<Service<Concept>>() {
            @Override
            public Iterator<Service<Concept>> iterator() {
                return Iterators.transform(delegatedProvider.getServices().iterator(), new Function<Service<String>, Service<Concept>>() {
                    @Override
                    public Service<Concept> apply(Service<String> srv) {
                        return translate(srv);
                    }
                });
            }
        };
    }

    @Override
    public Set<String> listOperations() {
        return delegatedProvider.listOperations();
    }

    @Override
    public Set<String> listServices() {
        return delegatedProvider.listServices();
    }
}
