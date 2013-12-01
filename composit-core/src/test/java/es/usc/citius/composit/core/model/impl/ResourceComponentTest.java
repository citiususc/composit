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
