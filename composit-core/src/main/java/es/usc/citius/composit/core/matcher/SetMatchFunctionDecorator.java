package es.usc.citius.composit.core.matcher;


import java.util.Set;

public class SetMatchFunctionDecorator<E,T extends Comparable<T>> implements SetMatchFunction<E,T> {
    private MatchFunction<E,T> decoratedMatcher;

    public SetMatchFunctionDecorator(MatchFunction<E, T> decoratedMatcher) {
        this.decoratedMatcher = decoratedMatcher;
    }

    private MatchTable<E, T> match(Set<E> source, Set<E> target, boolean full){
        // Compute partial match using the decorated match function
        MatchTable<E,T> result = new MatchTable<E, T>();
        for(E y : target){
            for(E x : source){
                T matchType = decoratedMatcher.match(x,y);
                if (matchType != null){
                    result.addMatch(x,y,matchType);
                    // We found a match between x and y.
                    // Skip to the next target element
                    if (!full) break;
                }
            }
        }
        return result;
    }

    @Override
    public MatchTable<E, T> partialMatch(Set<E> source, Set<E> target) {
        return match(source, target, false);
    }

    @Override
    public MatchTable<E, T> fullMatch(Set<E> source, Set<E> target) {
        return match(source, target, true);
    }

    @Override
    public T match(E source, E target) {
        return decoratedMatcher.match(source, target);
    }
}
