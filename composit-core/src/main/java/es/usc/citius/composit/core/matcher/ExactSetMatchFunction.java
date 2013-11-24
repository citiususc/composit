package es.usc.citius.composit.core.matcher;


import com.google.common.collect.Sets;

import java.util.Set;

public class ExactSetMatchFunction<E> implements SetMatchFunction<E, Boolean> {

    @Override
    public MatchTable<E, Boolean> partialMatch(Set<E> source, Set<E> target) {
        return fullMatch(source, target);
    }

    @Override
    public MatchTable<E, Boolean> fullMatch(Set<E> source, Set<E> target) {
        Sets.SetView<E> result = Sets.intersection(source, target);
        MatchTable<E, Boolean> matchTable = new MatchTable<E, Boolean>();
        for(E e : result){
            matchTable.addMatch(e,e,true);
        }
        return matchTable;
    }

    @Override
    public Boolean match(E source, E target) {
        return source.equals(target);
    }
}
