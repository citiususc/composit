/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC) http://citius.usc.es.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public class WSCKBMatchGraphTest {
    private static HierarchicalKnowledgeBase kb;
    private static WSCKBMatchGraph matchGraph;

    @BeforeClass
    public static void setUp() throws IOException {
        kb = WSCTest.TESTSET_2008_01.createKnowledgeBase();
        matchGraph = new WSCKBMatchGraph(kb);
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
