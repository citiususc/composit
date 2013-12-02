package es.usc.citius.composit.test.blueprints;

import com.tinkerpop.blueprints.Graph;
import es.usc.citius.composit.blueprints.NetworkGraphMaker;
import es.usc.citius.composit.core.composition.network.ServiceMatchNetwork;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.test.composition.ForwardServiceDiscovererWSC;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class MatchNetworkGraphBuildTest {

    @Test
    public void sanityExceptionTest() throws IOException {
        // Check if the graph can be generated without throwing any exception
        ServiceMatchNetwork<Concept, Boolean> network = ForwardServiceDiscovererWSC.generateGraph(WSCTest.TESTSET_2008_06.dataset());
        Graph g = NetworkGraphMaker.create(network);
        //GraphMLWriter.outputGraph(g, new FileOutputStream(new File("graph.graphml")));
    }
}
