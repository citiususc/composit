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

import java.util.Map;
import java.util.Set;

/**
 * This interface defines the main methods for matching sets of elements.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface SetMatchFunction<E,T extends Comparable<T>> extends MatchFunction<E,T> {


    /**
     * Find at most one match from source to target. The first
     * element from source that matches an element from target is recorded.
     * The result does not have all matches between source and target.
     * This method can be used to fast check if each element from target
     * has at least one element from source that match it.
     *
     * @param source source elements (elements to match).
     * @param target target elements (elements to be matched).
     * @return a {@link Map} with keys = matcher elements and values = {@link Match} instances.
     */
    MatchTable<E,T> partialMatch(Set<E> source, Set<E> target);


    /**
     * Compute all matches between set x (source) and set y (target).
     * @param source {@link Set} of elements to match.
     * @param target {@link Set} elements to be matched.
     * @return
     */
    MatchTable<E,T> fullMatch(Set<E> source, Set<E> target);
}
