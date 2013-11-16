package es.usc.citius.composit.core.matcher.logic;

import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.MatchTable;
import es.usc.citius.composit.core.matcher.SetMatchFunction;

import java.util.Set;

/**
 * This class performs exact match of elements.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ExactSetMatchFunction<E> implements SetMatchFunction<E, LogicMatchType> {

    @Override
    public MatchTable<E, LogicMatchType> partialMatch(Set<E> source, Set<E> target) {
        // In this case the concept of partial match is identical to full match.
        return fullMatch(source, target);
    }

    @Override
    public MatchTable<E, LogicMatchType> fullMatch(Set<E> source, Set<E> target) {
        // Perform a set intersection
        Sets.SetView<E> intersection = Sets.intersection(source, target);
        MatchTable<E,LogicMatchType> result = new MatchTable<E, LogicMatchType>();
        for(E e : intersection){
            result.addMatch(e, e, LogicMatchType.EXACT);
        }
        return result;
    }

    @Override
    public LogicMatchType match(E source, E target) {
        if (source.equals(target)){
            return LogicMatchType.EXACT;
        }
        return null;
    }
}
