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

package es.usc.citius.composit.core.composition.search;

import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.impl.DummyOperation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class State<E> {
    // Operations selected for this state
    Set<Operation<E>> stateOperations = new HashSet<Operation<E>>();
    // Level of the service network in which this state is contained
    int level;

    public State(Collection<Operation<E>> stateOperations, int level) {
        this.stateOperations = new HashSet<Operation<E>>(stateOperations);
        this.level = level;
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be less than 0");
        }
    }

    public int countServices() {
        int count = 0;
        // Count real selected services (avoid no-op dummies)
        for (Operation<E> op : stateOperations) {
            if (!(op instanceof DummyOperation)) {
                count++;
            }
        }
        return count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (level != state.level) return false;
        if (!stateOperations.equals(state.stateOperations)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateOperations.hashCode();
        result = 31 * result + level;
        return result;
    }

    @Override
    public String toString() {
        return stateOperations.toString();
    }

    public Set<Operation<E>> getStateOperations() {
        return stateOperations;
    }

    public int getLevel() {
        return level;
    }
}
