package es.usc.citius.composit.core.model.impl;


import org.junit.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceTest {
    @Test
    public void testGetId() throws Exception {
        Resource testResource = new Resource("test");
        assertEquals("test", testResource.getId());
    }

    @Test
    public void testGetNullUri() throws Exception {
        Resource testResource = new Resource("test");
        assertEquals(null, testResource.getUri());
    }

    @Test
    public void testEqualityWithoutUri(){
        Resource testResource1 = new Resource("test");
        Resource testResource2 = new Resource("test");
        assertEquals(testResource1, testResource2);
    }

    @Test
    public void testEqualityWithUri(){
        Resource testResource1 = new Resource("test", URI.create("http://localhost/test#1"));
        Resource testResource2 = new Resource("test", URI.create("http://localhost/test#1"));
        assertEquals(testResource1, testResource2);
    }

    @Test
    public void testInequalityWithoutUri(){
        Resource testResource1 = new Resource("test1");
        Resource testResource2 = new Resource("test2");
        assertNotEquals(testResource1, testResource2);
    }

    @Test
    public void testInequalityWithUri(){
        Resource testResource1 = new Resource("test", URI.create("http://localhost/test#1"));
        Resource testResource2 = new Resource("test", URI.create("http://localhost/test#2"));
        assertNotEquals(testResource1, testResource2);
    }

    @Test
    public void testHashEqual(){
        Set<Resource> set = new HashSet<Resource>();
        set.add(new Resource("test1"));
        set.add(new Resource("test1"));
        assertEquals(1, set.size());
    }

    @Test
    public void testHashNotEqual(){
        Set<Resource> set = new HashSet<Resource>();
        set.add(new Resource("test1"));
        set.add(new Resource("test1", URI.create("http://localhost/test")));
        assertEquals(2, set.size());
    }

    @Test
    public void testToString() throws Exception {
        Resource testResource = new Resource("test");
        assertEquals("test", testResource.toString());
    }
}
