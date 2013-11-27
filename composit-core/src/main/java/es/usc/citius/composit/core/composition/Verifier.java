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

package es.usc.citius.composit.core.composition;

import es.usc.citius.composit.core.matcher.SetMatchFunction;
import es.usc.citius.composit.core.model.Operation;
import es.usc.citius.composit.core.model.Operations;

import java.util.HashSet;
import java.util.Set;

/**
 * Util class to verify the correctness of a composition.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Verifier {

    /**
     * The composition, represented as a leveled structure of operations is validated using the provided matcher.
     * @param composition Composition represented as a leveled operation structure (directed acyclic graph without match links).
     * @param matcher Matcher that will be used to compute I/O matches.
     * @param <E> type of the inputs/outputs of the operations.
     * @param <T> type of the match links.
     * @return true if the composition is forward-invokable, from source to sink node.
     */
    public static <E,T extends Comparable<T>> boolean satisfies(LeveledServices<E> composition, SetMatchFunction<E,T> matcher){
        // Check if the composition is well-formed and satisfies the request
        Set<E> available = new HashSet<E>(composition.getSource().getSignature().getOutputs());
        for(int level=1; level < composition.numberOfLevels()-1; level++){
            Set<Operation<E>> operations = composition.getOperationsAtLevel(level);
            // Check invokability
            for(Operation<E> op : operations){
                if (!matcher.fullMatch(available, op.getSignature().getInputs()).getTargetElements().equals(op.getSignature().getInputs())){
                    // This operation cannot be executed
                    return false;
                }
            }
            available.addAll(Operations.outputs(operations));
            if (matcher.fullMatch(available, composition.getSink().getSignature().getInputs()).getTargetElements().equals(composition.getSink().getSignature().getInputs())){
                return true;
            }
        }
        return false;
    }

}
