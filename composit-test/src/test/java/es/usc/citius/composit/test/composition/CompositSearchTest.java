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

import com.google.common.collect.Lists;
import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.LeveledServices;
import es.usc.citius.composit.core.composition.Verifier;
import es.usc.citius.composit.core.composition.optimization.BackwardMinimizationOptimizer;
import es.usc.citius.composit.core.composition.optimization.FunctionalDominanceOptimizer;
import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.core.composition.search.State;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.impl.DummyOperation;
import es.usc.citius.composit.wsc08.data.WSCTest;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositSearchTest {
    private static final int timeout=10000;

    private Algorithms.Search<State<Concept>,HeuristicNode<State<Concept>,Double>>.Result run(WSCTest test) throws IOException {
        WSCTest.Dataset dataset = test.dataset();
        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());
        return composit.search(dataset.getRequest());
    }

    private void verify(WSCTest test, int runpath, int services) throws IOException {
        WSCTest.Dataset dataset = test.dataset();
        ComposIT<Concept, Boolean> composit = new ComposIT<Concept, Boolean>(dataset.getDefaultCompositionProblem());
        composit.addOptimization(new BackwardMinimizationOptimizer<Concept, Boolean>());
        composit.addOptimization(new FunctionalDominanceOptimizer<Concept, Boolean>());
        Algorithms.Search<State<Concept>, HeuristicNode<State<Concept>, Double>>.Result result
                = composit.search(dataset.getRequest());

        List<Set<Operation<Concept>>> composition = new ArrayList<Set<Operation<Concept>>>();
        int level=0;
        for(State<Concept> state : Lists.reverse(result.getOptimalPath())){
            Set<Operation<Concept>> ops = new HashSet<Operation<Concept>>();
            for(Operation<Concept> op : state.getStateOperations()){
                if (level==0 || level==result.getOptimalPath().size()-1){
                    ops.add(op);
                } else {
                    if (!(ops instanceof DummyOperation)){
                        ops.add(op);
                    }
                }
            }
            if (!ops.isEmpty()){
                composition.add(state.getStateOperations());
            }
            level++;
        }
        LeveledServices<Concept> cmp = new HashLeveledServices<Concept>(composition);
        assertTrue(Verifier.isExecutable(cmp, dataset.getMatchGraph()));
        assertTrue(Verifier.isExecutable(cmp, dataset.getDefaultMatcher()));
        assertEquals(runpath, result.getOptimalPath().size()-2);
        assertEquals(services, result.getGoalNode().getScore().intValue());
    }

    @Test(timeout=timeout)
    public void testComposition01() throws Exception {
        verify(WSCTest.TESTSET_2008_01, 3, 10);
    }

    @Test(timeout=timeout)
    public void testComposition02() throws Exception {
        verify(WSCTest.TESTSET_2008_02, 3, 5);
    }

    @Test(timeout=timeout)
    public void testComposition03() throws Exception {
        verify(WSCTest.TESTSET_2008_03, 23, 40);
    }

    @Test(timeout=timeout)
    public void testComposition04() throws Exception {
        verify(WSCTest.TESTSET_2008_04, 5, 10);
    }

    @Test(timeout=timeout)
    public void testComposition05() throws Exception {
        verify(WSCTest.TESTSET_2008_05, 8, 20);
    }

    @Test(timeout=timeout)
    public void testComposition06() throws Exception {
        verify(WSCTest.TESTSET_2008_06, 7, 42);
    }

    @Test(timeout=timeout)
    public void testComposition07() throws Exception {
        verify(WSCTest.TESTSET_2008_07, 12, 20);
    }

    @Test(timeout=timeout)
    public void testComposition08() throws Exception {
        verify(WSCTest.TESTSET_2008_08, 20, 30);
    }
}
