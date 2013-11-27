/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
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

package es.usc.citius.composit.test.composition;

import es.usc.citius.composit.core.composition.search.ComposIT;
import es.usc.citius.composit.wsc08.data.WSCTest;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class CompositSearchTest {


    private void run(WSCTest test) throws IOException {
        WSCTest.Dataset dataset = test.dataset();
        ComposIT.search(dataset.getDefaultCompositionProblem(), dataset.getRequest());
    }

    @Test
    public void testComposition01() throws Exception {
        run(WSCTest.TESTSET_2008_01);
    }

    @Test
    public void testComposition02() throws Exception {
        run(WSCTest.TESTSET_2008_02);
    }

    @Test
    public void testComposition03() throws Exception {
        run(WSCTest.TESTSET_2008_03);
    }

    @Test
    public void testComposition04() throws Exception {
        run(WSCTest.TESTSET_2008_04);
    }

    @Test
    public void testComposition05() throws Exception {
        run(WSCTest.TESTSET_2008_05);
    }

    @Test
    public void testComposition06() throws Exception {
        run(WSCTest.TESTSET_2008_06);
    }

    @Test
    public void testComposition07() throws Exception {
        run(WSCTest.TESTSET_2008_07);
    }

    @Test
    public void testComposition08() throws Exception {
        run(WSCTest.TESTSET_2008_08);
    }
}
