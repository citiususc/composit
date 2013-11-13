package es.usc.citius.composit.core.matcher.logic;

import com.google.common.collect.Sets;
import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.matcher.SetMatchResult;

import java.util.Set;

/**
 * This class performs exact match of elements.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class ExactSetMatchFunction<E> implements SetMatchFunction<E, LogicMatchType> {

    @Override
    public SetMatchResult<E, LogicMatchType> partialMatch(Set<E> source, Set<E> target) {
        // In this case the concept of partial match is identical to full match.
        return fullMatch(source, target);
    }

    @Override
    public SetMatchResult<E, LogicMatchType> fullMatch(Set<E> source, Set<E> target) {
        // Perform a set intersection
        Sets.SetView<E> intersection = Sets.intersection(source, target);
        SetMatchResult<E,LogicMatchType> result = new SetMatchResult<E, LogicMatchType>();
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
