package es.usc.citius.composit.wsc08.data.knowledge;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.knowledge.Instance;
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
        assertEquals(3138, this.kb.getInstances().size());
    }

    @Test
    public void testGetConcept(){
        assertNotNull(this.kb.getConcept("con2025477491"));
        assertNull(this.kb.getConcept("inst1426393654"));
    }

    @Test
    public void testGetInstance(){
        assertNotNull(this.kb.getInstance("inst1426393654"));
        assertNull(this.kb.getInstance("con2025477491"));
    }

    @Test
    public void testGetSubclasses() throws Exception {
        Set<String> expected = Sets.newHashSet("con430174676",
                "con983993996", "con1190652189", "con1608884093",
                "con1263361472", "con1954406752", "con1817180792",
                "con775697297", "con777335841", "con501245415", "con985632540",
                "con567400598", "con428536132", "con1952768208", "con15219784",
                "con1398948888", "con1744471509", "con1537813354",
                "con1331155161", "con1746110053");
        Set<Concept> subclasses = kb.getSubclasses(kb.getConcept("con2025477491"));
        assertEquals(expected.size(), subclasses.size());

        for(Concept subclass : subclasses){
            assertTrue(expected.contains(subclass.getID()));
        }
    }

    @Test
    public void testGetSuperclasses() throws Exception {
        Set<String> expected = Sets.newHashSet("con464583682",
                "con1226699739", "con1988815758");
        Set<Concept> superclasses = kb.getSuperclasses(kb.getConcept("con2025477491"));
        assertEquals(expected.size(), superclasses.size());

        for(Concept subclass : superclasses){
            assertTrue(expected.contains(subclass.getID()));
        }
    }

    @Test
    public void testEquivalent() throws Exception {
        assertEquals(kb.getConcept("con1988815758"), kb.getConcept("con1988815758"));
    }

    @Test
    public void testIsSubclass() throws Exception {
        Concept a = kb.getConcept("con983993996");
        Concept b = kb.getConcept("con2025477491");
        assertTrue(kb.isSubclass(a,b));
        assertFalse(kb.isSubclass(b,a));
    }

    @Test
    public void testIsSuperclass() throws Exception {
        Concept a = kb.getConcept("con983993996");
        Concept b = kb.getConcept("con2025477491");
        assertTrue(kb.isSuperclass(b,a));
        assertFalse(kb.isSuperclass(a,b));
    }

    @Test
    public void testResolveInstance() throws Exception {
        Instance instance = kb.getInstance("inst916200307");
        assertEquals("con985632540", kb.resolveInstance(instance).getID());
    }

    @Test
    public void testGetInstances() throws Exception {
        Set<String> expected = Sets.newHashSet("inst1678316326",
                "inst916200307", "inst223516483");
        Set<Instance> instances = kb.getInstances(kb.getConcept("con985632540"));
        assertEquals(expected.size(), instances.size());
        for(Instance instance : instances){
            assertTrue(expected.contains(instance.getID()));
        }
    }
}
