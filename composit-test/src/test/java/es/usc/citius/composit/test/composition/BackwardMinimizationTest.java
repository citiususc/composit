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

import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.Verifier;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BackwardMinimizationTest {
    private static final Logger log = LoggerFactory.getLogger(BackwardMinimizationTest.class);

    public void test(WSCTest test) throws IOException {
        WSCTest.Dataset dataset = test.dataset();
        ServiceMatchNetwork<Concept, Boolean> network = ForwardServiceDiscovererWSC.generateGraph(dataset);
        BackwardMinimizationOptimizer<Concept, Boolean> optimizer = new BackwardMinimizationOptimizer<Concept, Boolean>();
        Stopwatch stopwatch = Stopwatch.createStarted();
        ServiceMatchNetwork<Concept, Boolean> optimizedNetwork = optimizer.optimize(network);
        stopwatch.stop();
        // Check sizes before/after optimization
        float before = network.listOperations().size();
        float after = optimizedNetwork.listOperations().size();
        // % Reduction
        float reduction = (1f - after/before)*100f;
        log.debug("Backward minimization reduction achieved: {} % in {}", reduction, stopwatch.toString());
        // Verify if the result is correct
        assertTrue(Verifier.isExecutable(optimizedNetwork, dataset.getMatchGraph()));
        assertTrue(after < before);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC01() throws Exception {
        test(WSCTest.TESTSET_2008_01);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC02() throws Exception {
        test(WSCTest.TESTSET_2008_02);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC03() throws Exception {
        test(WSCTest.TESTSET_2008_03);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC04() throws Exception {
        test(WSCTest.TESTSET_2008_04);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC05() throws Exception {
        test(WSCTest.TESTSET_2008_05);
    }

    @Test
    public void testBackwardOptimizationWSC06() throws Exception {
        test(WSCTest.TESTSET_2008_06);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC07() throws Exception {
        test(WSCTest.TESTSET_2008_07);
    }

    @Test
    @Ignore
    public void testBackwardOptimizationWSC08() throws Exception {
        test(WSCTest.TESTSET_2008_08);
    }

}
