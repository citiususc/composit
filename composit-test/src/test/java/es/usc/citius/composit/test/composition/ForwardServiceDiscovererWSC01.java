package es.usc.citius.composit.test.composition;

import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.composition.DiscoveryIO;
import es.usc.citius.composit.core.composition.MatchBasedDiscoveryIO;
import es.usc.citius.composit.core.composition.search.ForwardServiceDiscoverer;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.provider.MemoryIndexServiceProvider;
import es.usc.citius.composit.core.provider.ServiceProvider;
import es.usc.citius.composit.wsc08.data.WSCTest;
import es.usc.citius.composit.wsc08.data.matcher.WSCKBMatchGraph;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ForwardServiceDiscovererWSC01 {
    private static final Logger log = LoggerFactory.getLogger(ForwardServiceDiscovererWSC01.class);

    private void run(WSCTest test) throws IOException {
        Stopwatch w = Stopwatch.createStarted();
        WSCTest.Dataset dataset = test.dataset();
        ServiceProvider<Concept> provider = new MemoryIndexServiceProvider<Concept>(dataset.getServiceProvider());
        // Create a I/O Discovery and the match graph
        WSCKBMatchGraph matchGraph = new WSCKBMatchGraph(dataset.getKb());
        DiscoveryIO<Concept> discovery = new MatchBasedDiscoveryIO<Concept, Boolean>(matchGraph, provider);
        new ForwardServiceDiscoverer<Concept, Boolean>(discovery, matchGraph).search(dataset.getRequest());
        log.debug("Test time {}", w.stop().toString());
    }

    @Test
    public void testWSC01() throws Exception {
        run(WSCTest.TESTSET_2008_01);
    }

    @Test
    public void testWSC02() throws Exception {
        run(WSCTest.TESTSET_2008_02);
    }

    @Test
    public void testWSC03() throws Exception {
        run(WSCTest.TESTSET_2008_03);
    }

    @Test
    public void testWSC04() throws Exception {
        run(WSCTest.TESTSET_2008_04);
    }

    @Test
    public void testWSC05() throws Exception {
        run(WSCTest.TESTSET_2008_05);
    }

    @Test
    public void testWSC06() throws Exception {
        run(WSCTest.TESTSET_2008_06);
    }

    @Test
    public void testWSC07() throws Exception {
        run(WSCTest.TESTSET_2008_07);
    }

    @Test
    public void testWSC08() throws Exception {
        run(WSCTest.TESTSET_2008_08);
    }
}
