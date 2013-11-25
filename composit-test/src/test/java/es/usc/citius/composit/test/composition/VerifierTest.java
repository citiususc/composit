package es.usc.citius.composit.test.composition;

import es.usc.citius.composit.core.composition.HashLeveledServices;
import es.usc.citius.composit.core.composition.Verifier;
import es.usc.citius.composit.core.composition.network.HashServiceMatchNetwork;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        ServiceMatchNetwork<Concept, Boolean> invalid = new HashServiceMatchNetwork<Concept, Boolean>(new HashLeveledServices<Concept>(layers),  network);
        assertFalse(Verifier.satisfies(invalid, dataset.getDefaultMatcher()));
    }
}
