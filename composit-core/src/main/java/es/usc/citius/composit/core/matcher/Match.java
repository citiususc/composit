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


/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class Match<E,T> {
    private E matcher;
    private E matched;
    private T matchType;

    public Match(E matcher, E matched, T matchType) {
        if (matcher == null){
            throw new IllegalArgumentException("Matcher argument cannot be null");
        }
        this.matcher = matcher;
        if (matched == null){
            throw new IllegalArgumentException("Matched argument cannot be null");
        }
        this.matched = matched;
        if (matchType == null){
            throw new IllegalArgumentException("Match type argument cannot be null");
        }
        this.matchType = matchType;
    }

    public E getMatcher() {
        return matcher;
    }

    public E getMatched() {
        return matched;
    }

    public T getMatchType() {
        return matchType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (!matchType.equals(match.matchType)) return false;
        if (!matched.equals(match.matched)) return false;
        if (!matcher.equals(match.matcher)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = matcher.hashCode();
        result = 31 * result + matched.hashCode();
        result = 31 * result + matchType.hashCode();
        return result;
    }
}
