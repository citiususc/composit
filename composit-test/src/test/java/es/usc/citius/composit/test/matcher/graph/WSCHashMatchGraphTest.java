package es.usc.citius.composit.test.matcher.graph;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.graph.HashMatchGraph;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCHashMatchGraphTest {

    private WSCTest.Dataset dataset;

    @Before
    public void setUp() throws Exception {
        dataset = WSCTest.TESTSET_2008_01.dataset();
    }

    @Test
    public void testMatchWSC(){
        HashMatchGraph<Concept, LogicMatchType> matchGraph =
                new HashMatchGraph<Concept, LogicMatchType>(dataset.getDefaultMatcher(), dataset.getKb().getConcepts());

        Set<Concept> targets = new HashSet<Concept>();
        // WSC 01 request inputs
        Set<String> inputs = dataset.getRequest().getInputs();
        for(String input : inputs){
            Concept c = dataset.getKb().getConcept(input);
            System.out.println(matchGraph.getTargetElementsMatchedBy(c));
        }
    }
}
