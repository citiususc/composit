/*
 * Copyright 2014 Centro de Investigación en Tecnoloxías da Información (CITIUS),
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

package es.usc.citius.composit.core.composition.search;


import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.InputDiscoverer;
import es.usc.citius.composit.core.composition.network.DirectedAcyclicSMN;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;
import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.composit.core.model.impl.Sink;
import es.usc.citius.composit.core.model.impl.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NaiveForwardServiceDiscoverer<E, T extends Comparable<T>> {
    private static final Logger log = LoggerFactory.getLogger(NaiveForwardServiceDiscoverer.class);
    private InputDiscoverer<E> discovery;
    private SetMatchFunction<E, T> matcher;

    public NaiveForwardServiceDiscoverer(InputDiscoverer<E> discovery, SetMatchFunction<E, T> matcher) {
        this.discovery = discovery;
        this.matcher = matcher;
    }

    public ServiceMatchNetwork<E,T> search(Signature<E> signature){
        Set<E> availableInputs = new HashSet<E>(signature.getInputs());
        Set<E> newOutputs = new HashSet<E>(signature.getInputs());
        Set<Operation<E>> usedServices = new HashSet<Operation<E>>();
        List<Set<Operation<E>>> leveledOps = new LinkedList<Set<Operation<E>>>();

        boolean checkExpectedOutputs = !signature.getOutputs().isEmpty();
        boolean stop;

        Stopwatch timer = Stopwatch.createStarted();
        Stopwatch levelTimer = Stopwatch.createUnstarted();
        int level=0;
        do {
            HashSet<Operation<E>> candidates = new HashSet<Operation<E>>();
            levelTimer.start();
            candidates.addAll(discovery.findOperationsConsumingSome(newOutputs));
            log.info("(Level {}) {} potential candidates selected in {}", level++, candidates.size(), levelTimer.toString());
            // Remove services that cannot be invoked with the available inputs
            for(Iterator<Operation<E>> it=candidates.iterator(); it.hasNext();){
                Operation<E> candidate = it.next();
                Set<E> matched = matcher.partialMatch(availableInputs, candidate.getSignature().getInputs()).getTargetElements();
                // Invokable?
                if (matched.equals(candidate.getSignature().getInputs())){
                    // Invokable operation, check if it was used previously
                    boolean isNew = usedServices.add(candidate);
                    if (!isNew) it.remove();
                } else {
                    it.remove();
                }
            }
            log.info("\t + [{}] operations selected for this level in {}: {}", candidates.size(), levelTimer.toString(), candidates);

            // Collect the new outputs of the new candidates
            newOutputs = Operations.outputs(candidates);
            availableInputs.addAll(newOutputs);
            Set<E> matchedOutputs = matcher.partialMatch(availableInputs, signature.getOutputs()).getTargetElements();

            // Add the discovered ops
            if (!candidates.isEmpty()) leveledOps.add(candidates);

            log.debug("\t + Available inputs: {}, new outputs: {}", availableInputs.size(), newOutputs.size());
            // Stop condition. Stop if there are no more candidates and/or expected outputs are satisfied.
            stop = (checkExpectedOutputs) ? candidates.isEmpty() || matchedOutputs.equals(signature.getOutputs()) : candidates.isEmpty();
            levelTimer.reset();
        } while(!stop);

        // Add the source and sink operations
        Source<E> sourceOp = new Source<E>(signature.getInputs());
        Sink<E> sinkOp = new Sink<E>(signature.getOutputs());
        leveledOps.add(0, Collections.<Operation<E>>singleton(sourceOp));
        leveledOps.add(leveledOps.size(), Collections.<Operation<E>>singleton(sinkOp));
        Stopwatch networkWatch = Stopwatch.createStarted();
        // Create a service match network with the discovered services
        DirectedAcyclicSMN<E,T> matchNetwork = new DirectedAcyclicSMN<E, T>(new HashLeveledServices<E>(leveledOps), this.matcher);
        log.info(" > Service match network computed in {}", networkWatch.stop().toString());
        log.info("Service Match Network created with {} levels (including source and sink) and {} operations.", leveledOps.size(), matchNetwork.listOperations().size());
        log.info("Forward Discovery done in {}", timer.toString());
        return matchNetwork;
    }
}
