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

package es.usc.citius.composit.core.model.impl;


import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SignatureIOTest {
    @Test
    public void testEmptySignature() throws Exception {
        SignatureIO<String> signature = new SignatureIO<String>();
        assertTrue(signature.getInputs().isEmpty());
        assertTrue(signature.getOutputs().isEmpty());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testImmutableInputException() throws Exception {
        Set<String> inputs = Sets.newHashSet("A","B","C");
        Set<String> outputs = Sets.newHashSet("D","E");
        SignatureIO<String> signature = new SignatureIO<String>(inputs, outputs);
        signature.getInputs().clear();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testImmutableOutputException() throws Exception {
        Set<String> inputs = Sets.newHashSet("A","B","C");
        Set<String> outputs = Sets.newHashSet("D","E");
        SignatureIO<String> signature = new SignatureIO<String>(inputs, outputs);
        signature.getOutputs().clear();
    }

    @Test
    public void testImmutability() throws Exception {
        Set<String> inputs = Sets.newHashSet("A","B","C");
        Set<String> outputs = Sets.newHashSet("D","E");
        SignatureIO<String> signature = new SignatureIO<String>(inputs, outputs);
        inputs.clear();
        outputs.clear();
        assertEquals(Sets.newHashSet("A","B","C"), signature.getInputs());
        assertEquals(Sets.newHashSet("D","E"), signature.getOutputs());
    }
}
