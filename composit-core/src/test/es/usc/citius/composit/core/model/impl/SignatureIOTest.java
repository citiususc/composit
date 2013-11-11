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
