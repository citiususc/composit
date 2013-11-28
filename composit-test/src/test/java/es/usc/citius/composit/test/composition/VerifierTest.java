/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
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

package es.usc.citius.composit.test.composition;

import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.Verifier;
import es.usc.citius.composit.core.composition.network.DirectedAcyclicSMN;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class VerifierTest {

    @Test
    public void testVerifier() throws Exception {
        WSCTest.Dataset dataset = WSCTest.TESTSET_2008_01.dataset();
        ServiceMatchNetwork<Concept, Boolean> network = ForwardServiceDiscovererWSC.generateGraph(dataset);
        assertTrue(Verifier.satisfies(network, dataset.getDefaultMatcher()));
        // Now remove a required service
        List<Set<Operation<Concept>>> layers = new ArrayList<Set<Operation<Concept>>>(network.getLeveledList());
        Set<Operation<Concept>> layer = layers.get(layers.size()-2);
        boolean removed = false;
        String operation = "serv699915007Operation";
        for(Iterator<Operation<Concept>> it = layer.iterator(); it.hasNext();){
            Operation<Concept> current = it.next();
            if (current.getID().equals(operation)){
                removed = true;
                it.remove();
                break;
            }
        }
        System.out.println(layers);
        if (!removed) fail(operation + " not found in network");
        ServiceMatchNetwork<Concept, Boolean> invalid = new DirectedAcyclicSMN<Concept, Boolean>(new HashLeveledServices<Concept>(layers),  network);
        assertFalse(Verifier.satisfies(invalid, dataset.getDefaultMatcher()));
    }
}
