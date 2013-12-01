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

package es.usc.citius.composit.test.matcher.graph;

import es.usc.citius.composit.core.knowledge.Concept;
import es.usc.citius.composit.core.matcher.graph.HashMatchGraph;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.matcher.logic.LogicMatchType;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCHashMatchGraphTest {

    private static WSCTest.Dataset dataset;
    private static HashMatchGraph<Concept, LogicMatchType> matchGraph;

    @BeforeClass
    public static void setUp() throws Exception {
        dataset = WSCTest.TESTSET_2008_01.dataset();
        matchGraph = new HashMatchGraph<Concept, LogicMatchType>(dataset.getDefaultMatcher(), dataset.getKb().getConcepts());

    }

    @Test
    public void testMatchGraphElements(){
        int elements = matchGraph.getElements().size();
        assertEquals(1540, elements);
    }

    @Test
    public void testMatchGraphTargetAllTypesWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1653328292"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con677999980"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1578980503"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1988815758"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        expected.put(dataset.getKb().getConcept("con332477359"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con54748427"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1226699739"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1648412736"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1094593378"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con471341825"), LogicMatchType.SUBSUMES);
        Map<Concept, LogicMatchType> result = matchGraph.getTargetElementsMatchedBy(c);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphTargetExactWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        Map<Concept, LogicMatchType> result = matchGraph.getTargetElementsMatchedBy(c, LogicMatchType.EXACT, MatchGraph.TypeSelector.EXACT);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphTargetAtLeastPluginWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1653328292"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1988815758"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        expected.put(dataset.getKb().getConcept("con1226699739"), LogicMatchType.PLUGIN);
        Map<Concept, LogicMatchType> result = matchGraph.getTargetElementsMatchedBy(c, LogicMatchType.PLUGIN, MatchGraph.TypeSelector.AT_LEAST);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphTargetAtMostPluginWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1653328292"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con677999980"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1578980503"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1988815758"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con332477359"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con54748427"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1226699739"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1648412736"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1094593378"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con471341825"), LogicMatchType.SUBSUMES);
        Map<Concept, LogicMatchType> result = matchGraph.getTargetElementsMatchedBy(c, LogicMatchType.PLUGIN, MatchGraph.TypeSelector.AT_MOST);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphSourceWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1988815758"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con677999980"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1648412736"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1653328292"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        expected.put(dataset.getKb().getConcept("con1578980503"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con471341825"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1226699739"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con54748427"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con332477359"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1094593378"), LogicMatchType.PLUGIN);
        Map<Concept, LogicMatchType> result = matchGraph.getSourceElementsThatMatch(c);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphSourceExactWithWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        Map<Concept, LogicMatchType> result = matchGraph.getSourceElementsThatMatch(c, LogicMatchType.EXACT, MatchGraph.TypeSelector.EXACT);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphSourceAtLeastWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con677999980"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1648412736"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1233457844"), LogicMatchType.EXACT);
        expected.put(dataset.getKb().getConcept("con1578980503"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con471341825"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con54748427"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con332477359"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1094593378"), LogicMatchType.PLUGIN);
        Map<Concept, LogicMatchType> result = matchGraph.getSourceElementsThatMatch(c, LogicMatchType.PLUGIN, MatchGraph.TypeSelector.AT_LEAST);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchGraphSourceAtMostWSC01(){
        Concept c = dataset.getKb().getConcept("con1233457844");
        Map<Concept, LogicMatchType> expected = new HashMap<Concept, LogicMatchType>();
        expected.put(dataset.getKb().getConcept("con1988815758"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con677999980"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1648412736"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1653328292"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con1578980503"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con471341825"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1226699739"), LogicMatchType.SUBSUMES);
        expected.put(dataset.getKb().getConcept("con54748427"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con332477359"), LogicMatchType.PLUGIN);
        expected.put(dataset.getKb().getConcept("con1094593378"), LogicMatchType.PLUGIN);
        Map<Concept, LogicMatchType> result = matchGraph.getSourceElementsThatMatch(c, LogicMatchType.PLUGIN, MatchGraph.TypeSelector.AT_MOST);
        assertEquals(expected, result);
    }
}
