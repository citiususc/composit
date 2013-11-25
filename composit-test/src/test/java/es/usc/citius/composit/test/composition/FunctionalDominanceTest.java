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

import es.usc.citius.composit.core.composition.Verifier;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class FunctionalDominanceTest {

    public void test(WSCTest test, int expectedSize) throws Exception {
        WSCTest.Dataset dataset = test.dataset();
        // Initial network, first pass
        ServiceMatchNetwork<Concept, Boolean> network1 = ForwardServiceDiscovererWSC.generateGraph(dataset);
        // Second pass optimization
        ServiceMatchNetwork<Concept, Boolean> network2 = new BackwardMinimizationOptimizer<Concept, Boolean>().optimize(network1);
        // Third pass, functional dominance
        ServiceMatchNetwork<Concept, Boolean> network3 = new FunctionalDominanceOptimizer<Concept, Boolean>().optimize(network2);
        // Verify final result
        assertEquals(expectedSize, network3.listOperations().size());
        // Use always a different matcher network to validate the result
        assertTrue(Verifier.satisfies(network1, dataset.getMatchGraph()));
        // Use different networks as the setMatcherFunction. The dataset.getMatchGraph
        // can be used as well to verify the correctness of the network.
        assertTrue(Verifier.satisfies(network2, network1));
        assertTrue(Verifier.satisfies(network3, network2));
    }

    @Test
    public void testFunctionalDominanceWSC01() throws Exception {
        test(WSCTest.TESTSET_2008_01, 15);
    }

    @Test
    public void testFunctionalDominanceWSC02() throws Exception {
        test(WSCTest.TESTSET_2008_02, 15);
    }

    @Test
    public void testFunctionalDominanceWSC03() throws Exception {
        test(WSCTest.TESTSET_2008_03, 42);
    }

    @Test
    public void testFunctionalDominanceWSC04() throws Exception {
        test(WSCTest.TESTSET_2008_04, 27);
    }

    @Test
    public void testFunctionalDominanceWSC05() throws Exception {
        test(WSCTest.TESTSET_2008_05, 54);
    }

    @Test
    public void testFunctionalDominanceWSC06() throws Exception {
        test(WSCTest.TESTSET_2008_06, 77);
    }

    @Test
    public void testFunctionalDominanceWSC07() throws Exception {
        test(WSCTest.TESTSET_2008_07, 72);
    }

    @Test
    public void testFunctionalDominanceWSC08() throws Exception {
        test(WSCTest.TESTSET_2008_08, 60);
    }
}
