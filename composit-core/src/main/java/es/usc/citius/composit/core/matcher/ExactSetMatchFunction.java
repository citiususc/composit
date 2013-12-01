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
