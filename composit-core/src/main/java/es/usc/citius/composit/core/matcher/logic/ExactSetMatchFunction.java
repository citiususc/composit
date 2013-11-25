/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
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
