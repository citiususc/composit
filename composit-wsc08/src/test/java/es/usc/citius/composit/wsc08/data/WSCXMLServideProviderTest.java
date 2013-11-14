package es.usc.citius.composit.wsc08.data;

import com.google.common.collect.Sets;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Service;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class WSCXMLServideProviderTest {

    private WSCXMLServideProvider provider;

    @Before
    public void setUp() throws IOException {
        this.provider = WSCTest.TESTSET_2008_01.createXmlResourceProvider();
    }

    @Test
    public void testGetExistingService() throws Exception {
        Service<String> service = provider.getService("serv699915007");
        assertEquals("serv699915007", service.getID());
        assertEquals(1, service.getOperations().size());
        assertEquals("serv699915007Operation", service.getOperations().iterator().next().getID());
    }

    @Test
    public void testGetNonExistingService() throws Exception {
        Service<String> service = provider.getService("serv699915007Operation");
        assertNull(service);
    }

    @Test
    public void testGetOperation() throws Exception {
        Operation<String> op = provider.getOperation("serv699915007Operation");
        assertNotNull(op);
        assertEquals("serv699915007", op.getServiceOwner().getID());
        // Expected instance inputs
        Set<String> expectedInputs = Sets.newHashSet("inst1716616603", "inst1689375842", "inst102675811");
        // Expected instance outputs
        Set<String> expectedOutputs =  Sets.newHashSet("inst725927364", "inst731046963", "inst1631413303");
        assertEquals(expectedInputs, op.getSignature().getInputs());
        assertEquals(expectedOutputs, op.getSignature().getOutputs());
    }

    @Test
    public void testGetOperations() throws Exception {
        Set<Operation<String>> operations = Sets.newHashSet(provider.getOperations());
        assertEquals(158, operations.size());
        boolean exists = false;
        for(Operation<String> op : operations){
            if (op.getID().equals("serv699915007Operation")){
                exists = true;
                break;
            }
        }
        assertTrue(exists);
    }

    @Test
    public void testGetServices() throws Exception {
        assertEquals(158, Sets.newHashSet(provider.getServices()).size());
    }

    @Test
    public void testListOperations() throws Exception {
        assertTrue(provider.listOperations().contains("serv699915007Operation"));
        assertTrue(provider.listOperations().contains("serv1531463259Operation"));
        assertTrue(provider.listOperations().contains("serv76663416Operation"));
        assertTrue(provider.listOperations().contains("serv630482774Operation"));
        assertTrue(provider.listOperations().contains("serv2085282617Operation"));
        assertFalse(provider.listOperations().contains("serv699915007"));
        assertFalse(provider.listOperations().contains("serv1531463259"));
        assertFalse(provider.listOperations().contains("serv76663416"));
        assertFalse(provider.listOperations().contains("serv630482774"));
        assertFalse(provider.listOperations().contains("serv2085282617"));
    }

    @Test
    public void testListServices() throws Exception {
        assertTrue(provider.listServices().contains("serv699915007"));
        assertTrue(provider.listServices().contains("serv1531463259"));
        assertTrue(provider.listServices().contains("serv76663416"));
        assertTrue(provider.listServices().contains("serv630482774"));
        assertTrue(provider.listServices().contains("serv2085282617"));
        assertFalse(provider.listServices().contains("serv699915007Operation"));
        assertFalse(provider.listServices().contains("serv1531463259Operation"));
        assertFalse(provider.listServices().contains("serv76663416Operation"));
        assertFalse(provider.listServices().contains("serv630482774Operation"));
        assertFalse(provider.listServices().contains("serv2085282617Operation"));
    }
}
