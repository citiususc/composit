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
        assertTrue(Verifier.satisfies(network3, network1));
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
