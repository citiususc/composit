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

    @Test
    public void testFunctionalDominanceOptimization() throws Exception {
        WSCTest.Dataset dataset = WSCTest.TESTSET_2008_01.dataset();
        // Initial network, first pass
        ServiceMatchNetwork<Concept, Boolean> network1 = ForwardServiceDiscovererWSC.generateGraph(dataset);
        // Second pass optimization
        ServiceMatchNetwork<Concept, Boolean> network2 = new BackwardMinimizationOptimizer<Concept, Boolean>().optimize(network1);
        // Third pass, functional dominance
        ServiceMatchNetwork<Concept, Boolean> network3 = new FunctionalDominanceOptimizer<Concept, Boolean>().optimize(network2);
        // Verify final result
        assertEquals(15, network3.listOperations().size());
        assertTrue(Verifier.satisfies(network3, network1, dataset.getRequest()));
    }
}
