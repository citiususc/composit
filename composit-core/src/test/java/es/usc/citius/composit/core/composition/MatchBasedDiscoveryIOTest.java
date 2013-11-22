package es.usc.citius.composit.core.composition;

import com.google.common.collect.Iterators;
import es.usc.citius.composit.core.matcher.graph.MatchGraph;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Signature;
import es.usc.citius.composit.core.model.impl.ResourceOperation;
import es.usc.citius.composit.core.model.impl.SignatureIO;
import es.usc.citius.composit.core.provider.ServiceDataProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class MatchBasedDiscoveryIOTest {
    private MatchBasedDiscoveryIO<String, Boolean> matchDiscovery;


    @Before
    public void setUp() throws Exception {
        // Create the match based discovery MatchGraph
        MatchGraph<String, Boolean> mockedMatchGraph = mock(MatchGraph.class);

        Map<String, Boolean> inputMatch = new HashMap<String, Boolean>();
        inputMatch.put("a", true);
        inputMatch.put("c", true);

        Map<String, Boolean> outputMatch = new HashMap<String, Boolean>();
        outputMatch.put("b", true);
        outputMatch.put("d", true);

        // x -> a, c (match x with a and c inputs)
        when(mockedMatchGraph.getTargetElementsMatchedBy("x")).thenReturn(inputMatch);

        // b, d -> y (match b and d with y)
        when(mockedMatchGraph.getSourceElementsThatMatch("y")).thenReturn(outputMatch);

        // Dummy operations
        ResourceOperation<String> op1 = new ResourceOperation<String>("Operation1", SignatureIO.<String>empty());
        ResourceOperation<String> op2 = new ResourceOperation<String>("Operation2", SignatureIO.<String>empty());

        // Configure a mocked service provider
        ServiceDataProvider<String> mockedDataProvider = mock(ServiceDataProvider.class);

        // Note that there is a inconsistency here. The mocked provider provides operations with empty signatures
        // which are suppose to use inputs a, c and return outputs b and d, but is not relevant for the test.
        when(mockedDataProvider.getOperationsWithInput("a")).thenReturn(Collections.<Operation<String>>singleton(op1));
        when(mockedDataProvider.getOperationsWithOutput("b")).thenReturn(Collections.<Operation<String>>singleton(op1));
        when(mockedDataProvider.getOperationsWithInput("c")).thenReturn(Collections.<Operation<String>>singleton(op2));
        when(mockedDataProvider.getOperationsWithOutput("d")).thenReturn(Collections.<Operation<String>>singleton(op2));

        matchDiscovery = new MatchBasedDiscoveryIO<String, Boolean>(mockedMatchGraph, mockedDataProvider);
    }

    @Test
    public void testDiscoverOperationsForInput() throws Exception {
        // Get all services that can consume input x (Op1 and Op2).
        Set<Operation<String>> result = matchDiscovery.discoverOperationsForInput("x");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDiscoverOperationsForOutput() throws Exception {
        // Get all services that can provide y (Op1 and Op2)
        Set<Operation<String>> result = matchDiscovery.discoverOperationsForOutput("y");
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
