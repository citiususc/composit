package es.usc.citius.composit.wsc08.data.matcher;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.HierarchicalKnowledgeBase;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ExactPluginKnowledgeBaseMatchGraphTest {
    private static HierarchicalKnowledgeBase kb;
    private static ExactPluginKnowledgeBaseMatchGraph matchGraph;

    @BeforeClass
    public static void setUp() throws IOException {
        kb = WSCTest.TESTSET_2008_01.createKnowledgeBase();
        matchGraph = new ExactPluginKnowledgeBaseMatchGraph(kb);
    }


    @Test
    public void testGetTargetElementsMatchedBy() throws Exception {
        System.out.println(matchGraph.getTargetElementsMatchedBy(kb.getConcept("con864995873")));
    }

    @Test
    public void testGetSourceElementsThatMatch() throws Exception {
        System.out.println(matchGraph.getSourceElementsThatMatch(kb.getConcept("con864995873")));
    }

    @Test
    public void testPartialMatch() throws Exception {
        Set<Concept> source = Collections.singleton(kb.getConcept("con864995873"));
        Set<Concept> expected = new HashSet<Concept>();
        expected.add(kb.getConcept("con1428646343"));
        expected.add(kb.getConcept("con864995873"));
        expected.add(kb.getConcept("con241744282"));
        expected.add(kb.getConcept("con1988815758"));
        expected.add(kb.getConcept("con1627111892"));
        expected.add(kb.getConcept("con1226699739"));
        assertEquals(expected, matchGraph.partialMatch(source, expected).getTargetElements());
    }

    @Test
    public void testFullMatch() throws Exception {
        Set<Concept> source = Collections.singleton(kb.getConcept("con864995873"));
        Set<Concept> expected = new HashSet<Concept>();
        expected.add(kb.getConcept("con1428646343"));
        expected.add(kb.getConcept("con864995873"));
        expected.add(kb.getConcept("con241744282"));
        expected.add(kb.getConcept("con1988815758"));
        expected.add(kb.getConcept("con1627111892"));
        expected.add(kb.getConcept("con1226699739"));
        assertEquals(expected, matchGraph.partialMatch(source, expected).getTargetElements());
    }

    @Test
    public void testMatch() throws Exception {
        assertTrue(matchGraph.match(kb.getConcept("con864995873"), kb.getConcept("con1627111892")));
    }
}
