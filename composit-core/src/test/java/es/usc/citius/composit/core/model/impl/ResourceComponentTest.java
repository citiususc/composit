package es.usc.citius.composit.core.model.impl;


import org.junit.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceComponentTest {
    @Test
    public void testGetId() throws Exception {
        ResourceComponent testResource = new ResourceComponent("test");
        assertEquals("test", testResource.getID());
    }

    @Test
    public void testGetNullUri() throws Exception {
        ResourceComponent testResource = new ResourceComponent("test");
        assertEquals(null, testResource.getURI());
    }

    @Test
    public void testEqualityWithoutUri(){
        ResourceComponent testResource1 = new ResourceComponent("test");
        ResourceComponent testResource2 = new ResourceComponent("test");
        assertEquals(testResource1, testResource2);
    }

    @Test
    public void testEqualityWithUri(){
        ResourceComponent testResource1 = new ResourceComponent("test", URI.create("http://localhost/test#1"));
        ResourceComponent testResource2 = new ResourceComponent("test", URI.create("http://localhost/test#1"));
        assertEquals(testResource1, testResource2);
    }

    @Test
    public void testInequalityWithoutUri(){
        ResourceComponent testResource1 = new ResourceComponent("test1");
        ResourceComponent testResource2 = new ResourceComponent("test2");
        assertNotEquals(testResource1, testResource2);
    }

    @Test
    public void testInequalityWithUri(){
        ResourceComponent testResource1 = new ResourceComponent("test", URI.create("http://localhost/test#1"));
        ResourceComponent testResource2 = new ResourceComponent("test", URI.create("http://localhost/test#2"));
        assertNotEquals(testResource1, testResource2);
    }

    @Test
    public void testHashEqual(){
        Set<ResourceComponent> set = new HashSet<ResourceComponent>();
        set.add(new ResourceComponent("test1"));
        set.add(new ResourceComponent("test1"));
        assertEquals(1, set.size());
    }

    @Test
    public void testHashNotEqual(){
        Set<ResourceComponent> set = new HashSet<ResourceComponent>();
        set.add(new ResourceComponent("test1"));
        set.add(new ResourceComponent("test1", URI.create("http://localhost/test")));
        assertEquals(2, set.size());
    }

    @Test
    public void testToString() throws Exception {
        ResourceComponent testResource = new ResourceComponent("test");
        assertEquals("test", testResource.toString());
    }
}
