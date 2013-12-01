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
