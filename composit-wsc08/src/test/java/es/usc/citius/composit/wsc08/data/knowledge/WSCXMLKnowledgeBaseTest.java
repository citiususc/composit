package es.usc.citius.composit.wsc08.data.knowledge;

import com.google.common.base.Stopwatch;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCXMLKnowledgeBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(WSCXMLKnowledgeBaseTest.class);
    private static WSCXMLKnowledgeBase kb;

    @BeforeClass
    public static void setUp() throws IOException {
        Stopwatch w = Stopwatch.createStarted();
        kb = new WSCXMLKnowledgeBase(WSCTest.TESTSET_2008_01.openTaxonomyStream());
        logger.debug("WSC Knowledge base processed in {}", w.stop().toString());
    }

    @Test
    public void testTotalConcepts(){
        assertEquals(1540, this.kb.getConcepts().size());
    }

    @Test
    public void testTotalInstances(){
        assertEquals(3138, this.kb.getConcepts().size());
    }

    @Test
    public void testGetConcept(){
        assertNotNull(this.kb.getConcept("con2025477491"));
        assertNull(this.kb.getConcept("inst1426393654"));
    }

    @Test
    public void testGetInstance(){
        assertNotNull(this.kb.getInstance("inst1426393654"));
        assertNull(this.kb.getConcept("con2025477491"));
    }

    @Test
    public void testGetSubclasses() throws Exception {
        Set<? extends Concept> subclasses = kb.getSubclasses(kb.getConcept("con2025477491"));
    }

    @Test
    public void testGetSuperclasses() throws Exception {

    }

    @Test
    public void testEquivalent() throws Exception {

    }

    @Test
    public void testIsSubclass() throws Exception {

    }

    @Test
    public void testIsSuperclass() throws Exception {

    }

    @Test
    public void testResolveInstance() throws Exception {

    }

    @Test
    public void testGetConcepts() throws Exception {

    }

    @Test
    public void testGetInstances() throws Exception {

    }
}
