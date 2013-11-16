package es.usc.citius.composit.core.matcher;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SetMatchFunctionDecoratorTest {

    private MatchFunction<String,Integer> mockedMatcher;
    private SetMatchFunctionDecorator<String,Integer> setMatcher;
    private Set<String> source = Sets.newHashSet("a", "b", "c");
    private Set<String> target = Sets.newHashSet("d", "e", "f");

    @Before
    public void setUp(){
        // Source elements: a, b, c
        // Target elements: d, e

        mockedMatcher = Mockito.mock(MatchFunction.class);
        setMatcher = new SetMatchFunctionDecorator<String, Integer>(mockedMatcher);

        // Configure matcher behaviour
        when(mockedMatcher.match("a","d")).thenReturn(1);
        when(mockedMatcher.match("a","e")).thenReturn(2);

        when(mockedMatcher.match("b","d")).thenReturn(0);
        when(mockedMatcher.match("b","e")).thenReturn(4);

        when(mockedMatcher.match("c","e")).thenReturn(5);
    }
    @Test
    public void testPartialMatch() throws Exception {
        MatchTable<String, Integer> result = setMatcher.partialMatch(source, target);
        // Only d and e matched
        assertEquals(Sets.newHashSet("d", "e"), result.getTargetElements());
        // Just the first element recorded
        assertEquals(1, result.getSortedSourceElemsThatMatch("d").size());
        assertEquals(1, result.getSortedSourceElemsThatMatch("e").size());
        assertNotNull(result.getSortedSourceElemsThatMatch("f"));
        assertTrue(result.getSortedSourceElemsThatMatch("f").isEmpty());
    }

    @Test
    public void testFullMatch() throws Exception {
        MatchTable<String, Integer> result = setMatcher.fullMatch(source, target);
        // Only d and e matched
        assertEquals(Sets.newHashSet("d", "e"), result.getTargetElements());
        // Only a and b match d
        assertEquals(Sets.newHashSet("a", "b"), result.getSortedSourceElemsThatMatch("d"));
        // a, b and c match e
        assertEquals(source, result.getSortedSourceElemsThatMatch("e"));
        assertNotNull(result.getSortedSourceElemsThatMatch("f"));
        assertTrue(result.getSortedSourceElemsThatMatch("f").isEmpty());
    }

    @Test
    public void testSortedTypesOnFullMatch(){
        MatchTable<String, Integer> result = setMatcher.fullMatch(source, target);
        SortedSet<String> elements = result.getSortedSourceElemsThatMatch("e");
        assertEquals("a",elements.first());
        assertEquals("c",elements.last());
    }

    @Test
    public void testMatch() throws Exception {
        assertEquals(1, setMatcher.match("a","d").intValue());
    }
}
