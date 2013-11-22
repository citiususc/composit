package es.usc.citius.composit.test.composition;

import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.composition.MatchBasedDiscoveryIO;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.provider.IndexedServiceProvider;
import es.usc.citius.composit.core.provider.ServiceDataProvider;
import es.usc.citius.composit.wsc08.data.WSCTest;
import es.usc.citius.composit.wsc08.data.matcher.ExactPluginKnowledgeBaseMatchGraph;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ForwardServiceDiscovererWSC01 {
    private ForwardServiceDiscoverer<Concept> forwardSearch;
    private WSCTest.Dataset dataset;

    @Before
    public void setUp() throws Exception {
        dataset = WSCTest.TESTSET_2008_01.dataset();
        ServiceDataProvider<Concept> provider = new IndexedServiceProvider<Concept>(dataset.getServiceProvider());
        // Create a I/O Discovery and the match graph
        ExactPluginKnowledgeBaseMatchGraph matchGraph = new ExactPluginKnowledgeBaseMatchGraph(dataset.getKb());
        DiscoveryIO<Concept> discovery = new MatchBasedDiscoveryIO<Concept, Boolean>(matchGraph, provider);
        forwardSearch = new ForwardServiceDiscoverer<Concept>(discovery, matchGraph);
    }

    @Test
    public void testWSC01() throws Exception {
        forwardSearch.search(dataset.getRequest());
    }
}
