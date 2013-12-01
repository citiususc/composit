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

package es.usc.citius.composit.core.matcher.graph;

import es.usc.citius.composit.core.matcher.SetMatchFunction;

import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public interface MatchGraph<E,T extends Comparable<T>> extends SetMatchFunction<E,T> {

    enum TypeSelector {
        AT_LEAST, AT_MOST, EXACT
    }

    Set<E> getElements();
    /**
     * from source to target
     * @param source
     * @return
     */
    Map<E,T> getTargetElementsMatchedBy(E source);

    /**
     * from target to source
     * @param target
     * @return
     */
    Map<E,T> getSourceElementsThatMatch(E target);

    /**
     * Get all target elements in the graph that are matched by source with match type t.
     * @param source
     * @return
     */
    Map<E,T> getTargetElementsMatchedBy(E source, T type, TypeSelector selector);

    /**
     *
     * @param target
     * @param type
     * @return
     */
    Map<E,T> getSourceElementsThatMatch(E target, T type, TypeSelector selector);
}
